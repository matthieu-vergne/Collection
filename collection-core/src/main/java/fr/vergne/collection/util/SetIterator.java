package fr.vergne.collection.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class SetIterator<T> implements Iterator<Set<T>> {

	private final List<T> values;
	private LinkedList<Integer> currentSet = new LinkedList<Integer>();

	public SetIterator(Collection<T> values) {
		this.values = new LinkedList<T>(values);
	}

	@Override
	public Set<T> next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		} else {
			int size = currentSet.size();
			while (!currentSet.isEmpty()
					&& currentSet.getLast() == values.size() - 1 - size
							+ currentSet.size()) {
				currentSet.removeLast();
			}

			if (currentSet.isEmpty()) {
				currentSet.clear();
				for (int i = 0; i < size + 1; i++) {
					currentSet.addLast(currentSet.size());
				}
			} else {
				Integer index = currentSet.removeLast();
				while (currentSet.size() < size) {
					index++;
					currentSet.addLast(index);
				}
			}

			LinkedHashSet<T> set = new LinkedHashSet<T>();
			for (Integer index : currentSet) {
				set.add(values.get(index));
			}
			return set;
		}
	}

	@Override
	public boolean hasNext() {
		return currentSet.size() < values.size();
	}

	@Override
	public void remove() {
		throw new RuntimeException();
	}
}
