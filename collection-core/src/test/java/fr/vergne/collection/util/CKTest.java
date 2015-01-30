package fr.vergne.collection.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import fr.vergne.collection.util.CK.ComposedKey;
import fr.vergne.collection.util.CK.FK;

public class CKTest {

	@Test
	public void testGetItem() {
		CK<Integer, CK<String, FK<Integer>>> key = new CK<>(1, new CK<>("a",
				new FK<>(2)));
		assertEquals((Integer) 1, key.getItem());
		assertEquals("a", key.getNext().getItem());
		assertEquals((Integer) 2, key.getNext().getNext().getItem());
	}

	@Test
	public void testGetNext() {
		FK<Integer> k3 = new FK<>(2);
		CK<String, FK<Integer>> k2 = new CK<>("a", k3);
		CK<Integer, CK<String, FK<Integer>>> k1 = new CK<>(1, k2);
		assertEquals(k2, k1.getNext());
		assertEquals(k3, k2.getNext());
		assertEquals(null, k3.getNext());
	}

	@Test
	public void testGetItems() {
		CK<Integer, CK<String, FK<Integer>>> key = new CK<>(1, new CK<>("a",
				new FK<>(2)));
		List<Object> items = key.getItems();
		assertEquals((Integer) 1, items.get(0));
		assertEquals("a", items.get(1));
		assertEquals((Integer) 2, items.get(2));
	}

	@Test
	public void testCKEquals() {
		FK<Integer> k1a = new FK<>(1);
		FK<Integer> k1b = new FK<>(1);
		assertEquals(k1a, k1b);

		CK<Integer, FK<Integer>> k2a;
		k2a = new CK<>(1, new FK<>(2));
		CK<Integer, FK<Integer>> k2b;
		k2b = new CK<>(1, new FK<>(2));
		assertEquals(k2a, k2b);

		CK<Integer, CK<Integer, FK<Integer>>> k3a;
		k3a = new CK<>(1, new CK<>(2, new FK<>(3)));
		CK<Integer, CK<Integer, FK<Integer>>> k3b;
		k3b = new CK<>(1, new CK<>(2, new FK<>(3)));
		assertEquals(k3a, k3b);

		CK<Integer, CK<Integer, CK<Integer, FK<Integer>>>> k4a;
		k4a = new CK<>(1, new CK<>(2, new CK<>(3, new FK<>(4))));
		CK<Integer, CK<Integer, CK<Integer, FK<Integer>>>> k4b;
		k4b = new CK<>(1, new CK<>(2, new CK<>(3, new FK<>(4))));
		assertEquals(k4a, k4b);

		assertFalse(k1a.equals(k2a));
		assertFalse(k3a.equals(k4a));
	}

	@Test
	public void testCKHashcode() {
		FK<Integer> k1a = new FK<>(1);
		FK<Integer> k1b = new FK<>(1);
		assertEquals(k1a.hashCode(), k1b.hashCode());

		CK<Integer, FK<Integer>> k2a = new CK<>(1, new FK<>(2));
		CK<Integer, FK<Integer>> k2b = new CK<>(1, new FK<>(2));
		assertEquals(k2a.hashCode(), k2b.hashCode());

		CK<Integer, CK<Integer, FK<Integer>>> k3a = new CK<>(1, new CK<>(2,
				new FK<>(3)));
		CK<Integer, CK<Integer, FK<Integer>>> k3b = new CK<>(1, new CK<>(2,
				new FK<>(3)));
		assertEquals(k3a.hashCode(), k3b.hashCode());

		CK<Integer, CK<Integer, CK<Integer, FK<Integer>>>> k4a = new CK<>(1,
				new CK<>(2, new CK<>(3, new FK<>(4))));
		CK<Integer, CK<Integer, CK<Integer, FK<Integer>>>> k4b = new CK<>(1,
				new CK<>(2, new CK<>(3, new FK<>(4))));
		assertEquals(k4a.hashCode(), k4b.hashCode());
		// no false cases: hashcode need "equals => same hash" only
	}

	@Test
	public void testComposedKeyGet() {
		ComposedKey<Integer> key = new ComposedKey<Integer>(1, 2, 3, 4);
		assertEquals((Integer) 1, key.get(0));
		assertEquals((Integer) 2, key.get(1));
		assertEquals((Integer) 3, key.get(2));
		assertEquals((Integer) 4, key.get(3));
	}

	@Test
	public void testComposedKeyIterator() {
		ComposedKey<Integer> key = new ComposedKey<Integer>(1, 2, 3, 4);
		Iterator<Integer> iterator = key.iterator();
		assertEquals((Integer) 1, iterator.next());
		assertEquals((Integer) 2, iterator.next());
		assertEquals((Integer) 3, iterator.next());
		assertEquals((Integer) 4, iterator.next());
	}

	@Test
	public void testComposedKeySize() {
		assertEquals(1, new ComposedKey<Integer>(1).size());
		assertEquals(2, new ComposedKey<Integer>(1, 2).size());
		assertEquals(3, new ComposedKey<Integer>(1, 2, 3).size());
		assertEquals(4, new ComposedKey<Integer>(1, 2, 3, 4).size());
	}

	@Test
	public void testComposedKeyListInput() {
		assertEquals(new ComposedKey<Integer>(1), new ComposedKey<Integer>(
				Arrays.asList(1)));
		assertEquals(new ComposedKey<Integer>(1, 2), new ComposedKey<Integer>(
				Arrays.asList(1, 2)));
		assertEquals(new ComposedKey<Integer>(1, 2, 3),
				new ComposedKey<Integer>(Arrays.asList(1, 2, 3)));
		assertEquals(new ComposedKey<Integer>(1, 2, 3, 4),
				new ComposedKey<Integer>(Arrays.asList(1, 2, 3, 4)));
	}

	@Test
	public void testComposedKeyEquals() {
		assertEquals(new ComposedKey<Integer>(1), new ComposedKey<Integer>(1));
		assertEquals(new ComposedKey<Integer>(1, 2), new ComposedKey<Integer>(
				1, 2));
		assertEquals(new ComposedKey<Integer>(1, 2, 3),
				new ComposedKey<Integer>(1, 2, 3));
		assertEquals(new ComposedKey<Integer>(1, 2, 3, 4),
				new ComposedKey<Integer>(1, 2, 3, 4));

		assertFalse(new ComposedKey<Integer>(1, 2)
				.equals(new ComposedKey<Integer>(1)));
		assertFalse(new ComposedKey<Integer>(1, 2, 3)
				.equals(new ComposedKey<Integer>(1, 2, 3, 4)));
	}

	@Test
	public void testComposedKeyHashcode() {
		assertEquals(new ComposedKey<Integer>(1).hashCode(),
				new ComposedKey<Integer>(1).hashCode());
		assertEquals(new ComposedKey<Integer>(1, 2).hashCode(),
				new ComposedKey<Integer>(1, 2).hashCode());
		assertEquals(new ComposedKey<Integer>(1, 2, 3).hashCode(),
				new ComposedKey<Integer>(1, 2, 3).hashCode());
		assertEquals(new ComposedKey<Integer>(1, 2, 3, 4).hashCode(),
				new ComposedKey<Integer>(1, 2, 3, 4).hashCode());
		// no false cases: hashcode need "equals => same hash" only
	}

}
