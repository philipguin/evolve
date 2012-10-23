package processing;

import java.util.ArrayDeque;
import java.util.List;


public class CachedResourceProvider<T> implements IResourceProvider<T>
{
	private final ArrayDeque<T> resources;
	
	public CachedResourceProvider(List<? extends T> resources)
	{
		this.resources = new ArrayDeque<T>();
		
		for (T resource : resources)
			this.resources.push(resource);
	}
	
	@Override
	public boolean hasResourceReady()
	{
		return !resources.isEmpty();
	}

	@Override
	public T getNextResource()
	{
		return resources.pop();
	}

	@Override
	public void returnResource(T process)
	{
		resources.push(process);
	}

}
