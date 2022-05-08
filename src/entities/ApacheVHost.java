package entities;

import interfaces.*;

@VHostLinePattern(pattern = "Use\\s+(?<VHostDirective>\\w+)\\s+(?<VHostName>[\\w\\.\\-]+)\\s+(?<DocumentRoot>([\\w\\.\\-\\/_,]+|\"[\\w\\.\\-\\/_, ]+\"))")
public class ApacheVHost extends ApacheVHostName
{
	public String DocumentRoot;
}
