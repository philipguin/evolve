package util;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class ImmutableList<T> implements List<T>
{
	@Override
	public boolean add(T e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, T element) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o)
	{
		for (int i = 0; i < size(); ++i)
			if (get(i).equals(o))
				return true;
		
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		otherLoop:
		for (Object o : c)
		{
			for (int i = 0; i < size(); ++i)
				if (get(i).equals(o))
					continue otherLoop;
			
			return false;
		}
		return true;
	}

	@Override
	public int indexOf(Object o)
	{
		if (o == null)
		{
			for (int i = 0; i < size(); ++i)
				if (get(i) == null)
					return i;
		}
		else
		{
			for (int i = 0; i < size(); ++i)
				if (o.equals(get(i)))
					return i;
		}
		
		return -1;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>()
		{
			int i = 0;
			int length = size();
			
			@Override
			public boolean hasNext()
			{
				return i < length;
			}

			@Override
			public T next()
			{
				return get(i++);
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
				
			}
			
		};
	}

	@Override
	public int lastIndexOf(Object o)
	{
		int result = -1;
		
		if (o == null)
		{
			for (int i = 0; i < size(); ++i)
				if (get(i) == null)
					result = i;
		}
		else
		{
			for (int i = 0; i < size(); ++i)
				if (o.equals(get(i)))
					result = i;
		}
		
		return result;
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T remove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T set(int index, T element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray()
	{
		Object[] result = new Object[size()];
		
		for (int i = 0; i < size(); ++i)
			result[i] = get(i);
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> R[] toArray(R[] a)
	{
		if (a == null)
			throw new NullPointerException();
		
		if (a.length < size())
			a = (R[])new Object[size()];
		
		int i = 0;
		
		for (; i < size(); ++i)
			a[i] = (R)get(i);
		
		if (i < a.length)
			a[i] = null;
		
		return a;
	}
}
