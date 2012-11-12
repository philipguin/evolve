package fitness;
import java.util.Comparator;


public interface IFitnessed
{
	/** Ideally, this should be as fast as possible-- this should behave as a field getter,
     * given the frequency at which this method will be called by owners of this interface. */
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
