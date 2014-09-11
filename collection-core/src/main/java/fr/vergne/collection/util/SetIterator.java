package fr.vergne.collection.util;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A {@link SetIterator} aims at providing a way to generate an exhaustive
 * sequence of possible {@link Set}s. Each {@link Set} is a subset of the one
 * provided in the constructor.<br/>
 * <br/>
 * Typically, a {@link SetIterator} can be used to generate a <a
 * href="http://en.wikipedia.org/wiki/Power_set">power set</a>, but the empty
 * set is not generated. If such a set need to be considered, it should be so
 * separately to this {@link SetIterator}.<br/>
 * <br/>
 * Additional methods are provided such as {@link #getAmountOfPossibleSets()} or
 * {@link #isPossibleSet(set)} to be able to evaluate a given {@link Set}
 * regarding the full set, without having to generate this full set.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <T>
 */
public class SetIterator<T> implements Iterator<Set<T>> {

	private final List<T> values;
	private LinkedList<Integer> currentSet = new LinkedList<Integer>();

	public SetIterator(Set<T> values) {
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

	public BigInteger getAmountOfPossibleSets() {
		return BigInteger.valueOf(2).pow(values.size())
				.subtract(BigInteger.ONE);
	}

	public boolean isPossibleSet(Set<T> set) {
		return !set.isEmpty() && values.containsAll(set);
	}
}
