package speciators;


public interface ISpeciator<T>
{
	/**
	 * This method returns a value between 0 and 1 (inclusive) representing how similar the organisms are to each other.
	 * '1' means they're exactly the same, '0' means they're as distinct as possible.<P>
	 * 
	 * This number is interpretive, and it's the implementor's job to define what 'similarity' is for the caller.
	 * Should similarity be based on genotype or phenotype?  Etc.<P>
	 * 
	 * It is required that the similarity between an individual and itself is always 1.
	 * 
	 * @param evolvees to be tested for similarity.
	 * @return A value between 0 and 1 representing the evolvees' similarity.
	 * */
	public float getSimilarity(T a, T b);
}
