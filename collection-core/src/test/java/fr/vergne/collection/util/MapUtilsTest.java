package fr.vergne.collection.util;

import static org.junit.Assert.*;

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

}
