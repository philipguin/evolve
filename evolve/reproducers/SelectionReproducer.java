package reproducers;

import java.util.ArrayList;
import java.util.List;

import selectors.IMultiselector;
import spawners.IMater;


public class SelectionReproducer<T> implements IReproducer<T>
{
	private final IMultiselector<T> selector;
	private final int parentCount;
	
	public SelectionReproducer(int parentCount, IMultiselector<T> selector)
	{
		this.parentCount = parentCount;
		this.selector = selector;
	}
	
	public final List<T> makeNextGeneration(List<T> population, IMater<T> mater)
	{
		List<T> babies = new ArrayList<T>(population.size());
		
		for (int baby = 0; baby < population.size(); ++baby)
		{
			babies.add(mater.mate(selector.select(population, parentCount)));
		}
		
		return babies;
	}
}