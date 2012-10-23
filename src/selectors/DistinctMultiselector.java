package selectors;

import java.util.ArrayList;
import java.util.List;

public class DistinctMultiselector<T> implements IMultiselector<T>
{
	private final ISelector<T> selector;
	private final int maxIndividualSelectAttempts, maxGroupSelectAttempts;
	
	public DistinctMultiselector(ISelector<T> selector)
	{
		this(10000, 100, selector);
	}
	
	public DistinctMultiselector(int maxIndividualSelectAttempts, int maxGroupSelectAttempts, ISelector<T> selector)
	{
		this.maxIndividualSelectAttempts = maxIndividualSelectAttempts;
		this.maxGroupSelectAttempts = maxGroupSelectAttempts;
		this.selector = selector;
	}
	
	@Override
	public final List<T> select(List<? extends T> choices, int numberToSelect)
	{
		List<T> result = new ArrayList<T>(numberToSelect);
		
		T selection;
		int groupSelectAttempt = 0;
		int individualSelectAttempt;

		groupSelection:
		for (int selectionCount = 0; selectionCount < numberToSelect; ++selectionCount)
		{
			individualSelectAttempt = 0;

			individualSelection:
			do
			{
				selection = selector.select(choices);
				
				if (!result.contains(selection))
					break individualSelection;
				
				if (++individualSelectAttempt < maxIndividualSelectAttempts)
					continue individualSelection;
				
				if (++groupSelectAttempt < maxGroupSelectAttempts)
				{
					selectionCount = -1;
					result.clear();
					continue groupSelection;
				}
				
				throw new Error("Error: could not find "+numberToSelect+" distinct choices!\n\tMax group attempts:\t"+groupSelectAttempt+"\n\tMax individual attempts:\t"+individualSelectAttempt);
			}
			while (true);
			
			result.add(selection);
		}
		
		return result;
	}
}
