package entities;

import interfaces.*;

@VHostLinePattern(pattern = "^\\s*Use\\s+(?<VHostDirective>\\w+)\\s+(?<VHostName>[\\w\\.\\-]+)\\s+(?<RedirectTarget>[\\w\\.\\-\\/\\:]+)\\s+(?<RedirectType>\\d+)")
//@VHostLinePattern(pattern = "(?<RedirectTarget>[\\w\\.\\-\\/\\:]+)\\s+(?<RedirectType>\\d+)")
public class ApacheRedirect extends ApacheVHostName
{
	public String RedirectTarget;
	public String RedirectType;
	
	public String toDirective() throws javax.naming.NamingException
	{
		return super.toDirective() + " \"" + RedirectTarget + "\" " + RedirectType;
	}
}
