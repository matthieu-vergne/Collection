package fr.vergne.collection.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ListUtilsTest {

	@Test
	public void testEquals() {
		assertTrue(ListUtils.equals(Arrays.asList(), Arrays.asList()));
		assertTrue(ListUtils.equals(Arrays.asList(1), Arrays.asList(1)));
		assertTrue(ListUtils.equals(Arrays.asList(1, 2), Arrays.asList(1, 2)));
		assertTrue(ListUtils.equals(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3)));
		
		assertFalse(ListUtils.equals(Arrays.asList(1, 2, 3), Arrays.asList(3, 2, 1)));
		assertFalse(ListUtils.equals(Arrays.asList(1, 2, 3), Arrays.asList(1, 3, 2)));
		assertFalse(ListUtils.equals(Arrays.asList(1, 2, 3), Arrays.asList(3, 1, 2)));
		
		assertFalse(ListUtils.equals(Arrays.asList(1), Arrays.asList(1, 2, 3)));
		
		assertFalse(ListUtils.equals(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6)));
		
		assertFalse(ListUtils.equals(Arrays.<Integer>asList(), Arrays.asList(1, 2, 3)));
	}

}
