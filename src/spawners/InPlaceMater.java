package spawners;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public final class InPlaceMater<T> implements IMater<T[]>
{
	private final Class<T> clazz;
	private final Random random;
	private final ICreator<T> creator;
	private final IMater<T> mater;
	private final float chanceToSpawnNew;
	
	public InPlaceMater(Class<T> clazz, Random random, IMater<T> mater)
	{
		this(clazz, random, mater, null, 0f);
	}
	
	public InPlaceMater(Class<T> clazz, Random random, IMater<T> mater, ICreator<T> creator, float chanceToSpawnNew)
	{
		this.clazz = clazz;
		this.random = random;
		this.mater = mater;
		this.creator = creator;
		this.chanceToSpawnNew = chanceToSpawnNew;
	}
	
	/**
	 * Mates each attachment in place-- iow, each parent's attachments may only mate with other parent's attachments that share the same index.
	 * This is a necessity if the neural network inputting to the attachments isn't mutable.
	 * 
	 * @param parents - Are assumed to be of same length.
	 * */
	@Override
	public T[] mate(final List<? extends T[]> parents)
	{
		@SuppressWarnings("unchecked")
		T[] result = (T[])Array.newInstance(clazz, parents.get(0).length);
		
		List<T> subs = new ArrayList<T>(parents.size());
		
		for (int i = 0; i < result.length; ++i)
		{
			if (random.nextFloat() < chanceToSpawnNew)
			{
				result[i] = creator.create();
			}
			else
			{
				for (T[] a : parents)
					subs.add(a[i]);
				
				result[i] = mater.mate(subs);
				subs.clear();
			}
		}
		
		return result;
	}
}