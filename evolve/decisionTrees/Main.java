package decisionTrees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Main
{
	public static int NUM_ATTRS = 5;
	private static Map<Integer, String> attributeNameMap = new HashMap<Integer, String>();

	static
	{
		attributeNameMap.put(0, "Outlook");
		attributeNameMap.put(1, "Temperature");
		attributeNameMap.put(2, "Humidity");
		attributeNameMap.put(3, "Wind");
		attributeNameMap.put(4, "PlayTennis");
	}
	
	public static void main(String[] args)
	{
		ArrayList<Record> records = FileReader.buildRecords("playtennis.data");
		
		List<RecordAttributeType> attributeTypes = new ArrayList<RecordAttributeType>(NUM_ATTRS);
		
		for (int i = 0; i < NUM_ATTRS; ++i)
			attributeTypes.add(new RecordAttributeType(i, getAttributeNames(i)));
		
		DecisionTree<Record, Object> tree = new DecisionTree<Record, Object>(records, attributeTypes, attributeTypes.get(attributeTypes.size() - 1));
		tree.predict(records.get(12));
	}
	
	public static String getAttributeTypeName(int attribute)
	{
		return attributeNameMap.get(attribute);
	}
	
	public static List<String> getAttributeNames(int attributeTypeIndex)
	{
		switch (attributeTypeIndex)
		{
		case 0: return Arrays.asList("Sunny", "Overcast", "Rain");
		case 1: return Arrays.asList("Hot", "Mild", "Cool");
		case 2: return Arrays.asList("High", "Normal");
		case 3: return Arrays.asList("Weak", "Strong");
		case 4: return Arrays.asList("Yes", "No");
		}
		
		return null;
	}
}
