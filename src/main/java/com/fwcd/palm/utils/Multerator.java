package com.fwcd.palm.utils;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * A bridge between various iterator classes.
 */
public class Multerator<T> implements Iterable<T>, Enumeration<T>, Iterator<T> {
	private final Iterator<T> iterator;
	private final Enumeration<T> enumeration;

	public Multerator(Iterator<T> iterator) {
		this.iterator = iterator;
		enumeration = null;
	}

	public Multerator(Iterable<T> iterable) {
		this(iterable.iterator());
	}

	public Multerator(Enumeration<T> enumeration) {
		this.enumeration = enumeration;
		iterator = null;
	}

	@Override
	public boolean hasMoreElements() {
		return hasNext();
	}

	@Override
	public T nextElement() {
		return next();
	}

	@Override
	public boolean hasNext() {
		if (iterator == null) {
			return enumeration.hasMoreElements();
		} else {
			return iterator.hasNext();
		}
	}

	@Override
	public T next() {
		if (iterator == null) {
			return enumeration.nextElement();
		} else {
			return iterator.next();
		}
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}
}