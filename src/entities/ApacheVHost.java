package entities;

import interfaces.*;

@VHostLinePattern(pattern = "Use\\s+(?<VHostDirective>\\w+)\\s+(?<VHostName>[\\w\\.\\-]+)\\s+(?<DocumentRoot>[\\w\\.\\-\\/]+)")
public class ApacheVHost extends ApacheVHostName
{
	public String DocumentRoot;
}
