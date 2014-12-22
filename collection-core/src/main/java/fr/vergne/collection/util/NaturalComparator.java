package fr.vergne.collection.util;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link NaturalComparator} allows to compare two elements in a human way,
 * which is commonly interpreted as a comparison which:
 * <ul>
 * <li>is not case sensitive</li>
 * <li>treat numbers as numbers (not as strings)</li>
 * </ul>
 * For example, "file" and "File" are merely the same names and so should be
 * grouped together. Also, while "file2" is sorted after "file10" with a
 * classical sorting (the character "1" is before "2"), a human expect it to be
 * sorted before because of the counting (2 is before 10).
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public class NaturalComparator<T> implements Comparator<T> {

	private final Translator<T> translator;

	/**
	 * Instantiate a basic {@link NaturalComparator} which compare the elements
	 * based on their string representation ({@link Object#toString()}).
	 */
	public NaturalComparator() {
		this(new Translator<T>() {

			@Override
			public String toString(T object) {
				return object.toString();
			}
		});
	}

	/**
	 * Instantiate a {@link NaturalComparator} which exploits a specific string
	 * representation for the elements.
	 */
	public NaturalComparator(Translator<T> translator) {
		this.translator = translator;
	}

	public interface Translator<T> {
		public String toString(T object);
	}

	@Override
	public int compare(T s1, T s2) {
		Chunker chunker1 = new Chunker(translator.toString(s1));
		Chunker chunker2 = new Chunker(translator.toString(s2));

		while (chunker1.hasNext() && chunker2.hasNext()) {
			Object chunk1 = chunker1.next();
			Object chunk2 = chunker2.next();
			if (chunk1 instanceof String && chunk2 instanceof String) {
				int comparison = ((String) chunk1)
						.compareToIgnoreCase((String) chunk2);
				if (comparison == 0) {
					// wait for next chunk
				} else {
					return comparison;
				}
			} else if (chunk1 instanceof BigDecimal
					&& chunk2 instanceof BigDecimal) {
				int comparison = ((BigDecimal) chunk1)
						.compareTo((BigDecimal) chunk2);
				if (comparison == 0) {
					// wait for next chunk
				} else {
					return comparison;
				}
			} else if (chunk1 instanceof String && chunk2 instanceof BigDecimal) {
				return ("" + chunk1).compareTo("" + chunk2);
			} else if (chunk1 instanceof BigDecimal && chunk2 instanceof String) {
				return ("" + chunk1).compareTo("" + chunk2);
			} else {
				throw new RuntimeException("Unmanaged combination of chunks: "
						+ chunk1 + " VS " + chunk2);
			}
		}
		if (chunker1.hasNext() && !chunker2.hasNext()) {
			return ("" + chunker1.next()).compareTo("");
		} else if (!chunker1.hasNext() && chunker2.hasNext()) {
			return ("").compareTo("" + chunker2.next());
		} else {
			return 0;
		}
	}

	private static class Chunker implements Iterator<Object> {
		private static final Pattern PATTERN = Pattern
				.compile("[^0-9]++|[0-9]++([.,][0-9]++)?(E-?[0-9]++)?");
		private final Matcher matcher;
		private int remaining;

		public Chunker(String string) {
			matcher = PATTERN.matcher(string);
			remaining = string.length();
		}

		@Override
		public boolean hasNext() {
			return remaining > 0;
		}

		@Override
		public Object next() {
			matcher.find();
			String chunk = matcher.group();
			remaining -= chunk.length();
			char c = chunk.charAt(0);
			if (c >= '0' && c <= '9') {
				return new BigDecimal(chunk);
			} else {
				return chunk.trim();
			}
		}

		@Override
		public void remove() {
			throw new RuntimeException(
					"You cannot remove a chunk with this iterator.");
		}

	}

}
