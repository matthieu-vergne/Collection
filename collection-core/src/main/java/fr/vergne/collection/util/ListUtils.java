package fr.vergne.collection.util;

import java.util.Iterator;
import java.util.List;

public class ListUtils {

	private ListUtils() {
		// Utilitary class, should not be instantiated
	}

	/**
	 * This method provides a convenient way to compare two {@link List}s based
	 * on their content. Instead of using the {@link List#equals(Object)}
	 * method, a proper comparison is made by looking at the equivalence of each
	 * item. This equivalence is assessed by having the same item or by having
	 * {@link Object#equals(Object)} returning true. As we compare {@link List}
	 * s, the order of the items is checked too.<br/>
	 * <br/>
	 * Notice that, as we are comparing items' equivalence (not strict
	 * equality), a {@link List} where two different items (a != b) are
	 * equivalent (a.equals(b) == true) is considered as equals to the same list
	 * where we swap these two items.
	 * 
	 * @param list1
	 * @param list2
	 * @return <code>true</code> if both list have equals elements in the same
	 *         order, <code>false</code> otherwise
	 */
	public static <T> boolean equals(List<T> list1, List<T> list2) {
		if (list1.size() != list2.size()) {
			return false;
		} else {
			Iterator<T> iterator1 = list1.iterator();
			Iterator<T> iterator2 = list2.iterator();
			while (iterator1.hasNext()) {
				T a = (T) iterator1.next();
				T b = (T) iterator2.next();
				if (a == b || a != null && a.equals(b)) {
					// no difference found so far
				} else {
					return false;
				}
			}
			return true;
		}
	}
}
