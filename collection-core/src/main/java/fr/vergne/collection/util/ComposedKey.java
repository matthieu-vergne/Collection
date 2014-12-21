package fr.vergne.collection.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A {@link ComposedKey} is an utilitary class to compose several pieces of data
 * in a single one and use this composition as a key, for instance in a
 * {@link Map}. Such a key need to be considered as equal to another if both
 * provide the equivalent objects (a.equals(b), not necessarily a == b) in the
 * same order. To keep the comparison consistent over time, a
 * {@link ComposedKey} is made as an immutable object, so it is not possible to
 * change the data it contains.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <T>
 */
public class ComposedKey<T> implements Iterable<T> {

	private final List<T> keys;

	public ComposedKey(List<T> keys) {
		this.keys = Collections.unmodifiableList(new ArrayList<T>(keys));
	}

	public ComposedKey(T... keys) {
		this(Arrays.asList(keys));
	}

	@SuppressWarnings("unchecked")
	public <TT extends T> TT get(int index) {
		return (TT) keys.get(index);
	}

	public int size() {
		return keys.size();
	}

	@Override
	public Iterator<T> iterator() {
		return keys.iterator();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof ComposedKey) {
			ComposedKey<?> k = (ComposedKey<?>) obj;
			Iterator<T> iterator1 = iterator();
			Iterator<?> iterator2 = k.iterator();
			while (iterator1.hasNext() && iterator2.hasNext()) {
				Object key2 = iterator2.next();
				T key1 = iterator1.next();
				if (key1 == key2 || key1 != null && key1.equals(key2)) {
					// still the same
				} else {
					return false;
				}
			}
			return !iterator1.hasNext() && !iterator2.hasNext();
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int hash = 1;
		for (T key : keys) {
			hash *= key.hashCode();
		}
		return hash;
	}
}
