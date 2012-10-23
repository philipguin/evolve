package selectors;

import java.util.List;

public interface IMultiselector<T>
{
	public List<T> select(List<? extends T> choices, int numberToSelect);
}
