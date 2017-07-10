package fr.vergne.collection.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;

import fr.vergne.collection.MultiMap;

/**
 * A {@link HashMultiMap} is a {@link MultiMap} which allows at most one
 * instance for each (key, value). If the same value is assigned several times
 * to the same key, it is the same than providing it only once, and removing it
 * once is enough to not have it anymore in the map.
 * 
 * @author Matthieu Vergne <vergne@fbk.eu>
 * 
 * @param <Key>
 * @param <Value>
 * @deprecated The code now has its dedicated project at: https://github.com/matthieu-vergne/multi-map
 */
public class HashMultiMap<Key, Value> extends AbstractMultiMap<Key, Value>
		implements MultiMap<Key, Value> {

	public HashMultiMap(MultiMap<Key, Value> map) {
		for (Entry<Key, Collection<Value>> entry : map.entrySet()) {
			addAll(entry.getKey(), entry.getValue());
		}
	}

	public HashMultiMap() {
	}

	@Override
	protected Collection<Value> generateInnerCollection(Key key) {
		return new HashSet<Value>();
	}

}
