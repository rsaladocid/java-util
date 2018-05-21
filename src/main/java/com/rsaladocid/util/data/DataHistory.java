package com.rsaladocid.util.data;

import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * A {@link ConcurrentHashMap} where each key has a {@link Deque} of values.
 * </p>
 * <p>
 * This class is used to store the different values that each key has along the
 * time. Each value is stored in a {@link DataRecord} that contains the own
 * value and the timestamp in which the value was set.
 * </p>
 *
 * @param <K>
 *            the type of keys maintained by this map
 * @param <V>
 *            the type of mapped values
 */
public class DataHistory<K, V> extends ConcurrentHashMap<K, Deque<DataRecord<V>>> {

	private static final long serialVersionUID = -4796826543554802389L;

	/**
	 * Set the key's value to be a one item deque consisting of the supplied value.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the single value of the key
	 */
	public synchronized void putSingle(K key, V value) {
		Deque<DataRecord<V>> entries = get(key);

		if (entries == null) {
			entries = new LinkedList<DataRecord<V>>();
			put(key, entries);
		}

		entries.add(new DataRecord<V>(value));
	}

	/**
	 * Returns a {@link Map} view containing the most recent {@link DataRecord} for
	 * each existing key.
	 * 
	 * @return the map containing the most recent data for each key
	 */
	public Map<K, DataRecord<V>> getAllMostRecent() {
		Map<K, DataRecord<V>> allMostRecent = new HashMap<K, DataRecord<V>>();

		Iterator<K> iterator = keySet().iterator();
		while (iterator.hasNext()) {
			K key = iterator.next();

			Deque<DataRecord<V>> entries = get(key);
			DataRecord<V> entry = null;

			if (entries != null) {
				entry = get(key).peekLast();
			}

			allMostRecent.put(key, entry);
		}

		return allMostRecent;
	}

	/**
	 * A shortcut to return the most recent data stored for the given key.
	 * 
	 * @param key
	 *            the key
	 * @return the most recent data record, or <code>null</code> if these is no data
	 *         record for the given key
	 */
	public DataRecord<V> getMostRecent(K key) {
		return get(key) != null ? get(key).peekLast() : null;
	}

}
