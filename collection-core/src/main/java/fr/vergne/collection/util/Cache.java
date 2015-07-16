package fr.vergne.collection.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link Cache} aims at storing heterogeneous values into a map-like
 * structure. At the opposite of using a naive {@link Map} of {@link Object}s,
 * this {@link Cache} uses {@link CacheKey}s only for keys, which are typed keys
 * (through generics). Thus, a type checking is made when calling
 * {@link #put(CacheKey, Object)} and retrieving a value can be done directly
 * with the right type. Because we use generics, the type erasure of Java makes
 * it uneffective at runtime, so strictly equivalent to a {@link Map} of
 * {@link Object}s, but the coding phase (where generics apply) is simplified.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 */
public class Cache {

	/**
	 * A {@link CacheKey} is a special key to use with the {@link Cache}
	 * structure. It is associated to a type which tells which kind of data is
	 * stored with this key.
	 * 
	 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
	 * 
	 * @param <T>
	 *            the type of data stored with this {@link CacheKey}
	 */
	public static class CacheKey<T> {

		private final String string;

		/**
		 * Create a {@link CacheKey} with a given name. This name will be used
		 * by {@link #toString()}, but it has no influence on the object itself.
		 * In particular, two {@link CacheKey} having the same name are
		 * <b>not</b> equals.
		 * 
		 * @param name
		 *            the name of this {@link CacheKey}
		 */
		public CacheKey(String name) {
			this.string = name;
		}

		public CacheKey() {
			this(null);
		}

		@Override
		public String toString() {
			if (string == null) {
				return super.toString();
			} else {
				return string;
			}
		}
	}

	private final Map<CacheKey<?>, Object> map = new HashMap<CacheKey<?>, Object>();
	private final Map<CacheKey<?>, Object> defaultMap = new HashMap<CacheKey<?>, Object>();

	/**
	 * Map a value to a given {@link CacheKey}.
	 * 
	 * @param key
	 *            the {@link CacheKey} to map
	 * @param value
	 *            the value to map to this {@link CacheKey}
	 */
	public synchronized <T> void put(CacheKey<T> key, T value) {
		map.put(key, value);
	}

	public synchronized void remove(CacheKey<?> key) {
		map.remove(key);
	}

	/**
	 * Retrieve a value mapped to a given {@link CacheKey}. If no value is
	 * mapped to the {@link CacheKey}, <code>null</code> will be returned.
	 * 
	 * @param key
	 *            the {@link CacheKey} to retrieve
	 * @return the value mapped to this {@link CacheKey}
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T> T get(CacheKey<T> key) {
		return (T) map.get(key);
	}

	/**
	 * Clear the {@link Cache} of any {@link CacheKey} and their values.
	 */
	public synchronized void clear() {
		map.clear();
	}

	/**
	 * This method save the current state of the {@link Cache} as a default
	 * which can be retrieved by calling {@link #ResetToDefault()}.
	 */
	public synchronized void setAsDefault() {
		defaultMap.clear();
		defaultMap.putAll(map);
	}

	/**
	 * Reset this {@link Cache} to its default, which corresponds to the last
	 * call of {@link #setAsDefault()}. If it was never called, the default
	 * state is an empty {@link Cache}, and calling {@link #ResetToDefault()} is
	 * equivalent to calling {@link #clear()}.
	 */
	public synchronized void ResetToDefault() {
		map.clear();
		map.putAll(defaultMap);
	}

	/**
	 * 
	 * @return the number of {@link CacheKey} mapped by this {@link Cache}
	 */
	public synchronized int size() {
		return map.size();
	}

	/**
	 * 
	 * @return <code>true</code> if no {@link CacheKey} is mapped to a value,
	 *         <code>false</code> otherwise
	 */
	public synchronized boolean isEmpty() {
		return map.isEmpty();
	}

	/**
	 * This method is a facility to use the internal {@link Map} of this
	 * {@link Cache} for further interactions. However, it is a read-only map.
	 * To modify the content of this {@link Cache}, you need to use its own
	 * methods.
	 * 
	 * @return a read-only {@link Map} of this {@link Cache}
	 */
	public synchronized Map<CacheKey<?>, Object> toMap() {
		return Collections.unmodifiableMap(map);
	}

	@Override
	public synchronized String toString() {
		return map.toString();
	}
}
