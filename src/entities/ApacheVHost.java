package entities;

import interfaces.*;

@VHostLinePattern(pattern = "^\\s*Use\\s+(?<VHostDirective>\\w+)\\s+(?<VHostName>[\\w\\.\\-]+)\\s+(?<DocumentRoot>([\\w\\.\\-\\/_,]+|\"[\\w\\.\\-\\/_, ]+\"))")
public class ApacheVHost extends ApacheVHostName
{
	public String DocumentRoot;
	
	public String toDirective() throws javax.naming.NamingException
	{
		return super.toDirective() + " \"" + DocumentRoot + "\"";
	}
}
