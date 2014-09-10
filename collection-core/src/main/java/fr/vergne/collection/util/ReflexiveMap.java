package fr.vergne.collection.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ReflexiveMap<Key, Value> implements Map<Key, Value> {

	private final Map<Key, Value> keyValue;
	private final Map<Value, Key> valueKey;
	private final ReflexiveMap<Value, Key> reversed;

	public ReflexiveMap() {
		keyValue = new LinkedHashMap<Key, Value>();
		valueKey = new LinkedHashMap<Value, Key>();
		reversed = new ReflexiveMap<Value, Key>(this);
	}

	private ReflexiveMap(ReflexiveMap<Value, Key> reflex) {
		keyValue = reflex.valueKey;
		valueKey = reflex.keyValue;
		reversed = reflex;
	}

	@Override
	public void clear() {
		keyValue.clear();
		valueKey.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return keyValue.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return keyValue.containsValue(value);
	}

	@Override
	public Set<Entry<Key, Value>> entrySet() {
		return keyValue.entrySet();
	}

	@Override
	public Value get(Object key) {
		return keyValue.get(key);
	}

	public Value getValueFrom(Key key) {
		return keyValue.get(key);
	}

	public Key getKeyFrom(Value value) {
		return valueKey.get(value);
	}

	@Override
	public boolean isEmpty() {
		return keyValue.isEmpty();
	}

	@Override
	public Set<Key> keySet() {
		return keyValue.keySet();
	}

	@Override
	public Value put(Key key, Value value) {
		valueKey.remove(keyValue.get(key));
		valueKey.put(value, key);
		return keyValue.put(key, value);
	}

	@Override
	public void putAll(Map<? extends Key, ? extends Value> m) {
		for (Entry<? extends Key, ? extends Value> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Value remove(Object key) {
		Value removed = keyValue.remove(key);
		valueKey.remove(removed);
		return removed;
	}

	@Override
	public int size() {
		return keyValue.size();
	}

	@Override
	public Collection<Value> values() {
		return keyValue.values();
	}

	public ReflexiveMap<Value, Key> reverse() {
		return reversed;
	}
}