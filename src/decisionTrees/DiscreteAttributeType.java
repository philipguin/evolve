package decisionTrees;

import java.util.List;

public interface DiscreteAttributeType<R, V>
{
	public List<V> getPossibleValues();
	public V getValue(R record);
}
