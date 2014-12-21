package fr.vergne.collection.filter;

import java.util.Collection;


/**
 * A {@link FilterDecider} allows to specify how an undecided case should be
 * managed. For instance, when no {@link Filter} supports nor rejects an
 * element, or when both supporters and rejectors are found.
 * 
 * @author Matthieu Vergne <vergne@fbk.eu>
 * 
 * @param <Element>
 */
public interface FilterDecider<Element> {
	/**
	 * 
	 * @param element
	 *            the element in an undecided case
	 * @param supporters
	 *            the {@link Filter}s supporting it
	 * @param rejectors
	 *            the {@link Filter}s rejecting it
	 * @return <code>true</code> if the element should be supported,
	 *         <code>false</code> if it should not
	 * @throws UndecidedFilteringException
	 *             if no decision can be made for the element
	 */
	public boolean isSupported(Element element,
			Collection<Filter<Element>> supporters,
			Collection<Filter<Element>> rejectors)
			throws UndecidedFilteringException;
}