package decisionTrees;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DecisionTree<R, V>
{
	private static final class Node<R>
	{
		public final Object value;
		public Node<R>[] children = null;
		public DiscreteAttributeType<? super R, ?> testCriteria = null;

		public Node(Object value)
		{
			this.value = value;
		}
	}
	
	private final Node<R> root;
	public final List<DiscreteAttributeType<? super R, ?>> attributeTypes;
	public final DiscreteAttributeType<? super R, V> inQuestion;
	
	public DecisionTree(List<? extends R> records, List<? extends DiscreteAttributeType<? super R, ?>> attributeTypes, DiscreteAttributeType<? super R, V> inQuestion)
	{
		this.root = new Node<R>(null);
		
		this.attributeTypes = new ArrayList<DiscreteAttributeType<? super R, ?>>(attributeTypes.size());
		this.attributeTypes.addAll(attributeTypes);
		
		this.inQuestion = inQuestion;
		
		buildTree(root, records, new boolean[attributeTypes.size()]);
	}
	
	@SuppressWarnings("unchecked")
	private void buildTree(Node<R> relativeRoot, List<? extends R> records, boolean[] isTypeUnavailable)
	{
		double entropy = Entropy.calculateEntropy(records, inQuestion);
		
		if (entropy == 0)
			return;

		DiscreteAttributeType<? super R, ?> bestAttributeType = null;
		int bestPossibleValuesSize = -1;
 		double bestGain = 0;
		List<List<R>> bestSubsets = null;
		int bestTypeIndex = -1, typeIndex = 0;
		
		for (DiscreteAttributeType<? super R, ?> attributeType : attributeTypes)
		{
			if (isTypeUnavailable[typeIndex++])
				continue;
			
			List<Double> subEntropies = new ArrayList<Double>();
			List<List<R>> subsets = new ArrayList<List<R>>();
			Collection<?> possibleValues = attributeType.getPossibleValues();
			
			for (Object possibleValue : possibleValues)
			{
				List<R> subset = makeSubset(records, attributeType, possibleValue);
				subsets.add(subset);
				
				if (!subset.isEmpty())
					subEntropies.add(Entropy.calculateEntropy(subset, inQuestion));
			}
			
			double gain = Entropy.calculateGain(entropy, subEntropies, subsets, records.size());
			
			if (gain > bestGain)
			{
				bestAttributeType = attributeType;
				bestGain = gain;
				bestSubsets = subsets;
				bestTypeIndex = typeIndex;
				bestPossibleValuesSize = possibleValues.size();
			}
		}
		
		if (bestAttributeType != null)
		{
			relativeRoot.testCriteria = bestAttributeType;
			relativeRoot.children = new Node[bestPossibleValuesSize];
			
			isTypeUnavailable[bestTypeIndex] = true;
			
			for (int j = 0; j < bestPossibleValuesSize; ++j)
			{
				relativeRoot.children[j] = new Node<R>(j);
				buildTree(relativeRoot.children[j], bestSubsets.get(j), isTypeUnavailable);
			}
			
			isTypeUnavailable[bestTypeIndex] = false;
		}
	}
	
	private List<R> makeSubset(List<? extends R> records, DiscreteAttributeType<? super R, ?> attributeType, Object value)
	{
		List<R> subset = new ArrayList<R>();
		
		for (R record : records)
		{
			if (attributeType.getValue(record).equals(value))
				subset.add(record);
		}
		
		return subset;
	}

	public V predict(R record)
	{
		return recursivePredict(record, root);
	}
	
	@SuppressWarnings("unchecked")
	private V recursivePredict(R record, Node<R> node)
	{
		if (node.children == null)
			return (V)node.value;
		
		Object value = node.testCriteria.getValue(record);
		
		for (Node<R> child : node.children)
		{
			if (value.equals(child.value))
				return recursivePredict(record, child);
		}
		
		return null;
	}
}
