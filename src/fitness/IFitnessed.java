package fitness;
import java.util.Comparator;


public interface IFitnessed
{
	/** 
	 *  Ideally, this should be a field getter-- otherwise, this needs to be fast,
	 *  given how frequently this will be called by users of the interface.<P>
	 *  
	 *  The number returned must be greater than (and not equal to) 0.
	 *  Otherwise, weird behavior and/or errors will result.
	 */
	public float getFitness();

	public static final Comparator<IFitnessed> fitnessComparator = new Comparator<IFitnessed>()
	{
		@Override
		public int compare(IFitnessed a, IFitnessed b)
		{
			return Float.compare(a.getFitness(), b.getFitness());
		}
	};
}
