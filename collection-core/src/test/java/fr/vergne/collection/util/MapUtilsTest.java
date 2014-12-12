package fr.vergne.collection.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapUtilsTest {

	@Test
	public void testReduceToDirectLinksWithoutIntermediaries() {
		Map<String, String> links = new HashMap<String, String>();
		links.put("a", "aa");
		links.put("aa", "aaa");
		links.put("aaa", "aaaa");
		links.put("A", "aa");
		links.put("b", "bb");
		links.put("bb", "bbb");
		links.put("B", "bbb");

		Map<String, String> reducedLinks = MapUtils.reduceToDirectLinks(links,
				false);

		assertEquals("aaaa", reducedLinks.get("a"));
		assertEquals("aaaa", reducedLinks.get("A"));
		assertEquals("bbb", reducedLinks.get("b"));
		assertEquals("bbb", reducedLinks.get("B"));
		assertEquals(reducedLinks.toString(), 4, reducedLinks.size());
	}

	@Test
	public void testReduceToDirectLinksWithIntermediaries() {
		Map<String, String> links = new HashMap<String, String>();
		links.put("a", "aa");
		links.put("aa", "aaa");
		links.put("aaa", "aaaa");
		links.put("A", "aa");
		links.put("b", "bb");
		links.put("bb", "bbb");
		links.put("B", "bbb");

		Map<String, String> reducedLinks = MapUtils.reduceToDirectLinks(links,
				true);

		assertEquals("aaaa", reducedLinks.get("a"));
		assertEquals("aaaa", reducedLinks.get("aa"));
		assertEquals("aaaa", reducedLinks.get("aaa"));
		assertEquals("aaaa", reducedLinks.get("A"));
		assertEquals("bbb", reducedLinks.get("b"));
		assertEquals("bbb", reducedLinks.get("bb"));
		assertEquals("bbb", reducedLinks.get("B"));
		assertEquals(reducedLinks.toString(), links.size(), reducedLinks.size());
	}

	@Test
	public void testReduceToDirectLinksWithLoop() {
		Map<String, String> links = new HashMap<String, String>();
		links.put("a", "aa");
		links.put("aa", "aaa");
		links.put("aaa", "aaaa");
		links.put("aaaa", "aa");
		links.put("A", "aa");
		links.put("b", "bb");
		links.put("bb", "bbb");
		links.put("B", "bbb");

		try {
			MapUtils.reduceToDirectLinks(links, false);
			fail("No exception thrown");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testEquals() {
		Map<String, Integer> mapRef = new HashMap<String, Integer>();
		mapRef.put("a", 1);
		mapRef.put("b", 2);
		mapRef.put("c", 3);
		Map<String, Integer> mapEqual = new HashMap<String, Integer>();
		mapEqual.put("c", 3);
		mapEqual.put("a", 1);
		mapEqual.put("b", 2);
		Map<String, Integer> mapModifiedValue = new HashMap<String, Integer>();
		mapModifiedValue.put("a", 1);
		mapModifiedValue.put("b", 5);
		mapModifiedValue.put("c", 3);
		Map<String, Integer> mapModifiedKey = new HashMap<String, Integer>();
		mapModifiedKey.put("a", 1);
		mapModifiedKey.put("d", 2);
		mapModifiedKey.put("c", 3);
		Map<String, Integer> mapRemoved = new HashMap<String, Integer>();
		mapRemoved.put("a", 1);
		mapRemoved.put("c", 3);
		Map<String, Integer> mapAdded = new HashMap<String, Integer>();
		mapAdded.put("c", 3);
		mapAdded.put("a", 1);
		mapAdded.put("b", 2);
		mapAdded.put("d", 8);

		assertTrue(MapUtils.equals(mapRef, mapEqual));
		assertFalse(MapUtils.equals(mapRef, mapModifiedValue));
		assertFalse(MapUtils.equals(mapRef, mapModifiedKey));
		assertFalse(MapUtils.equals(mapRef, mapRemoved));
		assertFalse(MapUtils.equals(mapRef, mapAdded));
	}

	@Test
	public void testTranslate() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "a");
		map.put(2, "b");
		map.put(3, "c");
		map.put(4, "d");
		map.put(5, "e");

		assertTrue(ListUtils.equals(
				MapUtils.translate(Arrays.<Integer> asList(), map, false),
				Arrays.<String> asList()));
		assertTrue(ListUtils.equals(
				MapUtils.translate(Arrays.asList(1, 3, 5), map, false),
				Arrays.asList("a", "c", "e")));
		
		assertFalse(ListUtils.equals(
				MapUtils.translate(Arrays.asList(1, 2, 3), map, false),
				Arrays.asList("a", "c", "e")));
		assertFalse(ListUtils.equals(
				MapUtils.translate(Arrays.asList(1, 2, 3), map, false),
				Arrays.asList("c", "b", "a")));
		
		assertTrue(ListUtils.equals(
				MapUtils.translate(Arrays.asList(1, 2, 6), map, true),
				Arrays.asList("a", "b", null)));
		try {
			MapUtils.translate(Arrays.asList(1, 2, 6), map, false);
			fail("No exception thrown");
		} catch (IllegalArgumentException e) {
		}
	}
}
