package competitions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class BinaryTournament<T> implements ICompetition<T>
{
	private final Random random;
	private List<? extends T> contenders;
	private List<T> winners;
	private int contenderIndex;
	private int currentRound;
	
	private final Match match = new Match();
	private List<? extends T> currentOpponents;
	private boolean matchReady = false;
	private T currentWinner;
	
	
	public BinaryTournament(Random random)
	{
		this.random = random;
	}

	private final void nextMatch()
	{
		if (contenderIndex >= contenders.size())
		{
			//End of tier
			contenders = winners;
			winners = new ArrayList<T>();
			contenderIndex = 0;
			
			if (contenders.size() <= 1)
			{
				//THE CHAMPION!!!
				return;
			}
		}
		
		prepareNextMatch();
	}

	@SuppressWarnings("unchecked")
	private final void prepareNextMatch()
	{
		if (contenderIndex + 2 > contenders.size())
			currentOpponents = Arrays.<T>asList(contenders.get(random.nextInt(contenders.size()-1)), contenders.get(contenders.size()-1)); //TODO: this stinks
		else
			currentOpponents = contenders.subList(contenderIndex, contenderIndex+2);
		
		contenderIndex += 2;
		matchReady = true;
	}
	
	@Override
	public void begin(List<? extends T> contenders)
	{
		this.contenders = contenders;
		winners = new ArrayList<T>();
		contenderIndex = 0;
		
		prepareNextMatch();
		currentRound = 0;
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
		return contenders.size() <= 1;
	}

	@Override
	public T concludeCompetition()
	{
		T winner = contenders.isEmpty() ? null : contenders.get(0);
		contenders = null;
		winners = null;
		return winner;
	}
	
	@Override
	public void returnResource(IMatch<T> match)
	{
		++currentRound;
		
		if (currentWinner != null && !winners.contains(currentWinner)) //TODO: slow....
			winners.add(currentWinner);
		
		currentWinner = null;
		
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