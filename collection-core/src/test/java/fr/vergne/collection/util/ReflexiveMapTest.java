package fr.vergne.collection.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReflexiveMapTest {

	@Test
	public void test() {
		ReflexiveMap<String, Integer> map = new ReflexiveMap<String, Integer>();
		assertEquals(0, map.size());

		map.put("1", 1);
		map.put("2", 2);
		map.put("3", 3);

		assertEquals(3, map.size());
		assertEquals((Integer) 1, map.get("1"));
		assertEquals((Integer) 2, map.get("2"));
		assertEquals((Integer) 3, map.get("3"));
		assertEquals(null, map.get("4"));

		assertEquals((Integer) 1, map.getValueFrom("1"));
		assertEquals((Integer) 2, map.getValueFrom("2"));
		assertEquals((Integer) 3, map.getValueFrom("3"));
		assertEquals(null, map.getValueFrom("4"));

		assertEquals("1", map.getKeyFrom(1));
		assertEquals("2", map.getKeyFrom(2));
		assertEquals("3", map.getKeyFrom(3));
		assertEquals(null, map.getKeyFrom(4));

		map.remove("2");

		assertEquals((Integer) 1, map.getValueFrom("1"));
		assertEquals(null, map.getValueFrom("2"));
		assertEquals((Integer) 3, map.getValueFrom("3"));
		assertEquals(null, map.getValueFrom("4"));

		assertEquals("1", map.getKeyFrom(1));
		assertEquals(null, map.getKeyFrom(2));
		assertEquals("3", map.getKeyFrom(3));
		assertEquals(null, map.getKeyFrom(4));

		ReflexiveMap<Integer, String> reverse = map.reverse();

		assertEquals((Integer) 1, reverse.getKeyFrom("1"));
		assertEquals(null, reverse.getKeyFrom("2"));
		assertEquals((Integer) 3, reverse.getKeyFrom("3"));
		assertEquals(null, reverse.getKeyFrom("4"));

		assertEquals("1", reverse.getValueFrom(1));
		assertEquals(null, reverse.getValueFrom(2));
		assertEquals("3", reverse.getValueFrom(3));
		assertEquals(null, reverse.getValueFrom(4));

		reverse.put(2, "2");

		assertEquals((Integer) 1, reverse.getKeyFrom("1"));
		assertEquals((Integer) 2, reverse.getKeyFrom("2"));
		assertEquals((Integer) 3, reverse.getKeyFrom("3"));
		assertEquals(null, reverse.getKeyFrom("4"));

		assertEquals("1", reverse.getValueFrom(1));
		assertEquals("2", reverse.getValueFrom(2));
		assertEquals("3", reverse.getValueFrom(3));
		assertEquals(null, reverse.getValueFrom(4));

		assertEquals((Integer) 1, map.getValueFrom("1"));
		assertEquals((Integer) 2, map.getValueFrom("2"));
		assertEquals((Integer) 3, map.getValueFrom("3"));
		assertEquals(null, map.getValueFrom("4"));

		assertEquals("1", map.getKeyFrom(1));
		assertEquals("2", map.getKeyFrom(2));
		assertEquals("3", map.getKeyFrom(3));
		assertEquals(null, map.getKeyFrom(4));

		assertEquals(reverse, map.reverse());
		assertEquals(map, reverse.reverse());
	}

}
