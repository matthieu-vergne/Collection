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
	 */
	public static <T> Map<T, T> reduceToDirectLinks(Map<T, T> links,
			boolean keepIntermediaries) {
		System.out.println("Links: " + links);
		Collection<T> sources = new HashSet<T>(links.keySet());
		Collection<T> targets = new HashSet<T>(links.values());
		List<T> intermediaries = new LinkedList<T>(sources);
		intermediaries.retainAll(targets);
		sources.removeAll(intermediaries);
		targets.removeAll(intermediaries);
		System.out.println("Sources: " + sources);
		System.out.println("Targets: " + targets);
		System.out.println("Intermediaries: " + intermediaries);

		Map<T, T> reducedLinks = new HashMap<T, T>();
		Map<T, T> intermediaryLinks = keepIntermediaries ? reducedLinks
				: new HashMap<T, T>();
		for (T source : sources) {
			T target = links.get(source);
			System.out.println("What about " + source + " -> " + target + "?");
			T expected = intermediaryLinks.get(target);
			if (expected != null) {
				System.out
						.println("Already know " + target + " -> " + expected);
				reducedLinks.put(source, expected);
				System.out.println("Set " + source + " -> " + expected);
			} else {
				Collection<T> sourceIntermediaries = new LinkedList<T>();
				while (!targets.contains(target)) {
					System.out.println("Find intermediary " + target);
					sourceIntermediaries.add(target);
					target = links.get(target);
				}
				System.out.println("Find target " + target);
				for (T intermediary : sourceIntermediaries) {
					System.out.println("Learn " + intermediary + " -> "
							+ target);
					intermediaryLinks.put(intermediary, target);
				}
				reducedLinks.put(source, target);
				System.out.println("Set " + source + " -> " + target);
			}
			System.out.println("Finally " + reducedLinks);
		}

		return reducedLinks;
	}
}
