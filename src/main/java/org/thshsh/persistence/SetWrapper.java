package org.thshsh.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * So this "Set" is actually a wrapper list that DOES NOT enforce Set logic. 
 * Which means it is only suitable for situations where the calling code can guarantee that no duplicates will be added to the collection
 * It can use any delegate collection, which means it can operate with the performance of a different implementation while maintaining
 * API compatibility
 * @author daniel.watson
 *
 * @param <T>
 */
public class SetWrapper<T> implements Set<T>, Serializable {

	private static final long serialVersionUID = 7015114365281200696L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SetWrapper.class);
	
	protected Collection<T> delegate;

	public SetWrapper(Collection<T> delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return delegate.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return delegate.iterator();
	}

	@Override
	public Object[] toArray() {
		return delegate.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return delegate.toArray(a);
	}

	@Override
	public boolean add(T e) {
		return delegate.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return delegate.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return delegate.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return delegate.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return delegate.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return delegate.removeAll(c);
	}

	@Override
	public void clear() {
		delegate.clear();
	}

	@Override
	public int hashCode() {
		LOGGER.info("hashcode");
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		LOGGER.info("equals");
		return super.equals(obj);
	}
	
	
	
}