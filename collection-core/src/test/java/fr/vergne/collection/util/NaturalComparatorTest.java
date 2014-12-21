package fr.vergne.collection.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class NaturalComparatorTest {

	@Test
	public void testStringSortingStrings() {
		NaturalComparator comparator = new NaturalComparator();
		assertTrue(comparator.compare("a1a", "a1a") == 0);

		assertTrue(comparator.compare("a1a", "b1a") < 0);
		assertTrue(comparator.compare("b1a", "a1a") > 0);

		assertTrue(comparator.compare("a1a", "a1b") < 0);
		assertTrue(comparator.compare("a1b", "a1a") > 0);

		assertTrue(comparator.compare("a1a", "a1aa") < 0);
	}

	@Test
	public void testNumberSortingStrings() {
		NaturalComparator comparator = new NaturalComparator();
		assertTrue(comparator.compare("abc1def", "abc1def") == 0);

		assertTrue(comparator.compare("abc1def", "abc2def") < 0);
		assertTrue(comparator.compare("abc2def", "abc1def") > 0);

		assertTrue(comparator.compare("abc1def", "abc10def") < 0);
		assertTrue(comparator.compare("abc10def", "abc1def") > 0);

		assertTrue(comparator.compare("abc2def", "abc10def") < 0);
		assertTrue(comparator.compare("abc10def", "abc2def") > 0);

		assertTrue(comparator.compare("abc2def", "abc1E4def") < 0);
		assertTrue(comparator.compare("abc1E4def", "abc2def") > 0);

		assertTrue(comparator.compare("abc2def", "abc2.4def") < 0);
		assertTrue(comparator.compare("abc2.4def", "abc2def") > 0);

		assertTrue(comparator.compare("abc2.4def", "abc2.5def") < 0);
		assertTrue(comparator.compare("abc2.5def", "abc2.4def") > 0);

		assertTrue(comparator.compare("abc2.5def", "abc2.4E5def") < 0);
		assertTrue(comparator.compare("abc2.4E5def", "abc2.5def") > 0);
	}

	@Test
	public void testReallyBigNumbers() {
		NaturalComparator comparator = new NaturalComparator();
		assertTrue(comparator.compare("abc123456789123456789123456788def",
				"abc123456789123456789123456789def") < 0);
		assertTrue(comparator.compare("abc123456789123456789123456789def",
				"abc123456789123456789123456788def") > 0);
	}

	@Test
	public void testReallySmallNumbers() {
		NaturalComparator comparator = new NaturalComparator();
		assertTrue(comparator.compare("abc0.0000000000000000000000001def",
				"abc0.0000000000000000000000002def") < 0);
		assertTrue(comparator.compare("abc0.0000000000000000000000002def",
				"abc0.0000000000000000000000001def") > 0);
	}

	@Test
	public void testCaseInsensitivity() {
		NaturalComparator comparator = new NaturalComparator();
		assertTrue(comparator.compare("a", "A") == 0);
		assertTrue(comparator.compare("aBc123dEf", "aBC123Def") == 0);
	}

	@Test
	public void testSpaceBetweenChunksInsensitivity() {
		NaturalComparator comparator = new NaturalComparator();
		assertTrue(comparator.compare("abc123def", "abc123def") == 0);
		assertTrue(comparator.compare("abc123def", "abc 123def") == 0);
		assertTrue(comparator.compare("abc123def", "abc123 def") == 0);
		assertTrue(comparator.compare("abc123def", "abc 123 def") == 0);

		assertTrue(comparator.compare("abc 123def", "abc123def") == 0);
		assertTrue(comparator.compare("abc 123def", "abc 123def") == 0);
		assertTrue(comparator.compare("abc 123def", "abc123 def") == 0);
		assertTrue(comparator.compare("abc 123def", "abc 123 def") == 0);

		assertTrue(comparator.compare("abc123 def", "abc123def") == 0);
		assertTrue(comparator.compare("abc123 def", "abc 123def") == 0);
		assertTrue(comparator.compare("abc123 def", "abc123 def") == 0);
		assertTrue(comparator.compare("abc123 def", "abc 123 def") == 0);

		assertTrue(comparator.compare("abc 123 def", "abc123def") == 0);
		assertTrue(comparator.compare("abc 123 def", "abc 123def") == 0);
		assertTrue(comparator.compare("abc 123 def", "abc123 def") == 0);
		assertTrue(comparator.compare("abc 123 def", "abc 123 def") == 0);
	}

	@Test
	public void testEmptyString() {
		NaturalComparator comparator = new NaturalComparator();
		assertTrue(comparator.compare("", "") == 0);
		assertTrue(comparator.compare("", "a") < 0);
		assertTrue(comparator.compare("a", "") > 0);
	}

	@Test
	public void testDifferentStartString() {
		NaturalComparator comparator = new NaturalComparator();
		assertTrue(comparator.compare("1", "a") < 0);
		assertTrue(comparator.compare("a", "2b") > 0);
		assertTrue(comparator.compare("ab", "2b") > 0);
		assertTrue(comparator.compare("b", "2b") > 0);
	}

}
