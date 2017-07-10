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
 * href="http://en.wikipedia.org/wiki/Power_set">power set</a>.<br/>
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
	private BigInteger next;

	public SetIterator(Set<T> values) {
		this.values = new LinkedList<T>(values);
		this.next = getAmountOfPossibleSets();
	}

	@Override
	public Set<T> next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		} else {
			LinkedHashSet<T> set = new LinkedHashSet<T>();
			for (int index = 0; index < values.size(); index++) {
				if (next.testBit(index)) {
					set.add(values.get(index));
				} else {
					// not wanted
				}
			}
			next = next.subtract(BigInteger.ONE);
			return set;
		}
	}

	@Override
	public boolean hasNext() {
		return next.compareTo(BigInteger.ZERO) > 0;
	}

	@Override
	public void remove() {
		throw new RuntimeException();
	}

	public BigInteger getAmountOfPossibleSets() {
		return BigInteger.valueOf(2).pow(values.size());
	}

	public boolean isPossibleSet(Set<T> set) {
		return !set.isEmpty() && values.containsAll(set);
	}
}
