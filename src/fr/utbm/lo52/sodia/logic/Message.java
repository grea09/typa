package fr.utbm.lo52.sodia.logic;

public class Message
{
	private Mime type;
	private Object data;
	
	public Message(Mime type, Object data)
	{
		this.type = type;
		this.data = data;
	}

	public Mime type()
	{
		return type;
	}

	public void setType(Mime type)
	{
		this.type = type;
	}

	public Object data()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}
	
}
