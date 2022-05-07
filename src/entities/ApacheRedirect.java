package entities;

import interfaces.*;

@VHostLinePattern(pattern = "Use\\s+(?<VHostDirective>\\w+)\\s+(?<VHostName>[\\w\\.\\-]+)\\s+(?<RedirectTarget>[\\w\\.\\-\\/]+)\\s+(?<RedirectType>\\d+)")
public class ApacheRedirect extends ApacheVHostName
{
	public String RedirectTarget;
	public Integer RedirectType;
}
