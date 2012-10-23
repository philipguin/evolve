package decisionTrees;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public abstract class FileReader
{
    public static ArrayList<Record> buildRecords(String filePath) 
    {
		ArrayList<Record> records = new ArrayList<Record>();

        try
        { 
           File f = new File(filePath);
           FileInputStream fis = new FileInputStream(f); 
           BufferedReader reader = new BufferedReader(new InputStreamReader(fis));;
           
           // read the first record of the file
           String line;
           
           while ((line = reader.readLine()) != null)
           {
              StringTokenizer st = new StringTokenizer(line, ",");
              List<Object> attributes = new ArrayList<Object>();
              Record r = new Record();
              
              if (Main.NUM_ATTRS != st.countTokens())
              {
            	  throw new Exception("Unknown number of attributes!");
              }
              	
			  String outlook = st.nextToken();
			  String temperature = st.nextToken();
			  String humidity = st.nextToken();
			  String wind = st.nextToken();
			  String playTennis = st.nextToken();
			  
			  if (outlook.equalsIgnoreCase("overcast") || outlook.equalsIgnoreCase("sunny") || outlook.equalsIgnoreCase("rain"))
				  attributes.add(outlook);
			  
			  if (temperature.equalsIgnoreCase("hot") || temperature.equalsIgnoreCase("mild") || temperature.equalsIgnoreCase("cool"))
				  attributes.add(temperature);
			  
			  if (humidity.equalsIgnoreCase("high") || humidity.equalsIgnoreCase("normal"))
				  attributes.add(humidity);
			  
			  if (wind.equalsIgnoreCase("weak") || wind.equalsIgnoreCase("strong"))
				  attributes.add(wind);
			  
			  if (playTennis.equalsIgnoreCase("no") || playTennis.equalsIgnoreCase("yes"))
				  attributes.add(playTennis);
			    		    
			  r.setAttributes(attributes);
			  records.add(r);
           }

        } 
        catch (IOException e)
        { 
           System.out.println("Uh oh, got an IOException error: " + e.getMessage()); 
        } 
        catch (Exception e)
        {
            System.out.println("Uh oh, got an Exception error: " + e.getMessage()); 
        }

		return records;
	}
}
