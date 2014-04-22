package fr.vergne.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A {@link MultiMap} allows to map a key to several values. This class is
 * similar to the <a src=
 * "http://commons.apache.org/proper/commons-collections/javadocs/api-3.2.1/org/apache/commons/collections/MultiMap.html"
 * >Apache MultiMap</a>, but assumes that it is a proper {@link Map} between a
 * key and a <u>set of</u> values rather than a special {@link Map} between a
 * key and each of its <u>single</u> values. Thus, where the Apache one appears
 * as a common {@link Map} but with the need to make casting and other tricky
 * things to use it fully, this {@link MultiMap} implements as much as possible
 * the concept to provide a user-friendly class.
 * 
 * @author Matthieu Vergne <vergne@fbk.eu>
 * 
 * @param <Key>
 * @param <Value>
 */
public interface MultiMap<Key, Value> extends Map<Key, Collection<Value>>,
		Iterable<Entry<Key, Value>> {
	/**
	 * Same as {@link #populate(Object, Collection)}.
	 */
	public boolean populate(Key key, Value... values);

	/**
	 * Map a key to all the provided values. If there is already values mapped
	 * to this key, the new values are added (the old ones are kept, not
	 * replaced).
	 * 
	 * @return <code>true</code> if some values have been added
	 */
	public boolean populate(Key key, Collection<Value> values);

	/**
	 * Same as {@link #depopulate(Object, Collection)}.
	 */
	public boolean depopulate(Key key, Value... values);

	/**
	 * Remove some values from a key. Values which are not mapped to this key
	 * does not generate any exception.
	 * 
	 * @param key
	 *            the key to remove values from
	 * @param values
	 *            the values to remove
	 * @return <code>true</code> if some values have been removed
	 */
	public boolean depopulate(Key key, Collection<Value> values);

	/**
	 * 
	 * @param key
	 *            the key to check
	 * @param value
	 *            the value to check
	 * @return <code>true</code> if the key is known and the value is actually
	 *         mapped to it
	 */
	public boolean containsCouple(Key key, Value value);

}
