package processing;

public interface IResourceProvider<T>
{
	public boolean hasResourceReady();
	public T getNextResource();
	public void returnResource(T result);
}
