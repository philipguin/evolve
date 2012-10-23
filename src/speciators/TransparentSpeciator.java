package speciators;

import util.IMapping;

public class TransparentSpeciator<B, T> implements ISpeciator<B>
{
	private final ISpeciator<? super T> speciator;
	private final IMapping<? super B, ? extends T> mapping;
	
	public TransparentSpeciator(IMapping<? super B, ? extends T> mapping, ISpeciator<? super T> speciator)
	{
		this.mapping = mapping;
		this.speciator = speciator;
	}
	
	@Override
	public float getSimilarity(B a, B b)
	{
		return speciator.getSimilarity(mapping.map(a), mapping.map(b));
	}
}
