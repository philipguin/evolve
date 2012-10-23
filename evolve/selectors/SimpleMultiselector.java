package selectors;

import java.util.ArrayList;
import java.util.List;

public class SimpleMultiselector<T> implements IMultiselector<T>
{
	private final ISelector<T> selector;
	
	public SimpleMultiselector(ISelector<T> selector)
	{
		this.selector = selector;
	}
	
	@Override
	public List<T> select(List<? extends T> choices, int numberToSelect)
	{
		List<T> result = new ArrayList<T>(numberToSelect);
		
		for (int i = 0; i < numberToSelect; ++i)
			result.add(selector.select(choices));
		
		return result;
	}

}
