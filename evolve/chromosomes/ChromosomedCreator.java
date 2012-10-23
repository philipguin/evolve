package chromosomes;

import java.util.ArrayList;
import java.util.List;

import spawners.ICreator;

public final class ChromosomedCreator<C, T extends IChromosomed<C, T>> implements ICreator<T>
{
	private final ChromosomedType<C, T> type;
	
	public ChromosomedCreator(ChromosomedType<C, T> type)
	{
		this.type = type;
	}
	
	public final T create()
	{
		List<C> chromosomes = new ArrayList<C>(type.getHierarchyDepth());
		
		buildRandomChromosomeList(type, chromosomes);
		
		return type.makeInstance(chromosomes);
	}
	
	private static final <C> void buildRandomChromosomeList(ChromosomedType<C, ?> type, List<? super C> chromosomes)
	{
		if (type.getParent() != null)
			buildRandomChromosomeList(type.getParent(), chromosomes);
		
		chromosomes.add(type.getChromosomeCreator().create());
	}
}