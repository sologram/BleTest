package com.sologram.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MapList extends ArrayList implements List {
	static private final String TAG = PopupMenu.class.getSimpleName();
	static private Object nil = "\255";

	private Map<Object, Object> map = new TreeMap();

	public boolean add(Object key, Object value) {
		if (key == null)
			key = nil;
		if (map.get(key) == null) {
			add(key);
			map.put(key, value == null ? nil : value);
			return true;
		}
		return false;
	}

	public Object getValue(int index) {
		return getValue(get(index));
	}

	public Object getValue(Object key) {
		Object re = map.get(key);
		return re == nil ? null : re;
	}

	public boolean remove(Object key) {
		if (key == null)
			key = nil;
		boolean re = super.remove(key);
		if (re)
			map.remove(key);
		return re;
	}
}
