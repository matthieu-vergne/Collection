package fr.vergne.collection.filter;

/**
 * A {@link FilteringStrategy} allows to describe how are managed the undecided
 * cases. An undecided case happen when both supporting {@link Filter}s and
 * rejecting {@link Filter}s are found, as well as no supporter nor rejector is
 * found. In such a case, different needs implies different management
 * strategies, and simple strategies are represented by one of these
 * {@link FilteringStrategy}s.
 * 
 * @author Matthieu Vergne <vergne@fbk.eu>
 * 
 */
public enum FilteringStrategy {
	/**
	 * When an undecided case happen, we consider the element to be supported.
	 */
	CONSERVATIVE,
	/**
	 * When an undecided case happen, we consider the element to be rejected.
	 */
	EXPEDITIVE,
	/**
	 * When an undecided case happen, we throw an exception.
	 */
	EXPLICIT
}
