package competitions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.MathHelper;


public class CombinationWar<T> implements ICompetition<T>
{
	public final int contendersPerRound;
	
	private int currentRound = -1, roundCount;

	private List<? extends T> contenders;
	private final int[] currentOpponentIndices;
	
	private final Match match = new Match();
	private boolean matchReady = false;
	private final List<T> currentOpponents;

	private final HashMap<T, Integer> winMap = new HashMap<T, Integer>();
	private T currentWinner;
	
	public CombinationWar(int contendersPerRound)
	{
		this.contendersPerRound = contendersPerRound;
		currentOpponentIndices = new int[contendersPerRound];
		currentOpponents = new ArrayList<T>(contendersPerRound);
	}

	private final void nextMatch()
	{
		if (++currentRound == roundCount)
			return;
		
		currentOpponents.clear();
		
		for (int i : currentOpponentIndices)
			currentOpponents.add(contenders.get(i));
		
		matchReady = true;
		
		/*System.out.print("Round " + currentRound + ": ");

		for (int k = 0; k < currentContenderIndices.length; ++k)
			System.out.print(currentContenderIndices[k] + " ");
		
		System.out.println();*/
	}
	
	@Override
	public void begin(List<? extends T> contenders)
	{
		this.contenders = contenders;
		roundCount = MathHelper.getBinomialCoefficient(contenders.size(), contendersPerRound);
		
		if (roundCount == 0)
			return;
		
		winMap.clear();
		
		for (int i = 0; i < contendersPerRound; ++i)
			currentOpponentIndices[i] = i;
		
		nextMatch();
	}

	private final void updateContenderIndex(int i)
	{
		++currentOpponentIndices[i];
		
		if (currentOpponentIndices[i] >= contenders.size() - contendersPerRound + i + 1 && i > 0)
		{
			updateContenderIndex(i-1);
			currentOpponentIndices[i] = currentOpponentIndices[i-1] + 1;
		}
	}

	@Override
	public IMatch<T> getNextResource()
	{
		if (!matchReady)
			return null;
		
		matchReady = false;
		return match;
	}
	
	@Override
	public boolean hasResourceReady()
	{
		return matchReady;
	}
	
	@Override
	public boolean isFinished()
	{
		return currentRound == roundCount;
	}

	@Override
	public T concludeCompetition()
	{
		int mostWins = -1;
		T winner = null;
		
		for (T contender : contenders)
		{
			int contenderWins = winMap.get(contender);
			
			if (contenderWins == mostWins)
			{
				winner = null;
			}
			else if (contenderWins > mostWins)
			{
				mostWins = contenderWins;
				winner = contender;
			}
		}
		
		currentRound = -1;
		return winner;
	}
	
	@Override
	public void returnResource(IMatch<T> match)
	{
		if (currentWinner != null)
		{
			Integer wins = winMap.get(currentWinner);
			
			if (wins == null)
				winMap.put(currentWinner, 1);
			else
				winMap.put(currentWinner, wins + 1);
			
			currentWinner = null;
		}
		
		updateContenderIndex(contendersPerRound - 1);
		
		nextMatch();
	}
	
	private final class Match implements IMatch<T>
	{
		@Override
		public List<? extends T> getOpponents()
		{
			return currentOpponents;
		}
		
		@Override
		public int getMatchNumber()
		{
			return currentRound;
		}
		
		@Override
		public void setWinner(T winner)
		{
			currentWinner = winner;
		}
		
		@Override
		public T getWinner()
		{
			return currentWinner;
		}
	}
}
