package reproducers;
import java.util.List;

import spawners.IMater;


public interface IReproducer<T>
{
	public List<T> makeNextGeneration(List<T> population, IMater<T> mater);
}
