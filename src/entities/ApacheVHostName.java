package entities;

import interfaces.*;

public abstract class ApacheVHostName
{
	public String VHostName;
	
	public String toDirective()
	{
		return "Use " + this.getClass().getSimpleName() + " \"" + VHostName + "\"";
	}
}

