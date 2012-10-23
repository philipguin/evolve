package competitions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import util.MathHelper;


public class MultithreadableCombinationWar<T> implements ICompetition<T>
{
	private final int contendersPerRound;
	
	private final List<List<Match>> synchronouslyAccessibleMatches = new ArrayList<List<Match>>();
	private final ArrayList<Integer> wins = new ArrayList<Integer>();
	
	private int matchNumber, matchCount, matchIndexInGroup, currentMatchGroup, resolvedMatchCount;
	private List<? extends T> currentContenders;
	
	private final HashMap<T, Integer> winMap = new HashMap<T, Integer>();

	public MultithreadableCombinationWar(int contendersPerRound)
	{
		this.contendersPerRound = contendersPerRound;
	}
	
	private final void incrementIndices(int[] indices, int i, int contenderCount)
	{
		++indices[i];
		
		if (indices[i] >= contenderCount - contendersPerRound + i + 1 && i > 0)
		{
			incrementIndices(indices, i-1, contenderCount);
			indices[i] = indices[i-1] + 1;
		}
	}
	
	@Override
	public void begin(List<? extends T> contenders)
	{
		begin(contenders, false);
	}
	
	public void begin(List<? extends T> contenders, boolean printStats)
	{
		currentContenders = contenders;
		
		matchCount = MathHelper.getBinomialCoefficient(contenders.size(), contendersPerRound);
		synchronouslyAccessibleMatches.clear();
		
		matchNumber = resolvedMatchCount = matchIndexInGroup = currentMatchGroup = 0;
		
		winMap.clear();
		
		if (matchCount == 0)
			return;
		
		wins.clear();
		wins.ensureCapacity(contenders.size());

		for (int i = contenders.size(); i > 0; --i)
			wins.add(Integer.valueOf(0));
		
		
		int[] indices = new int[contendersPerRound];
		
		for (int i = 0; i < contendersPerRound; ++i)
			indices[i] = i;
		
		
		List<Match> matches = new ArrayList<Match>(matchCount);
		
		for (int i = 0; i < matchCount; ++i)
		{
			List<T> opponents = new ArrayList<T>(contendersPerRound);
			
			for (int index : indices)
				opponents.add(contenders.get(index));
			
			matches.add(new Match(opponents, Arrays.copyOf(indices, indices.length)));
			
			incrementIndices(indices, contendersPerRound - 1, contenders.size());
		}
		
		int unsuccessfulIterations = 0;
		int totalIterations = 0;
		
		Collections.shuffle(matches);
		
		while (!matches.isEmpty())
		{
			List<Match> selections = new ArrayList<Match>();
			ListIterator<Match> it = matches.listIterator();
			Match combination;
	
			combinationLoop:
			while (it.hasNext())
			{
				combination = it.next();
				++totalIterations;
				
				for (IMatch<T> selection : selections)
				{
					if (!Collections.disjoint(selection.getOpponents(), combination.getOpponents()))
					{
						++unsuccessfulIterations;
						continue combinationLoop;
					}
				}
				
				selections.add(combination);
				it.remove();
			}
			
			synchronouslyAccessibleMatches.add(selections);
		}
		
		if (printStats)
			printBeginStats(unsuccessfulIterations, totalIterations);
	}

	private final void printBeginStats(int unsuccessfulIterations, int totalIterations)
	{
		for (List<Match> matchGroup : synchronouslyAccessibleMatches)
		{
			for (IMatch<T> match : matchGroup)
			{
				System.out.print("[");
				
				for (T opponent : match.getOpponents())
					System.out.print(opponent + " ");

				System.out.print("]   \t");
			}
			
			System.out.println();
		}

		System.out.println();
		System.out.println("Average barrier size: " + matchCount / synchronouslyAccessibleMatches.size());
		System.out.println("Total thread barriers: " + synchronouslyAccessibleMatches.size());
		System.out.println("Excess combination checks: " + unsuccessfulIterations);
		System.out.println("Percent of checks that were effective: " + (1f - (float)unsuccessfulIterations / totalIterations));
	}

	@Override
	public IMatch<T> getNextResource()
	{
		return synchronouslyAccessibleMatches.get(currentMatchGroup).get(matchIndexInGroup++).withMatchNumber(matchNumber++);
	}

	@Override
	public boolean hasResourceReady()
	{
		return currentMatchGroup < synchronouslyAccessibleMatches.size() &&  matchIndexInGroup < synchronouslyAccessibleMatches.get(currentMatchGroup).size();
	}

	@Override
	public boolean isFinished()
	{
		return currentMatchGroup == synchronouslyAccessibleMatches.size();
	}

	@Override
	public T concludeCompetition()
	{
		int mostWins = -1;
		T winner = null;
		
		for (int i = 0; i < wins.size(); ++i)
		{
			if (wins.get(i) == mostWins)
			{
				winner = null;
			}
			else if (wins.get(i) > mostWins)
			{
				mostWins = wins.get(i);
				winner = currentContenders.get(i);
			}
		}
		
		return winner;
	}
	
	private final class Match implements IMatch<T>
	{
		private final List<T> opponents;
		
		private int matchNumber;
		private T winner;
		
		public Match(List<T> opponents, int[] winMap)
		{
			this.opponents = opponents;
		}
		
		public Match withMatchNumber(int matchNumber)
		{
			this.matchNumber = matchNumber;
			return this;
		}
		
		@Override
		public List<? extends T> getOpponents()
		{
			return opponents;
		}
		
		@Override
		public int getMatchNumber()
		{
			return matchNumber;
		}
		
		@Override
		public void setWinner(T winner)
		{
			this.winner = winner;
		}
		
		@Override
		public T getWinner()
		{
			return winner;
		}
	}
	
	@Override
	public void returnResource(IMatch<T> match)
	{
		T winner = match.getWinner();
		
		if (winner != null)
		{
			Integer wins = winMap.get(winner);
			
			if (wins == null)
				winMap.put(winner, 1);
			else
				winMap.put(winner, wins + 1);
		}
		
		++resolvedMatchCount;
		
		if (resolvedMatchCount == synchronouslyAccessibleMatches.get(currentMatchGroup).size())
		{
			++currentMatchGroup;
			resolvedMatchCount = 0;
			matchIndexInGroup = 0;
		}
	}
	
	public static void main(String[] args)
	{
		final int contenderCount = 40, contendersPerRound = 2;
		
		List<Integer> contenders = new ArrayList<Integer>(contenderCount);
		
		for (int i = 0; i < contenderCount; ++i)
			contenders.add(i);

		
		MultithreadableCombinationWar<Integer> competition = new MultithreadableCombinationWar<Integer>(contendersPerRound);
		
		competition.begin(contenders, true);
	}
}
