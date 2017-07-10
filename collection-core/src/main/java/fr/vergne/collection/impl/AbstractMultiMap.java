package fr.vergne.collection.impl;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import fr.vergne.collection.MultiMap;

/**
 * An {@link AbstractMultiMap} is a {@link MultiMap} which implements most of
 * the features. It is basically a {@link HashMap}, so a given key appears only
 * once. However, the type of collection used for values is not provided, which
 * leads to a method to implement in the classes which extend this
 * {@link AbstractMultiMap}.
 * 
 * @author Matthieu Vergne <vergne@fbk.eu>
 * 
 * @param <Key>
 * @param <Value>
 * @deprecated The code now has its dedicated project at: https://github.com/matthieu-vergne/multi-map
 */
public abstract class AbstractMultiMap<Key, Value> implements
		MultiMap<Key, Value> {

	private final HashMap<Key, Collection<Value>> innerMap = new HashMap<Key, Collection<Value>>();

	public AbstractMultiMap(MultiMap<Key, Value> map) {
		for (Entry<Key, Collection<Value>> entry : map.entrySet()) {
			addAll(entry.getKey(), entry.getValue());
		}
	}

	public AbstractMultiMap() {
	}

	@Override
	public boolean add(Key key, Value value) {
		Collection<Value> set = getContainerFor(key);
		return set.add(value);
	}

	@Override
	public boolean addAll(Key key, Collection<Value> values) {
		Collection<Value> set = getContainerFor(key);
		return set.addAll(values);
	}

	@Override
	public boolean remove(Key key, Value value) {
		Collection<Value> set = getContainerFor(key);
		return set.remove(value);
	}

	@Override
	public boolean removeAll(Key key, Collection<Value> values) {
		Collection<Value> set = getContainerFor(key);
		return set.removeAll(values);
	}

	private Collection<Value> getContainerFor(Key key) {
		if (!containsKey(key)) {
			innerMap.put(key, generateInnerCollection(key));
		} else {
			// use the already present collection
		}
		return innerMap.get(key);
	}

	protected abstract Collection<Value> generateInnerCollection(Key key);

	@Override
	public boolean containsCouple(Key key, Value value) {
		return containsKey(key) && innerMap.get(key).contains(value);
	}

	@Override
	public Iterator<Entry<Key, Value>> iterator() {
		return new Iterator<Entry<Key, Value>>() {

			private Iterator<Key> keysIterator = keySet().iterator();
			private Iterator<Value> valuesIterator;
			private Key key;
			private Value value;

			@Override
			public boolean hasNext() {
				return keysIterator.hasNext() || valuesIterator != null
						&& valuesIterator.hasNext();
			}

			@Override
			public Entry<Key, Value> next() {
				while (valuesIterator == null || !valuesIterator.hasNext()) {
					key = keysIterator.next();
					valuesIterator = AbstractMultiMap.this.innerMap.get(key)
							.iterator();
				}
				value = valuesIterator.next();
				return new AbstractMap.SimpleImmutableEntry<Key, Value>(key,
						value);
			}

			@Override
			public void remove() {
				AbstractMultiMap.this.remove(key, value);
			}

		};
	}
	
	@Override
	public Collection<Value> replaceAll(Key key, Collection<Value> collection) {
		Collection<Value> actualCollection = generateInnerCollection(key);
		actualCollection.addAll(collection);
		return innerMap.put(key, actualCollection);
	}

	@Override
	public Collection<Value> getAll(Object key) {
		return innerMap.get(key);
	}

	@Override
	public Set<Key> keySet() {
		return innerMap.keySet();
	}

	@Override
	public Collection<Collection<Value>> collections() {
		return innerMap.values();
	}

	@Override
	public Set<Entry<Key, Collection<Value>>> entrySet() {
		return innerMap.entrySet();
	}

	@Override
	public Collection<Value> remove(Object key) {
		return innerMap.remove(key);
	}

	@Override
	public void clear() {
		innerMap.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return innerMap.containsKey(key);
	}

	@Override
	public boolean containsCollection(Collection<Value> collection) {
		return innerMap.containsValue(collection);
	}

	@Override
	public int size() {
		return innerMap.size();
	}
}
