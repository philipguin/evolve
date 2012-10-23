package selectors;

import java.util.List;

public interface ISelector<T>
{
	public T select(List<? extends T> choices);
}
