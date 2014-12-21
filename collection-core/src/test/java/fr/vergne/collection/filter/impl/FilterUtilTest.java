package fr.vergne.collection.filter.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import fr.vergne.collection.filter.Filter;
import fr.vergne.collection.filter.FilterUtil;
import fr.vergne.collection.filter.FilteringStrategy;
import fr.vergne.collection.filter.UndecidedFilteringException;

public class FilterUtilTest {

	private final Filter<Integer> keepAll = new Filter<Integer>() {

		@Override
		public Boolean isSupported(Integer element) {
			return true;
		}
	};
	private final Filter<Integer> keepNone = new Filter<Integer>() {

		@Override
		public Boolean isSupported(Integer element) {
			return false;
		}
	};
	private final Filter<Integer> uninformative = new Filter<Integer>() {

		@Override
		public Boolean isSupported(Integer element) {
			return null;
		}
	};

	@SuppressWarnings("unchecked")
	@Test
	public void testConservativeStrategies() throws UndecidedFilteringException {
		Collection<Integer> elements = Arrays.asList(0, 1, 2, 3, 4, 5);
		Collection<Integer> filtered;

		filtered = FilterUtil.filter(elements, FilteringStrategy.CONSERVATIVE,
				FilteringStrategy.EXPLICIT, uninformative);
		assertEquals(elements.size(), filtered.size());
		assertTrue(elements.containsAll(filtered));
		assertTrue(filtered.containsAll(elements));

		filtered = FilterUtil.filter(elements, FilteringStrategy.EXPLICIT,
				FilteringStrategy.CONSERVATIVE, keepAll, keepNone);
		assertEquals(elements.size(), filtered.size());
		assertTrue(elements.containsAll(filtered));
		assertTrue(filtered.containsAll(elements));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testExpeditiveStrategies() throws UndecidedFilteringException {
		Collection<Integer> elements = Arrays.asList(0, 1, 2, 3, 4, 5);
		Collection<Integer> filtered;

		filtered = FilterUtil.filter(elements, FilteringStrategy.EXPEDITIVE,
				FilteringStrategy.EXPLICIT, uninformative);
		assertEquals(0, filtered.size());

		filtered = FilterUtil.filter(elements, FilteringStrategy.EXPLICIT,
				FilteringStrategy.EXPEDITIVE, keepAll, keepNone);
		assertEquals(0, filtered.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testExplicitStrategies() {
		Collection<Integer> elements = Arrays.asList(0, 1, 2, 3, 4, 5);
		boolean isExceptionThrown;

		isExceptionThrown = false;
		try {
			FilterUtil.filter(elements, FilteringStrategy.EXPLICIT,
					FilteringStrategy.CONSERVATIVE, uninformative);
		} catch (UndecidedFilteringException e) {
			isExceptionThrown = true;
		}
		assertTrue(isExceptionThrown);

		isExceptionThrown = false;
		try {
			FilterUtil.filter(elements, FilteringStrategy.CONSERVATIVE,
					FilteringStrategy.EXPLICIT, keepAll, keepNone);
		} catch (UndecidedFilteringException e) {
			isExceptionThrown = true;
		}
		assertTrue(isExceptionThrown);
	}

}
