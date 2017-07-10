package fr.vergne.collection.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;

import fr.vergne.collection.MultiMap;

/**
 * A {@link ListMultiMap} is a {@link MultiMap} which allows to have several
 * instances for each (key, value). If the same value is assigned several times
 * to the same key, the value is added at each time, and to remove it fully it
 * has to be removed at least the same number of time.
 * 
 * @author Matthieu Vergne <vergne@fbk.eu>
 * 
 * @param <Key>
 * @param <Value>
 * @deprecated The code now has its dedicated project at: https://github.com/matthieu-vergne/multi-map
 */
public class ListMultiMap<Key, Value> extends AbstractMultiMap<Key, Value>
		implements MultiMap<Key, Value> {

	public ListMultiMap(MultiMap<Key, Value> map) {
		for (Entry<Key, Collection<Value>> entry : map.entrySet()) {
			addAll(entry.getKey(), entry.getValue());
		}
	}

	public ListMultiMap() {
	}

	@Override
	protected Collection<Value> generateInnerCollection(Key key) {
		return new LinkedList<Value>();
	}

}
