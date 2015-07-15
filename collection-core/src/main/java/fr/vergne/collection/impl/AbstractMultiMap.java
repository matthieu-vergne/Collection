package fr.vergne.collection.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

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
 */
@SuppressWarnings("serial")
public abstract class AbstractMultiMap<Key, Value> extends
		HashMap<Key, Collection<Value>> implements MultiMap<Key, Value> {

	public AbstractMultiMap(MultiMap<Key, Value> map) {
		for (Entry<Key, Collection<Value>> entry : map.entrySet()) {
			populate(entry.getKey(), entry.getValue());
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
	public final boolean populate(Key key, Value... values) {
		return addAll(key, Arrays.asList(values));
	}
	
	@Override
	public final boolean populate(Key key, Collection<Value> values) {
		return addAll(key, values);
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
	
	@Override
	public final boolean depopulate(Key key, Value... values) {
		return depopulate(key, Arrays.asList(values));
	}

	@Override
	public final boolean depopulate(Key key, Collection<Value> values) {
		Collection<Value> set = getContainerFor(key);
		boolean changed = set.removeAll(values);
		if (set.isEmpty()) {
			remove(key);
		} else {
			// still have values, keep the key
		}
		return changed;
	}

	private Collection<Value> getContainerFor(Key key) {
		if (!containsKey(key)) {
			put(key, generateInnerCollection(key));
		} else {
			// use the already present collection
		}
		return get(key);
	}

	protected abstract Collection<Value> generateInnerCollection(Key key);

	@Override
	public boolean containsCouple(Key key, Value value) {
		return containsKey(key) && get(key).contains(value);
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
				return keysIterator.hasNext() || valuesIterator != null && valuesIterator.hasNext();
			}

			@Override
			public Entry<Key, Value> next() {
				while (valuesIterator == null || !valuesIterator.hasNext()) {
					key = keysIterator.next();
					valuesIterator = AbstractMultiMap.this.get(key).iterator();
				}
				value = valuesIterator.next();
				return new SimpleImmutableEntry<Key, Value>(key, value);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void remove() {
				AbstractMultiMap.this.depopulate(key, value);
			}

		};
	}
}
