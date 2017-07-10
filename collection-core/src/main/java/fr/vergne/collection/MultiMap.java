package fr.vergne.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <p>
 * A {@link MultiMap} allows to map a key to several values, as opposed to a
 * {@link Map} which maps a key to a single value. Functionally speaking, a
 * {@link MultiMap} is equivalent to a {@link Map} which maps keys to
 * {@link Collection}s, but with additional features to interact directly with
 * the values within these {@link Collection}s. Unfortunately, since Java 1.8,
 * the method {@link Map#remove(Object, Object)} has been introduced, which
 * clashed with our {@link #remove(Object, Object)} method. Thus, we cannot
 * extends {@link Map} anymore and the user should use {@link #toMap()} instead.
 * </p>
 * 
 * <p>
 * This class is similar to the <a src=
 * "http://commons.apache.org/proper/commons-collections/javadocs/api-3.2.1/org/apache/commons/collections/MultiMap.html"
 * >Apache MultiMap</a>, but assumes that it is a proper {@link Map} between a
 * key and a <u>set of</u> values rather than a special {@link Map} between a
 * key and each of its <u>single</u> values. Thus, where the Apache one appears
 * as a common {@link Map} but with the need to make casting and other tricky
 * things to use it fully, this {@link MultiMap} implements as much as possible
 * the concept to provide a user-friendly class, without breaking the contract
 * of a {@link Map} which explicitly mentions that
 * "each key can map to at most one value" (see {@link Map} documentation).
 * </p>
 * 
 * @author Matthieu Vergne <vergne@fbk.eu>
 * 
 * @param <Key>
 * @param <Value>
 * @deprecated The code now has its dedicated project at: https://github.com/matthieu-vergne/multi-map
 */
public interface MultiMap<Key, Value> extends Iterable<Entry<Key, Value>> {
	/**
	 * Map a key to the provided value. If there is already values mapped to
	 * this key, the new value is added (the old ones are kept, not replaced).
	 * 
	 * @param key
	 *            the key to map
	 * @param value
	 *            the value to map to that key
	 * @return <code>true</code> if the mapping has been changed,
	 *         <code>false</code> otherwise
	 */
	public boolean add(Key key, Value value);

	/**
	 * Map a key to all the provided values. If there is already values mapped
	 * to this key, the new values are added (the old ones are kept, not
	 * replaced).
	 * 
	 * @param key
	 *            the key to map
	 * @param values
	 *            the values to map to that key
	 * @return <code>true</code> if the mapping has been changed,
	 *         <code>false</code> otherwise
	 */
	public boolean addAll(Key key, Collection<Value> values);

	/**
	 * Add set of values to their corresponding keys. All the values already
	 * mapped to these keys are kept, and the new ones are added.
	 * 
	 * @param map
	 *            the various values to map to their various keys
	 */
	default void addAll(Map<? extends Key, ? extends Collection<Value>> map) {
		for (Entry<? extends Key, ? extends Collection<Value>> entry : map
				.entrySet()) {
			addAll(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Remove all the current values mapped to a given key and replace them by
	 * the ones provided in argument.
	 * 
	 * @param key
	 *            the key to change
	 * @param collection
	 *            the new {@link Collection} of values to map to the key
	 * @return the previous {@link Collection} of values mapped to the key,
	 *         <code>null</code> if the key was not mapped to any value
	 */
	public Collection<Value> replaceAll(Key key, Collection<Value> collection);

	/**
	 * Map {@link Collection}s of values to their corresponding keys. All the
	 * values already mapped to these keys are forgotten.
	 * 
	 * @param map
	 *            the various values to map to their various keys
	 */
	default void replaceAll(Map<? extends Key, ? extends Collection<Value>> map) {
		for (Entry<? extends Key, ? extends Collection<Value>> entry : map
				.entrySet()) {
			replaceAll(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * 
	 * @param key
	 *            the key to retrieve
	 * @return all the values mapped to this key
	 */
	public Collection<Value> getAll(Object key);

	/**
	 * 
	 * @return all the keys stored in this {@link MultiMap}
	 */
	public Set<Key> keySet();

	/**
	 * 
	 * @return all the {@link Collection}s stored in this {@link MultiMap}
	 */
	public Collection<Collection<Value>> collections();

	/**
	 * 
	 * @return all the mappings stored in this {@link MultiMap}
	 */
	public Set<Entry<Key, Collection<Value>>> entrySet();

	/**
	 * Unmap a specific value from a key.
	 * 
	 * @param key
	 *            the key to map
	 * @param value
	 *            the value to remove from that key
	 * @return <code>true</code> if the mapping has been changed,
	 *         <code>false</code> otherwise
	 */
	public boolean remove(Key key, Value value);

	/**
	 * Unmap all the values mapped to a given key.
	 * 
	 * @param key
	 *            the key to remove
	 * @return the values previously mapped to this key, <code>null</code> if
	 *         the key was not mapped to any value.
	 */
	public Collection<Value> remove(Object key);

	/**
	 * Unmap a collection of values from a key.
	 * 
	 * @param key
	 *            the key to map
	 * @param values
	 *            the values to remove from that key
	 * @return <code>true</code> if the mapping has been changed,
	 *         <code>false</code> otherwise
	 */
	public boolean removeAll(Key key, Collection<Value> values);

	/**
	 * Remove all the keys of this {@link MultiMap}
	 */
	public void clear();

	/**
	 * 
	 * @param key
	 *            the key to check
	 * @return <code>true</code> if the key is mapped to some values,
	 *         <code>false</code> otherwise
	 */
	public boolean containsKey(Object key);

	/**
	 * 
	 * @param key
	 *            the key to check
	 * @param value
	 *            the value to check
	 * @return <code>true</code> if the key is known and the value is actually
	 *         mapped to it
	 */
	public boolean containsCouple(Key key, Value value);

	/**
	 * 
	 * @param collection
	 *            the {@link Collection} to check
	 * @return <code>true</code> if a key is mapped to an equal
	 *         {@link Collection}, <code>false</code> otherwise
	 */
	public boolean containsCollection(Collection<Value> collection);

	/**
	 * 
	 * @return the number of keys of this {@link MultiMap}
	 */
	public int size();

	/**
	 * 
	 * @return <code>true</code> if no key is stored in this {@link MultiMap},
	 *         <code>false</code> otherwise
	 */
	default boolean isEmpty() {
		return !iterator().hasNext();
	}

	default Map<Key, Collection<Value>> toMap() {
		return new Map<Key, Collection<Value>>() {

			@Override
			public int size() {
				return MultiMap.this.size();
			}

			@Override
			public boolean isEmpty() {
				return MultiMap.this.isEmpty();
			}

			@Override
			public boolean containsKey(Object key) {
				return MultiMap.this.containsKey(key);
			}

			@SuppressWarnings("unchecked")
			@Override
			public boolean containsValue(Object value) {
				if (value instanceof Collection) {
					return MultiMap.this
							.containsCollection((Collection<Value>) value);
				} else {
					return false;
				}
			}

			@Override
			public Collection<Value> get(Object key) {
				return MultiMap.this.getAll(key);
			}

			@Override
			public Collection<Value> put(Key key, Collection<Value> collection) {
				return MultiMap.this.replaceAll(key, collection);
			}

			@Override
			public Collection<Value> remove(Object key) {
				return MultiMap.this.remove(key);
			}

			@Override
			public void putAll(
					Map<? extends Key, ? extends Collection<Value>> map) {
				MultiMap.this.replaceAll(map);
			}

			@Override
			public void clear() {
				MultiMap.this.clear();
			}

			@Override
			public Set<Key> keySet() {
				return MultiMap.this.keySet();
			}

			@Override
			public Collection<Collection<Value>> values() {
				return MultiMap.this.collections();
			}

			@Override
			public Set<Entry<Key, Collection<Value>>> entrySet() {
				return MultiMap.this.entrySet();
			}
		};
	}
}
