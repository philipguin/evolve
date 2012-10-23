package spawners;

import java.util.List;

public class NullSpawner<T> implements ICreator<T>, IMater<T>
{
	public final T create()
	{
		return null;
	}
	
	public final T mate(List<? extends T> parents)
	{
		return null;
	}
}
