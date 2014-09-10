package fr.vergne.collection.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import org.junit.Test;

public class SetIteratorTest {

	@Test
	public void test4Values() {
		SetIterator<Integer> iterator = new SetIterator<Integer>(Arrays.asList(
				1, 2, 3, 4));
		Collection<Set<Integer>> sets = new LinkedList<Set<Integer>>();
		int setLimit = 15;
		for (int i = 0; i < setLimit; i++) {
			sets.add(iterator.next());
		}
		assertTrue(find(sets, Arrays.asList(1)));
		assertTrue(find(sets, Arrays.asList(2)));
		assertTrue(find(sets, Arrays.asList(3)));
		assertTrue(find(sets, Arrays.asList(4)));

		assertTrue(find(sets, Arrays.asList(1, 2)));
		assertTrue(find(sets, Arrays.asList(1, 3)));
		assertTrue(find(sets, Arrays.asList(1, 4)));
		assertTrue(find(sets, Arrays.asList(2, 3)));
		assertTrue(find(sets, Arrays.asList(2, 4)));
		assertTrue(find(sets, Arrays.asList(3, 4)));

		assertTrue(find(sets, Arrays.asList(1, 2, 3)));
		assertTrue(find(sets, Arrays.asList(1, 2, 4)));
		assertTrue(find(sets, Arrays.asList(1, 3, 4)));
		assertTrue(find(sets, Arrays.asList(2, 3, 4)));

		assertTrue(find(sets, Arrays.asList(1, 2, 3, 4)));

		assertFalse(iterator.hasNext());
	}

	private <T> boolean find(Collection<Set<T>> container, Collection<T> set) {
		for (Set<T> set2 : container) {
			if (sameContent(set, set2)) {
				return true;
			} else {
				// continue searching
			}
		}
		return false;
	}

	private <T> boolean sameContent(Collection<T> s1, Collection<T> s2) {
		return s1.containsAll(s2) && s2.containsAll(s1);
	}
}
