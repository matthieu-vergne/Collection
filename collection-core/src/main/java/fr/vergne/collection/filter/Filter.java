package fr.vergne.collection.filter;

/**
 * A {@link Filter} allows to tell whether an element is supported or rejected.
 * It can also be undecided if the {@link Filter} does not manage such element.
 * 
 * @author Matthieu Vergne <vergne@fbk.eu>
 * 
 * @param <Element>
 */
public interface Filter<Element> {
	/**
	 * 
	 * @param element
	 *            the element to consider
	 * @return <code>true</code> if the element should be kept,
	 *         <code>false</code> if it should not be kept, <code>null</code>
	 *         otherwise (undecided case)
	 */
	public Boolean isSupported(Element element);
}
