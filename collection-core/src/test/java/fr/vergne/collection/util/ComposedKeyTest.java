package fr.vergne.collection.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class ComposedKeyTest {

	@Test
	public void testSize() {
		assertEquals(0,
				new ComposedKey<Integer>(Collections.<Integer> emptyList())
						.size());
		assertEquals(1, new ComposedKey<Integer>(Arrays.asList(1)).size());
		assertEquals(2, new ComposedKey<Integer>(Arrays.asList(1, 2)).size());
		assertEquals(3, new ComposedKey<Integer>(Arrays.asList(1, 2, 3)).size());
		assertEquals(4,
				new ComposedKey<Integer>(Arrays.asList(1, 2, 3, 4)).size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGet() {
		for (List<Integer> list : Arrays.asList(Arrays.asList(1, 2, 3, 4),
				Arrays.asList(3, 2, 1), Arrays.asList(7, 23, 7, 5, 2, 678, 43))) {
			ComposedKey<Integer> key = new ComposedKey<Integer>(list);
			for (int i = 0; i < key.size(); i++) {
				assertEquals(list.get(i), key.get(i));
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testIterator() {
		for (List<Integer> list : Arrays.asList(Arrays.asList(1, 2, 3, 4),
				Arrays.asList(3, 2, 1), Arrays.asList(7, 23, 7, 5, 2, 678, 43))) {
			ComposedKey<Integer> key = new ComposedKey<Integer>(list);
			Iterator<Integer> iterator = key.iterator();
			Iterator<Integer> iteratorRef = list.iterator();
			while (iteratorRef.hasNext()) {
				assertTrue(iterator.hasNext());
				assertEquals(iteratorRef.next(), iterator.next());
			}
		}
	}

	@Test
	public void testEquals() {
		assertEquals(new ComposedKey<Integer>(new LinkedList<Integer>()),
				new ComposedKey<Integer>(Collections.<Integer> emptyList()));
		assertEquals(new ComposedKey<Integer>(Arrays.asList(1)),
				new ComposedKey<Integer>(Arrays.asList(1)));
		assertEquals(new ComposedKey<Integer>(Arrays.asList(1, 2)),
				new ComposedKey<Integer>(Arrays.asList(1, 2)));
		assertEquals(new ComposedKey<Integer>(Arrays.asList(1, 2, 3)),
				new ComposedKey<Integer>(Arrays.asList(1, 2, 3)));
		assertEquals(new ComposedKey<Integer>(Arrays.asList(1, 2, 3, 4)),
				new ComposedKey<Integer>(Arrays.asList(1, 2, 3, 4)));

		assertFalse(new ComposedKey<Integer>(Arrays.asList(1))
				.equals(new ComposedKey<Integer>(Collections
						.<Integer> emptyList())));
		assertFalse(new ComposedKey<Integer>(Arrays.asList(1, 2, 3))
				.equals(new ComposedKey<Integer>(Arrays.asList(1, 2, 3, 4))));
	}

	@Test
	public void testHashcode() {
		assertEquals(
				new ComposedKey<Integer>(new LinkedList<Integer>()).hashCode(),
				new ComposedKey<Integer>(Collections.<Integer> emptyList())
						.hashCode());
		assertEquals(new ComposedKey<Integer>(Arrays.asList(1)).hashCode(),
				new ComposedKey<Integer>(Arrays.asList(1)).hashCode());
		assertEquals(new ComposedKey<Integer>(Arrays.asList(1, 2)).hashCode(),
				new ComposedKey<Integer>(Arrays.asList(1, 2)).hashCode());
		assertEquals(
				new ComposedKey<Integer>(Arrays.asList(1, 2, 3)).hashCode(),
				new ComposedKey<Integer>(Arrays.asList(1, 2, 3)).hashCode());
		assertEquals(
				new ComposedKey<Integer>(Arrays.asList(1, 2, 3, 4)).hashCode(),
				new ComposedKey<Integer>(Arrays.asList(1, 2, 3, 4)).hashCode());
		// no false cases: hashcode need "equals => same hash" only
	}

}
