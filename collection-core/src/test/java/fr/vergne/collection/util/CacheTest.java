package fr.vergne.collection.util;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.vergne.collection.util.Cache.CacheKey;

public class CacheTest {

	@Test
	public void testCacheStartsEmpty() {
		Cache cache = new Cache();

		assertEquals(0, cache.size());
		assertTrue(cache.isEmpty());
	}

	@Test
	public void testPutValueAreCorrectlyGet() {
		Cache cache = new Cache();

		CacheKey<String> key1 = new CacheKey<String>();
		cache.put(key1, "test");
		assertEquals("test", cache.get(key1));
	}

	@Test
	public void testNewKeysProperlyIncreaseSize() {
		Cache cache = new Cache();

		CacheKey<String> key1 = new CacheKey<String>();
		cache.put(key1, "test");
		assertEquals(1, cache.size());

		CacheKey<Integer> key2 = new CacheKey<Integer>();
		cache.put(key2, 3);
		assertEquals(2, cache.size());

		CacheKey<Boolean> key3 = new CacheKey<Boolean>();
		cache.put(key3, true);
		assertEquals(3, cache.size());
	}

	@Test
	public void testSameKeysDoNotIncreaseSize() {
		Cache cache = new Cache();

		CacheKey<String> key1 = new CacheKey<String>();
		cache.put(key1, "test");
		CacheKey<Integer> key2 = new CacheKey<Integer>();
		cache.put(key2, 3);
		CacheKey<Boolean> key3 = new CacheKey<Boolean>();
		cache.put(key3, true);

		cache.put(key1, "aze");
		assertEquals(3, cache.size());
		cache.put(key2, 58);
		assertEquals(3, cache.size());
		cache.put(key3, false);
		assertEquals(3, cache.size());
	}

	@Test
	public void testRemoveProperlyDecreasesSize() {
		Cache cache = new Cache();

		CacheKey<String> key1 = new CacheKey<String>();
		cache.put(key1, "test");
		CacheKey<Integer> key2 = new CacheKey<Integer>();
		cache.put(key2, 3);
		CacheKey<Boolean> key3 = new CacheKey<Boolean>();
		cache.put(key3, true);

		cache.remove(key2);
		assertEquals(2, cache.size());
		cache.remove(key3);
		assertEquals(1, cache.size());
		cache.remove(key1);
		assertEquals(0, cache.size());
	}

	@Test
	public void testIsEmptyOnlyWhenZeroSize() {
		Cache cache = new Cache();

		assertTrue(cache.isEmpty());

		CacheKey<String> key1 = new CacheKey<String>();
		cache.put(key1, "test");
		assertFalse(cache.isEmpty());

		CacheKey<Integer> key2 = new CacheKey<Integer>();
		cache.put(key2, 3);
		assertFalse(cache.isEmpty());

		CacheKey<Boolean> key3 = new CacheKey<Boolean>();
		cache.put(key3, true);
		assertFalse(cache.isEmpty());

		cache.remove(key2);
		assertFalse(cache.isEmpty());

		cache.remove(key3);
		assertFalse(cache.isEmpty());

		cache.remove(key1);
		assertTrue(cache.isEmpty());
	}

	@Test
	public void testResetProperlyAppliesDefault() {
		Cache cache = new Cache();

		CacheKey<String> key1 = new CacheKey<String>();
		cache.put(key1, "test");
		CacheKey<Integer> key2 = new CacheKey<Integer>();
		cache.put(key2, 3);
		cache.setAsDefault();

		cache.put(key1, "aze");
		cache.put(key2, 95);
		CacheKey<Boolean> key3 = new CacheKey<Boolean>();
		cache.put(key3, true);

		cache.ResetToDefault();
		assertEquals("test", cache.get(key1));
		assertEquals((Integer) 3, cache.get(key2));
		assertEquals(null, cache.get(key3));
	}

	@Test
	public void testResetWithoutDefaultEquivalentToClear() {
		Cache cache = new Cache();

		CacheKey<String> key1 = new CacheKey<String>();
		cache.put(key1, "test");
		CacheKey<Integer> key2 = new CacheKey<Integer>();
		cache.put(key2, 3);

		cache.ResetToDefault();
		assertTrue(cache.isEmpty());
	}

	@Test
	public void testClearRemovesAll() {
		Cache cache = new Cache();

		CacheKey<String> key1 = new CacheKey<String>();
		cache.put(key1, "test");
		CacheKey<Integer> key2 = new CacheKey<Integer>();
		cache.put(key2, 3);
		CacheKey<Boolean> key3 = new CacheKey<Boolean>();
		cache.put(key3, true);

		cache.clear();
		assertEquals(null, cache.get(key1));
		assertEquals(null, cache.get(key2));
		assertEquals(null, cache.get(key3));
		assertEquals(0, cache.size());
		assertTrue(cache.isEmpty());
	}

	@Test
	public void testClearRemovesAllButKeepDefault() {
		Cache cache = new Cache();

		CacheKey<String> key1 = new CacheKey<String>();
		cache.put(key1, "test");
		CacheKey<Integer> key2 = new CacheKey<Integer>();
		cache.put(key2, 3);
		cache.setAsDefault();

		cache.clear();
		assertEquals(0, cache.size());
		assertTrue(cache.isEmpty());

		cache.ResetToDefault();
		assertEquals(2, cache.size());
		assertFalse(cache.isEmpty());
		assertEquals("test", cache.get(key1));
		assertEquals((Integer) 3, cache.get(key2));
	}

	@Test
	public void testPutNewValueReturnsOldValue() {
		Cache cache = new Cache();

		CacheKey<String> key1 = new CacheKey<String>();
		assertEquals(null, cache.put(key1, "test"));
		assertEquals("test", cache.put(key1, "test"));
		assertEquals("test", cache.put(key1, "aze"));
		assertEquals("aze", cache.put(key1, null));
		assertEquals(null, cache.put(key1, null));
	}

	@Test
	public void testRemoveReturnsValue() {
		Cache cache = new Cache();

		CacheKey<String> key1 = new CacheKey<String>();
		cache.put(key1, "test");
		assertEquals("test", cache.remove(key1));
	}
}
