package decisionTrees;

import java.util.Collection;
import java.util.List;

public abstract class Entropy
{	
	private Entropy() { }
	
	public static <R> double calculateEntropy(List<? extends R> data, DiscreteAttributeType<? super R, ?> relativeTo)
	{
		double entropy = 0;
		
		if (data.size() == 0)
			return 0;
		
		for (Object value : relativeTo.getPossibleValues())
		{
			int count = 0;
			for (R record : data)
			{
				if (relativeTo.getValue(record).equals(value))
					++count;
			}
				
			double probability = count / (double)data.size();
			
			if (count > 0)
				entropy -= probability * (Math.log(probability) / Math.log(2));
		}
		
		return entropy;
	}
	
	public static double calculateGain(double rootEntropy, List<Double> subEntropies, List<? extends Collection<?>> subsets, double data)
	{
		double gain = 0; 
		
		for (int i = 0; i < subEntropies.size(); ++i)
			gain -= subsets.get(i).size() * subEntropies.get(i);
		
		return rootEntropy + gain / data;
	}
}
