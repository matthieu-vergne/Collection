package fr.vergne.collection.filter;

import java.util.Collection;
import java.util.LinkedList;

public class FilterUtil {

	/**
	 * Convenient method to filter the elements, trying to be as strict as
	 * possible. This is equivalent to
	 * {@link #filter(Collection, FilteringStrategy, FilteringStrategy, Filter...)}
	 * with both filtering modes at {@link FilteringStrategy#EXPLICIT}.
	 */
	public static <Element> Collection<Element> filter(
			Collection<Element> elements, Filter<Element>... filters)
			throws UndecidedFilteringException {
		return filter(elements, FilteringStrategy.EXPLICIT,
				FilteringStrategy.EXPLICIT, filters);
	}

	/**
	 * Convenient method to filter the elements, trying to keep as much as
	 * possible. This is equivalent to
	 * {@link #filter(Collection, FilteringStrategy, FilteringStrategy, Filter...)}
	 * with both filtering modes at {@link FilteringStrategy#CONSERVATIVE}.
	 */
	public static <Element> Collection<Element> filterConservatively(
			Collection<Element> elements, Filter<Element>... filters)
			throws UndecidedFilteringException {
		return filter(elements, FilteringStrategy.CONSERVATIVE,
				FilteringStrategy.CONSERVATIVE, filters);
	}

	/**
	 * Convenient method to filter the elements, trying to reject as much as
	 * possible. This is equivalent to
	 * {@link #filter(Collection, FilteringStrategy, FilteringStrategy, Filter...)}
	 * with both filtering modes at {@link FilteringStrategy#EXPEDITIVE}.
	 */
	public static <Element> Collection<Element> filterExpeditively(
			Collection<Element> elements, Filter<Element>... filters)
			throws UndecidedFilteringException {
		return filter(elements, FilteringStrategy.EXPEDITIVE,
				FilteringStrategy.EXPEDITIVE, filters);
	}

	/**
	 * This method filter a collection of elements by using some {@link Filter}
	 * s. In the case no clear decision can be taken, some simple
	 * {@link FilteringStrategy}s are applied. Basically, it build a
	 * {@link FilterDecider} based on these {@link FilteringStrategy}s and
	 * return the result of
	 * {@link #filter(Collection, FilterDecider, Filter...)}.
	 * 
	 * @param elements
	 *            the elements to filter
	 * @param uninformativeStrategy
	 *            {@link FilteringStrategy} to apply when no supporter nor
	 *            rejector is found
	 * @param conflictualStrategy
	 *            {@link FilteringStrategy} to apply when both supporters and
	 *            rejectors are found
	 * @param filters
	 *            the filters to apply
	 * @return the filtered collection
	 * @throws UndecidedFilteringException
	 *             when the {@link FilteringStrategy}s does not allow to decide
	 */
	public static <Element> Collection<Element> filter(
			Collection<Element> elements,
			final FilteringStrategy uninformativeStrategy,
			final FilteringStrategy conflictualStrategy,
			Filter<Element>... filters) throws UndecidedFilteringException {
		return filter(elements, new FilterDecider<Element>() {

			@Override
			public boolean isSupported(Element element,
					Collection<Filter<Element>> supporters,
					Collection<Filter<Element>> rejectors)
					throws UndecidedFilteringException {
				if (supporters.isEmpty() && rejectors.isEmpty()) {
					return decide(uninformativeStrategy, element, supporters,
							rejectors);
				} else if (!supporters.isEmpty() && !rejectors.isEmpty()) {
					return decide(conflictualStrategy, element, supporters,
							rejectors);
				} else {
					throw new IllegalArgumentException(
							"One of the supporter/rejector collection is empty, thus it is not an undecided case: supporters "
									+ supporters + ", rejectors " + rejectors);
				}
			}

			private boolean decide(FilteringStrategy mode, Element element,
					Collection<Filter<Element>> supporters,
					Collection<Filter<Element>> rejectors)
					throws UndecidedFilteringException {
				switch (mode) {
				case CONSERVATIVE:
					return true;
				case EXPEDITIVE:
					return false;
				case EXPLICIT:
					throw new UndecidedFilteringException("The filtering of "
							+ element + " is undecided: supporters "
							+ supporters + " VS rejectors " + rejectors);
				default:
					throw new RuntimeException("Unmanaged case: " + mode);
				}
			}
		}, filters);
	}

	/**
	 * This method filter a collection of elements by using {@link Filter}s. In
	 * the case the support/rejection cannot be decided (no {@link Filter} gives
	 * an informative answer or both supporters and rejectors are found), a
	 * {@link FilterDecider} take the lead and decide the final answer. This
	 * allows for instance to manage custom strategies such as prioritized or
	 * weighted {@link Filter}s.
	 * 
	 * @param elements
	 *            the elements to filter
	 * @param decider
	 *            a {@link FilterDecider} which manages the undecided cases
	 * @param filters
	 *            the filters to apply
	 * @return the filtered collection
	 * @throws UndecidedFilteringException
	 *             when the {@link FilterDecider} is not able to decide
	 */
	public static <Element> Collection<Element> filter(
			Collection<Element> elements, FilterDecider<Element> decider,
			Filter<Element>... filters) throws UndecidedFilteringException {
		Collection<Element> filteredElements = new LinkedList<Element>();
		for (Element element : elements) {
			Collection<Filter<Element>> supporters = new LinkedList<Filter<Element>>();
			Collection<Filter<Element>> rejectors = new LinkedList<Filter<Element>>();
			for (Filter<Element> filter : filters) {
				Boolean accepted = filter.isSupported(element);
				if (accepted == null) {
					// uninformative, don't consider it
				} else if (accepted == true) {
					supporters.add(filter);
				} else if (accepted == false) {
					rejectors.add(filter);
				} else {
					throw new RuntimeException("Unmanaged value: " + accepted);
				}
			}
			boolean isSupported;
			if (supporters.isEmpty() == rejectors.isEmpty()) {
				isSupported = decider.isSupported(element, supporters,
						rejectors);
			} else if (!supporters.isEmpty() && rejectors.isEmpty()) {
				isSupported = true;
			} else if (supporters.isEmpty() && !rejectors.isEmpty()) {
				isSupported = false;
			} else {
				throw new RuntimeException("This case should not happen.");
			}
			if (isSupported) {
				filteredElements.add(element);
			} else {
				// rejected, ignore it
			}
		}
		return filteredElements;
	}

}
