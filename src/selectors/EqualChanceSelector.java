package selectors;

import java.util.List;
import java.util.Random;


public class EqualChanceSelector<T> implements ISelector<T>
{
	private final Random random;
	
	public EqualChanceSelector(Random random)
	{
		this.random = random;
	}
	
	public final T select(List<? extends T> choices)
	{
		return choices.get(random.nextInt(choices.size()));
	}
}