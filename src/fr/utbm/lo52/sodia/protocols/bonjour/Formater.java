package fr.utbm.lo52.sodia.protocols.bonjour;

import java.util.HashMap;
import java.util.Map;

import fr.utbm.lo52.sodia.logic.Contact;

public class Formater
{
	public enum Operation
	{
		GET,
		RET;
	}
	
	public int size;
	
	public Operation operation;
	
	public Class<?> type;
	
	public static final Map<Class<?>, Integer > paramsSize;
	static
	{
		paramsSize = new HashMap<Class<?>, Integer>();
		paramsSize.put(Contact.class, 3);
		
	}
	
	public static final String SEPARATOR = ";";
	
	public String[][] params;
	
	public String display;
	
	public Formater()
	{
		
	}
	
	public Formater(String display)
	{
		parse(display);
	}
	
	public void parse(String display)
	{
		this.display = display;
		String[] csv = display.split(";");
		if(csv.length > 1)
		{
			operation = Operation.valueOf(Operation.class, csv[0]);
		}
		if(csv.length > 2)
		{
			try
			{
				type = Class.forName(csv[1]);
			} catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(csv.length > 3)
		{
			size = Integer.parseInt(csv[2]);
			int paramSize = paramsSize.get(type);
			for(int i = 0; i< csv.length-3; i++)
			{
				params[(i-3)/paramSize][(i-3)%paramSize] = csv[i+3];
			}
		}
		
	}
	
	public String toString()
	{
		
		return "";
	}
}
