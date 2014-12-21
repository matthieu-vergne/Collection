package fr.vergne.collection.filter;



/**
 * A {@link UndecidedFilteringException} arises when no decision can be taken
 * regarding the support or rejection of an element in regard to some
 * {@link Filter}s.
 * 
 * @author Matthieu Vergne <vergne@fbk.eu>
 * 
 */
@SuppressWarnings("serial")
public class UndecidedFilteringException extends RuntimeException {
	public UndecidedFilteringException(String message) {
		super(message);
	}

	public UndecidedFilteringException(Throwable cause) {
		super(cause);
	}

	public UndecidedFilteringException(String message, Throwable cause) {
		super(message, cause);
	}
}
