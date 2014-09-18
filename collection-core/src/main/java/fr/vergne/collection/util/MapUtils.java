package fr.vergne.collection.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
}
