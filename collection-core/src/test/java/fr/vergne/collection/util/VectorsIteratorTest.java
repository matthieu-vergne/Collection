package fr.vergne.collection.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class VectorsIteratorTest {

	@Test
	public void testIterator() {
		List<Integer> values1 = Arrays.asList(1, 2, 3);
		List<String> values2 = Arrays.asList("a", "b");
		List<Integer> values3 = values1;
		@SuppressWarnings("unchecked")
		VectorsIterator<Object> iterator = new VectorsIterator<Object>(values1,
				values2, values3);

		Collection<List<Object>> expected = new LinkedList<List<Object>>();
		expected.add(Arrays.<Object> asList(1, "a", 1));
		expected.add(Arrays.<Object> asList(1, "a", 2));
		expected.add(Arrays.<Object> asList(1, "a", 3));
		expected.add(Arrays.<Object> asList(1, "b", 1));
		expected.add(Arrays.<Object> asList(1, "b", 2));
		expected.add(Arrays.<Object> asList(1, "b", 3));
		expected.add(Arrays.<Object> asList(2, "a", 1));
		expected.add(Arrays.<Object> asList(2, "a", 2));
		expected.add(Arrays.<Object> asList(2, "a", 3));
		expected.add(Arrays.<Object> asList(2, "b", 1));
		expected.add(Arrays.<Object> asList(2, "b", 2));
		expected.add(Arrays.<Object> asList(2, "b", 3));
		expected.add(Arrays.<Object> asList(3, "a", 1));
		expected.add(Arrays.<Object> asList(3, "a", 2));
		expected.add(Arrays.<Object> asList(3, "a", 3));
		expected.add(Arrays.<Object> asList(3, "b", 1));
		expected.add(Arrays.<Object> asList(3, "b", 2));
		expected.add(Arrays.<Object> asList(3, "b", 3));

		while (iterator.hasNext()) {
			List<Object> candidate = iterator.next();
			boolean found = false;
			Iterator<List<Object>> targetIterator = expected.iterator();
			while (targetIterator.hasNext()) {
				List<Object> target = targetIterator.next();
				found = isSame(candidate, target);
				if (found) {
					targetIterator.remove();
					break;
				} else {
					// try another one
				}
			}
			if (found) {
				continue;
			} else {
				fail("Not expected: " + candidate);
			}
		}

		assertTrue("Not generated: " + expected, expected.isEmpty());
	}

	private <T> boolean isSame(List<T> list1, List<T> list2) {
		if (list1.size() != list2.size()) {
			return false;
		} else {
			for (int i = 0; i < list1.size(); i++) {
				if (list1.get(i).equals(list2.get(i))) {
					continue;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	@Test
	public void testNotSameInstance() {
		List<Integer> values = Arrays.asList(1, 2, 3);
		@SuppressWarnings("unchecked")
		VectorsIterator<Integer> iterator = new VectorsIterator<Integer>(
				values, values, values);

		Collection<List<Integer>> instances = new LinkedList<List<Integer>>();
		while (iterator.hasNext()) {
			List<Integer> vector = iterator.next();
			for (List<Integer> seen : instances) {
				if (seen == vector) {
					fail("Same instance reused");
				} else {
					continue;
				}
			}
			instances.add(vector);
		}
	}

}
