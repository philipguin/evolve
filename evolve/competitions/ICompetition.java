package competitions;
import java.util.List;

import processing.IResourceProvider;


/**
 * Implementors of this interface are stateful, lightweight "competition" processors.
 * Users of this interface should call {@code onStart}, followed by repeatedly calling
 * {@code update} so long as {@code isUnfinished} is true.  Then, {@code onFinish}
 * is called, which returns the winner (null on ties).
 * */
public interface ICompetition<T> extends IResourceProvider<ICompetition.IMatch<T>>
{
	/** Begins execution of the competition.  This may be called mid-execution to begin a new competition (implementors beware...).*/
	public void begin(List<? extends T> contenders);
	
	public boolean isFinished();
	
	/**
	 * Returns the victor and "cleans up" the competition, thus preserving our lightweight status.
	 * @return The winner, or null on ties.
	 * @throws Error Thrown when the competition has yet to finish (check with {@code isFinished()})
	 * */
	public T concludeCompetition();
	
	public static interface IMatch<T>
	{
		public List<? extends T> getOpponents();
		public int getMatchNumber();
		public void setWinner(T winner);
		public T getWinner();
	}
}
