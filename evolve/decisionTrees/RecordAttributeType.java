package decisionTrees;

import java.util.ArrayList;
import java.util.List;

public class RecordAttributeType implements DiscreteAttributeType<Record, Object>
{
	private final int typeIndex;
	private final List<Object> possibleValues;
	
	public RecordAttributeType(int typeIndex, List<?> possibleValues)
	{
		this.typeIndex = typeIndex;
		this.possibleValues = new ArrayList<Object>(possibleValues.size());
		this.possibleValues.addAll(possibleValues);
	}
	
	public List<Object> getPossibleValues() { return possibleValues; }
	
	public Object getValue(Record record)
	{
		//TODO: get around this?  make this safer?
		return record.getAttributes().get(typeIndex);
	}
}
