package fr.vergne.collection.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.junit.Test;

public class SetIteratorTest {

	@Test
	public void test4Values() {
		SetIterator<Integer> iterator = new SetIterator<Integer>(
				set(1, 2, 3, 4));
		Collection<Set<Integer>> sets = new LinkedList<Set<Integer>>();
		while (iterator.hasNext()) {
			sets.add(iterator.next());
		}

		assertTrue(find(sets, Collections.<Integer> emptySet()));

		assertTrue(find(sets, set(1)));
		assertTrue(find(sets, set(2)));
		assertTrue(find(sets, set(3)));
		assertTrue(find(sets, set(4)));

		assertTrue(find(sets, set(1, 2)));
		assertTrue(find(sets, set(1, 3)));
		assertTrue(find(sets, set(1, 4)));
		assertTrue(find(sets, set(2, 3)));
		assertTrue(find(sets, set(2, 4)));
		assertTrue(find(sets, set(3, 4)));

		assertTrue(find(sets, set(1, 2, 3)));
		assertTrue(find(sets, set(1, 2, 4)));
		assertTrue(find(sets, set(1, 3, 4)));
		assertTrue(find(sets, set(2, 3, 4)));

		assertTrue(find(sets, set(1, 2, 3, 4)));

		assertFalse(iterator.hasNext());
	}

	private <T> boolean find(Collection<Set<T>> container, Set<T> set) {
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

	@Test
	public void testAmountOfPossibleSets() {
		assertEquals(2, new SetIterator<Integer>(set(1))
				.getAmountOfPossibleSets().intValue());
		assertEquals(4, new SetIterator<Integer>(set(1, 2))
				.getAmountOfPossibleSets().intValue());
		assertEquals(8, new SetIterator<Integer>(set(1, 2, 3))
				.getAmountOfPossibleSets().intValue());
		assertEquals(16, new SetIterator<Integer>(set(1, 2, 3, 4))
				.getAmountOfPossibleSets().intValue());
	}

	@Test
	public void testPossibleSetsGenerationAndEvaluation() {
		for (Set<Integer> set : Arrays.asList(set(1), set(1, 2),
				set(1, 2, 3, 4, 5, 6), set(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))) {
			SetIterator<Integer> iterator = new SetIterator<Integer>(set);
			int count = 0;
			while (iterator.hasNext()) {
				iterator.next();
				count++;
			}
			assertEquals(set.toString(), iterator.getAmountOfPossibleSets()
					.intValue(), count);
		}
	}

	@Test
	public void testIsPossibleSet() {
		SetIterator<Integer> iterator = new SetIterator<Integer>(
				set(1, 2, 3, 4));
		assertTrue(iterator.isPossibleSet(set(1)));
		assertTrue(iterator.isPossibleSet(set(2)));
		assertTrue(iterator.isPossibleSet(set(3)));
		assertTrue(iterator.isPossibleSet(set(4)));
		assertTrue(iterator.isPossibleSet(set(1, 2)));
		assertTrue(iterator.isPossibleSet(set(1, 3)));
		assertTrue(iterator.isPossibleSet(set(1, 4)));
		assertTrue(iterator.isPossibleSet(set(2, 3)));
		assertTrue(iterator.isPossibleSet(set(2, 4)));
		assertTrue(iterator.isPossibleSet(set(3, 4)));
		assertTrue(iterator.isPossibleSet(set(1, 2, 3)));
		assertTrue(iterator.isPossibleSet(set(1, 2, 4)));
		assertTrue(iterator.isPossibleSet(set(1, 3, 4)));
		assertTrue(iterator.isPossibleSet(set(2, 3, 4)));
		assertTrue(iterator.isPossibleSet(set(1, 2, 3, 4)));
		assertFalse(iterator.isPossibleSet(set(1, 2, 3, 4, 5)));
		assertFalse(iterator.isPossibleSet(set(1, 2, 3, 5)));
		assertFalse(iterator.isPossibleSet(set(1, 2, 5)));
		assertFalse(iterator.isPossibleSet(set(1, 5)));
		assertFalse(iterator.isPossibleSet(set(5)));
		assertFalse(iterator.isPossibleSet(Collections.<Integer> emptySet()));
	}

	private <T> Set<T> set(T... values) {
		return new HashSet<T>(Arrays.asList(values));
	}
}
