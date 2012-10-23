package chromosomes;
import java.util.List;

import spawners.ICreator;
import spawners.IMater;


public abstract class ChromosomedType<C, T extends IChromosomed<C, T>>
{
	private final int hierarchyDepth;
	private final ChromosomedType<C, ? super T> parent;
	private final ICreator<? extends C> chromosomeCreator;
	private final IMater<C> chromosomeMater;
	
	protected ChromosomedType(ChromosomedType<C, ? super T> parent, ICreator<? extends C> genomeCreator, IMater<C> genomeMater)
	{
		this.parent = parent;
		
		if (parent == null)
			this.hierarchyDepth = 0;
		else
			this.hierarchyDepth = parent.hierarchyDepth+1;
		
		this.chromosomeCreator = genomeCreator;
		this.chromosomeMater = genomeMater;
	}

	public final int getHierarchyDepth() { return hierarchyDepth; }
	public final ChromosomedType<C, ? super T> getParent() { return parent; }
	public final ICreator<? extends C> getChromosomeCreator() { return chromosomeCreator; }
	public final IMater<C> getChromosomeMater() { return chromosomeMater; }
	
	protected abstract T makeInstance(List<C> chromosomes);
}
