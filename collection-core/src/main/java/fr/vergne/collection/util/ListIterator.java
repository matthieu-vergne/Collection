package fr.vergne.collection.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A {@link ListIterator} aims at providing a way to generate an exhaustive
 * sequence of possible {@link List}s. Each {@link List} has the same size and
 * is consistent regarding the order of its elements: if the
 * {@link ListIterator} is configured with a set of {@link Integer}s, a set of
 * {@link String}s and a set of {@link Boolean}s, then the {@link List}s
 * generated always have an {@link Integer}, a {@link String} and a
 * {@link Boolean} in the same order.<br/>
 * <br/>
 * Typically, a {@link ListIterator} can be used to generate a set of vectors or
 * possible configurations, where each configuration uses the same pattern of
 * (heterogeneous) parameters.<br/>
 * <br/>
 * Additional methods are provided such as {@link #getAmountOfPossibleLists()}
 * or {@link #isPossibleList(List)} to be able to evaluate a given {@link List}
 * regarding the full set, without having to generate this full set.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <T>
 */
public class ListIterator<T> implements Iterator<List<T>> {

	private final List<Set<T>> potentialValues;
	private final List<Iterator<T>> iterators = new ArrayList<Iterator<T>>();
	private final List<T> lastValues = new ArrayList<T>();
	private BigInteger count = new BigInteger("0");
	private BigInteger amountOfPossibleLists = null;

	@SafeVarargs
	public ListIterator(Collection<? extends T>... possibleValues) {
		this(false, possibleValues);
	}

	@SafeVarargs
	public ListIterator(boolean allowEmpty,
			Collection<? extends T>... possibleValues) {
		List<Set<T>> potentialValues = new ArrayList<Set<T>>();
		for (Collection<? extends T> values : possibleValues) {
			potentialValues.add(new LinkedHashSet<T>(values));
		}
		for (Set<T> values : potentialValues) {
			if (!allowEmpty && values.isEmpty()) {
				throw new RuntimeException(
						"One of the node have no known potential value. We cannot compute its possible values.");
			} else {
				iterators.add(values.iterator());
			}
		}
		this.potentialValues = Collections.unmodifiableList(potentialValues);
	}

	public <ObjectType> ListIterator(
			Map<ObjectType, ? extends Collection<T>> possibleValues) {
		this(possibleValues, Collections.<ObjectType, T> emptyMap(),
				possibleValues.keySet(), false);
	}

	public <ObjectType> ListIterator(
			Map<ObjectType, ? extends Collection<T>> possibleValues,
			Map<ObjectType, T> observedValues,
			Collection<ObjectType> elementsConsidered) {
		this(possibleValues, observedValues, elementsConsidered, false);
	}

	public <ObjectType> ListIterator(
			Map<ObjectType, ? extends Collection<T>> possibleValues,
			Map<ObjectType, T> observedValues,
			Collection<ObjectType> elementsConsidered, boolean allowEmpty) {
		List<Set<T>> potentialValues = new ArrayList<Set<T>>();
		List<ObjectType> pattern = new LinkedList<ObjectType>(
				elementsConsidered);
		for (int i = 0; i < pattern.size(); i++) {
			potentialValues.add(null);
		}

		for (Entry<ObjectType, T> entry : observedValues.entrySet()) {
			ObjectType node = entry.getKey();
			int index = pattern.indexOf(node);
			if (elementsConsidered.contains(node)) {
				LinkedHashSet<T> singleton = new LinkedHashSet<T>();
				singleton.add(entry.getValue());
				potentialValues.set(index, singleton);
			} else {
				continue;
			}
		}
		for (Entry<ObjectType, ? extends Collection<T>> entry : possibleValues
				.entrySet()) {
			ObjectType node = entry.getKey();
			int index = pattern.indexOf(node);
			if (elementsConsidered.contains(node)
					&& potentialValues.get(index) == null) {
				potentialValues.set(index,
						new LinkedHashSet<T>(entry.getValue()));
			} else {
				continue;
			}
		}
		for (Set<T> values : potentialValues) {
			if (!allowEmpty && values.isEmpty()) {
				throw new RuntimeException(
						"One of the node have no known potential value. We cannot compute its possible values.");
			} else {
				iterators.add(values.iterator());
			}
		}
		this.potentialValues = Collections.unmodifiableList(potentialValues);
	}

	public BigInteger getAmountOfPossibleLists() {
		if (amountOfPossibleLists == null) {
			amountOfPossibleLists = new BigInteger("1");
			for (Set<?> values : potentialValues) {
				amountOfPossibleLists = amountOfPossibleLists.multiply(new BigInteger("" + values.size()));
			}
		} else {
			// already computed
		}
		return amountOfPossibleLists;
	}

	public BigInteger getAmountOFGeneratedLists() {
		return count;
	}

	public boolean isPossibleList(List<Object> list) {
		for (int i = 0; i < potentialValues.size(); i++) {
			if (potentialValues.get(i).contains(list.get(i))) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean hasNext() {
		if (getAmountOfPossibleLists().compareTo(BigInteger.ZERO) > 0) {
			for (Iterator<?> iterator : iterators) {
				if (iterator.hasNext()) {
					return true;
				} else {
					continue;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	@Override
	public List<T> next() {
		if (lastValues.isEmpty()) {
			for (Iterator<T> iterator : iterators) {
				lastValues.add(iterator.next());
			}
		} else {
			int index = lastValues.size() - 1;
			setLastValueFor(index);
		}
		count = count.add(new BigInteger("1"));
		return new LinkedList<T>(lastValues);
	}

	private void setLastValueFor(int index) {
		Iterator<T> iterator = iterators.get(index);
		if (iterator.hasNext()) {
			// use this iterator
		} else {
			iterator = potentialValues.get(index).iterator();
			iterators.set(index, iterator);
			setLastValueFor(index - 1);
		}
		lastValues.set(index, iterator.next());
	}

	@Override
	public void remove() {
		throw new RuntimeException("Not implemented.");
	}

	public static void main(String[] args) {
		Map<Integer, Set<Integer>> nodePossibleValues = new HashMap<Integer, Set<Integer>>();
		nodePossibleValues.put(1, new HashSet<Integer>(Arrays.asList(1)));
		nodePossibleValues.put(2, new HashSet<Integer>(Arrays.asList(1, 2)));
		nodePossibleValues.put(3, new HashSet<Integer>(Arrays.asList(1, 2, 3)));
		nodePossibleValues.put(4,
				new HashSet<Integer>(Arrays.asList(1, 2, 3, 4)));
		nodePossibleValues.put(5,
				new HashSet<Integer>(Arrays.asList(1, 2, 3, 4, 5)));

		Map<Integer, Integer> nodeObservedValues = new HashMap<Integer, Integer>();
		nodeObservedValues.put(3, 2);

		List<Integer> nodesConsidered = new LinkedList<Integer>();
		nodesConsidered.add(1);
		nodesConsidered.add(2);
		nodesConsidered.add(3);
		nodesConsidered.add(4);

		ListIterator<Integer> iterator = new ListIterator<Integer>(
				nodePossibleValues, nodeObservedValues, nodesConsidered);
		while (iterator.hasNext()) {
			List<Integer> values = iterator.next();
			System.out.println(values);
		}
	}
}
