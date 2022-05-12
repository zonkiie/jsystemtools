package entities;

import interfaces.*;

public abstract class ApacheVHostName
{
	public String VHostName;
	
	public String toDirective() throws javax.naming.NamingException
	{
		if(getClass().getSimpleName().equals("")) throw new javax.naming.NamingException("Cannot determine class!");
		return "Use " + getClass().getSimpleName() + " \"" + VHostName + "\"";
	}
}

