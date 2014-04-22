package fr.vergne.collection.impl;

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

public class VectorsIterator<ValueType> implements Iterator<List<ValueType>> {

	private final List<Set<ValueType>> potentialValues = new ArrayList<Set<ValueType>>();
	private final List<Iterator<ValueType>> iterators = new ArrayList<Iterator<ValueType>>();
	private final List<ValueType> lastValues = new ArrayList<ValueType>();
	private BigInteger count = new BigInteger("0");

	public VectorsIterator(Collection<? extends ValueType>... possibleValues) {
		this(false, possibleValues);
	}

	public VectorsIterator(boolean allowEmpty,
			Collection<? extends ValueType>... possibleValues) {
		for (Collection<? extends ValueType> values : possibleValues) {
			potentialValues.add(new LinkedHashSet<ValueType>(values));
		}
		for (Set<ValueType> values : potentialValues) {
			if (!allowEmpty && values.isEmpty()) {
				throw new RuntimeException(
						"One of the node have no known potential value. We cannot compute its possible values.");
			} else {
				iterators.add(values.iterator());
			}
		}
	}

	public <ObjectType> VectorsIterator(
			Map<ObjectType, ? extends Collection<ValueType>> possibleValues) {
		this(possibleValues, Collections.<ObjectType, ValueType> emptyMap(),
				possibleValues.keySet(), false);
	}

	public <ObjectType> VectorsIterator(
			Map<ObjectType, ? extends Collection<ValueType>> possibleValues,
			Map<ObjectType, ValueType> observedValues,
			Collection<ObjectType> elementsConsidered) {
		this(possibleValues, observedValues, elementsConsidered, false);
	}

	public <ObjectType> VectorsIterator(
			Map<ObjectType, ? extends Collection<ValueType>> possibleValues,
			Map<ObjectType, ValueType> observedValues,
			Collection<ObjectType> elementsConsidered, boolean allowEmpty) {
		List<ObjectType> pattern = new LinkedList<ObjectType>(
				elementsConsidered);
		for (int i = 0; i < pattern.size(); i++) {
			potentialValues.add(null);
		}

		for (Entry<ObjectType, ValueType> entry : observedValues.entrySet()) {
			ObjectType node = entry.getKey();
			int index = pattern.indexOf(node);
			if (elementsConsidered.contains(node)) {
				LinkedHashSet<ValueType> singleton = new LinkedHashSet<ValueType>();
				singleton.add(entry.getValue());
				potentialValues.set(index, singleton);
			} else {
				continue;
			}
		}
		for (Entry<ObjectType, ? extends Collection<ValueType>> entry : possibleValues
				.entrySet()) {
			ObjectType node = entry.getKey();
			int index = pattern.indexOf(node);
			if (elementsConsidered.contains(node)
					&& potentialValues.get(index) == null) {
				potentialValues.set(index,
						new LinkedHashSet<ValueType>(entry.getValue()));
			} else {
				continue;
			}
		}
		for (Set<ValueType> values : potentialValues) {
			if (!allowEmpty && values.isEmpty()) {
				throw new RuntimeException(
						"One of the node have no known potential value. We cannot compute its possible values.");
			} else {
				iterators.add(values.iterator());
			}
		}
	}

	public BigInteger getAmountOfPossibleVectors() {
		BigInteger max = new BigInteger("1");
		for (Set<?> values : potentialValues) {
			max = max.multiply(new BigInteger("" + values.size()));
		}
		return max;
	}

	public BigInteger getAmountOFGeneratedVectors() {
		return count;
	}

	public boolean isPossibleVector(List<Object> vector) {
		for (int i = 0; i < potentialValues.size(); i++) {
			if (potentialValues.get(i).contains(vector.get(i))) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean hasNext() {
		if (getAmountOfPossibleVectors().compareTo(BigInteger.ZERO) > 0) {
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
	public List<ValueType> next() {
		if (lastValues.isEmpty()) {
			for (Iterator<ValueType> iterator : iterators) {
				lastValues.add(iterator.next());
			}
		} else {
			int index = lastValues.size() - 1;
			setLastValueFor(index);
		}
		count = count.add(new BigInteger("1"));
		return lastValues;
	}

	private void setLastValueFor(int index) {
		Iterator<ValueType> iterator = iterators.get(index);
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

		VectorsIterator<Integer> iterator = new VectorsIterator<Integer>(
				nodePossibleValues, nodeObservedValues, nodesConsidered);
		while (iterator.hasNext()) {
			List<Integer> values = iterator.next();
			System.out.println(values);
		}
	}
}
