package ch.epfl.general_libraries.utils;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class SimpleMap<K, V> extends AbstractMap<K, V> implements Cloneable, Serializable {

	private static final long serialVersionUID = 8568052613444573038l;

	private transient Set<Entry<K, V>> entries = null;
	private ArrayList<Entry<K, V>> list;

	public SimpleMap() {
		list = new ArrayList<Entry<K, V>>();
	}

	public SimpleMap(Map<K, V> map) {
		this(map.size());
		putAll(map);
	}

	public SimpleMap(Collection<Pair<K,V>> col) {
		this(col.size());
		for (Pair<K,V> p : col) {
			this.put(p.getFirst(), p.getSecond());
		}
	}

	public SimpleMap(int initCapacity) {
		list = new ArrayList<Entry<K, V>>(initCapacity);
	}

	public SimpleMap(K k, V v) {
		this(1);
		put(k,v);

	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		if (entries == null) {
			entries = new AbstractSet<Entry<K, V>>() {
				@Override
				public void clear() {
					list.clear();
				}

				@Override
				public Iterator<Entry<K, V>> iterator() {
					return list.iterator();
				}

				@Override
				public int size() {
					return list.size();
				}
			};
		}
		return entries;
	}

	@Override
	public V put(K key, V value) {
		int size = list.size();
		Entry<K, V> entry = null;

		boolean exists = false;
		if (key == null) {
			for (int i = 0; i < size; i++) {
				entry = (list.get(i));
				if (entry.getKey() == null) {
					exists = true;
					break;
				}
			}
		} else {
			for (int i = 0; i < size; i++) {
				entry = (list.get(i));
				if (key.equals(entry.getKey())) {
					exists = true;
					break;
				}
			}
		}


		V oldValue;
		if (exists) {
			oldValue = entry.getValue();
			entry.setValue(value);
		} else {
			oldValue = null;
			list.add(new SimpleEntry<K, V>(key, value));
		}
		return oldValue;
	}

	@Override
	public V remove(Object key) {
		Entry<K, V> entry = null;
		for(Entry<K, V> e: list) {
			if (e.getKey().equals(key)) {
				entry = e;
				break;
			}
		}

		if (entry == null) {
			entries = null;
			return null;
		}
		else {
			list.remove(entry);
			return entry.getValue();
		}
	}
	@Override
	public Object clone() {
		return new SimpleMap<K, V>(this);
	}

}