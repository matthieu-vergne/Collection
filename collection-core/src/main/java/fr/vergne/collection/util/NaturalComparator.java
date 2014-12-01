package fr.vergne.collection.util;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NaturalComparator implements Comparator<String> {

	@Override
	public int compare(String s1, String s2) {
		Chunker chunker1 = new Chunker(s1);
		Chunker chunker2 = new Chunker(s2);

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

	private class Chunker implements Iterator<Object> {
		private final Matcher matcher;
		private int remaining;

		public Chunker(String string) {
			Pattern pattern = Pattern
					.compile("[^0-9]+|[0-9]+([.,][0-9]+)?(E-?[0-9]+)?");
			matcher = pattern.matcher(string);
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
			try {
				return new BigDecimal(chunk);
			} catch (NumberFormatException e) {
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
