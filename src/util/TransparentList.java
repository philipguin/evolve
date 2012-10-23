package util;

import java.util.List;

public class TransparentList<B, T> extends ImmutableList<T>
{
	private final List<? extends B> base;
	private final IMapping<? super B, ? extends T> mapping;
	
	public TransparentList(List<? extends B> base, IMapping<? super B, ? extends T> mapping)
	{
		this.base = base;
		this.mapping = mapping;
	}
	
	@Override
	public T get(int index)
	{
		return mapping.map(base.get(index));
	}

	@Override
	public int size()
	{
		return base.size();
	}

}
