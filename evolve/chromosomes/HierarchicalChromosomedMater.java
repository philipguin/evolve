package chromosomes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import spawners.IMater;

public final class HierarchicalChromosomedMater<C, R extends IChromosomed<C, R>> implements IMater<R>
{
	private final Random random;
	
	public HierarchicalChromosomedMater(Random random)
	{
		this.random = random;
	}
	
	
	private static final class HierarchyDepthComparator implements Comparator<IChromosomed<?, ?>>
	{
		@Override
		public int compare(IChromosomed<?, ?> a, IChromosomed<?, ?> b)
		{
			return b.getChromosomedType().getHierarchyDepth() - a.getChromosomedType().getHierarchyDepth();
		}
	}
	
	private static final Comparator<IChromosomed<?, ?>> hierarchicalComparator = new HierarchyDepthComparator();
	
	
	
	/** @param parents - must be mutable!!!*/
	public R mate(List<? extends R> parents)
	{
		//List of what this function does, in the order it does it:
		//  Sort parents by their chromosome depths (from highest to lowest).
		//  Pick parent at random.
		//  Traverse parents' hierarchies to left until their depths match the chosen parent's depth.
		//  Traverse all hierarchies simultaneously, expanding rightward as we go, performing replication on any matching sets of chromosomes.
		
		
		//sort
		Collections.sort(parents, hierarchicalComparator);
		
		//pick parent
		int chosenIndex = random.nextInt(parents.size());
		
		@SuppressWarnings("unchecked")
		ChromosomedType<C, ?>[] types = (ChromosomedType<C, ?>[])new ChromosomedType[parents.size()];
		
		ChromosomedType<C, ?> type;
		int chosenParentDepth = parents.get(chosenIndex).getChromosomedType().getHierarchyDepth();
		
		//match depths on left
		for (int i = 0; i <= chosenIndex; ++i)
		{
			type = parents.get(i).getChromosomedType();
			while (type.getHierarchyDepth() > chosenParentDepth)
				type = type.getParent();
			
			types[i] = type;
		}


		//replicate chromosomes according to type
		List<C> result = new ArrayList<C>(chosenParentDepth);
		List<C> matches = new ArrayList<C>(parents.size());
		int chromosomeContenderLine = chosenIndex + 1;
		ChromosomedType<C, ?> chosenType;
		int i;
		
		while (types[0] != null)
		{
			chosenType = types[chosenIndex];
			
			for (i = 0; i < chromosomeContenderLine; ++i)
			{
				if (chosenType.equals(types[i]))
					matches.add(parents.get(i).getChromosomes().get(chosenType.getHierarchyDepth()));
				
				types[i] = types[i].getParent();
			}
			
			result.add(chosenType.getChromosomeMater().mate(matches));
			matches.clear();
			
			while (chromosomeContenderLine < types.length
					&& parents.get(chromosomeContenderLine).getChromosomedType().getHierarchyDepth() == chosenType.getHierarchyDepth())
			{
				types[chromosomeContenderLine] = parents.get(chromosomeContenderLine).getChromosomedType();
				++chromosomeContenderLine;
			}
		}
		
		Collections.reverse(result);
		return parents.get(chosenIndex).getChromosomedType().makeInstance(result);
	}	
}