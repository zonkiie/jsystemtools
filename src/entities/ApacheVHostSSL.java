package entities;

import interfaces.*;

@VHostLinePattern(pattern = "Use\\s+(?<VHostDirective>\\w+)\\s+(?<VHostName>[\\w\\.\\-]+)\\s+(?<DocumentRoot>[\\w\\.\\-\\/]+)\\s+(?<CertificatePath>[\\w\\.\\-\\/]+)")
public class ApacheVHostSSL extends ApacheVHost
{
	public String CertificatePath;
}
