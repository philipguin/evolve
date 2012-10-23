package fitness;
import java.util.Comparator;


public interface IFitnessed
{
	/** Ideally, this should be a field getter-- otherwise, this needs to be fast,
	 *  given how frequently this will likely be called by users of the interface. */
	public float getFitness();

	public static final Comparator<IFitnessed> fitnessComparator = new Comparator<IFitnessed>()
	{
		@Override
		public int compare(IFitnessed o1, IFitnessed o2)
		{
			return Float.compare(o1.getFitness(), o2.getFitness());
		}
	};
}
