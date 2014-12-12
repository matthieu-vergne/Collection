package fr.vergne.collection.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapUtils {

	private MapUtils() {
		// utilitary class, only static methods
	}

	/**
	 * This method allows to transform a set of chains A-...-Z to a set of
	 * direct links A-Z. The assumption is that each (key, value) entry of the
	 * {@link Map} gives a direct link, like (A, B), (B, D), (C, D), etc. By
	 * aggregating the links we are able to rebuild the chains and find the last
	 * elements of these chains. This method produces a {@link Map} in which all
	 * the values correspond to the final element of the chain that the key is
	 * part of.
	 * 
	 * @param links
	 *            the links to reduce
	 * @param keepIntermediaries
	 *            <code>true</code> if all the keys should be preserved,
	 *            <code>false</code> if only the keys which are not used as
	 *            values should be kept
	 * @return the direct links to the final elements of the chains
	 * @throws IllegalArgumentException
	 *             a loop is present at the end of a chain (we cannot have it
	 *             elsewhere with a map)
	 */
	public static <T> Map<T, T> reduceToDirectLinks(Map<T, T> links,
			boolean keepIntermediaries) {
		Collection<T> sources = new HashSet<T>(links.keySet());
		Collection<T> targets = new HashSet<T>(links.values());
		List<T> intermediaries = new LinkedList<T>(sources);
		intermediaries.retainAll(targets);
		sources.removeAll(intermediaries);
		targets.removeAll(intermediaries);

		Map<T, T> reducedLinks = new HashMap<T, T>();
		Map<T, T> intermediaryLinks = keepIntermediaries ? reducedLinks
				: new HashMap<T, T>();
		for (T source : sources) {
			T target = links.get(source);
			T expected = intermediaryLinks.get(target);
			if (expected != null) {
				reducedLinks.put(source, expected);
			} else {
				LinkedList<T> sourceIntermediaries = new LinkedList<T>();
				while (!targets.contains(target)) {
					sourceIntermediaries.add(target);
					target = links.get(target);
					if (sourceIntermediaries.contains(target)) {
						while (sourceIntermediaries.getFirst() != target) {
							sourceIntermediaries.removeFirst();
						}
						throw new IllegalArgumentException(
								"There is a loop at the end of the chains containing "
										+ sourceIntermediaries);
					} else {
						// continue searching
					}
				}
				for (T intermediary : sourceIntermediaries) {
					intermediaryLinks.put(intermediary, target);
				}
				reducedLinks.put(source, target);
			}
		}

		return reducedLinks;
	}

	/**
	 * This method allows to check whether or not two maps have the same content
	 * (same keys with same values).
	 * 
	 * @param map1
	 * @param map2
	 * @return <code>true</code> if they are equals, <code>false</code>
	 *         otherwise
	 */
	public static <K, V> boolean equals(Map<K, V> map1, Map<K, V> map2) {
		Set<K> keys1 = map1.keySet();
		Set<K> keys2 = map2.keySet();
		if (!keys1.containsAll(keys2) || !keys2.containsAll(keys1)) {
			return false;
		} else {
			for (K key : keys1) {
				if (map1.get(key).equals(map2.get(key))) {
					// not found a difference yet
				} else {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * While {@link Map#get(Object)} provide the single value assigned to a
	 * single key, this method aims at retrieving a {@link List} of values
	 * corresponding to a {@link List} of keys.
	 * 
	 * @param keys
	 *            the keys to consider
	 * @param map
	 *            the translation map
	 * @param isMissingKeyAllowed
	 *            <code>true</code> to map an unknown key to a <code>null</code>
	 *            value, <code>false</code> to throw an exception
	 * @return the values corresponding to the keys
	 * @throws IllegalArgumentException
	 *             if a requested key is not found in the map and non-mapped
	 *             keys are not allowed
	 */
	public static <K, V> List<V> translate(List<K> keys, Map<K, V> map,
			boolean isMissingKeyAllowed) {
		List<V> values = new LinkedList<V>();
		for (K key : keys) {
			if (!isMissingKeyAllowed && !map.containsKey(key)) {
				throw new IllegalArgumentException("Key not found: " + key);
			} else {
				values.add(map.get(key));
			}
		}
		return values;
	}
}
