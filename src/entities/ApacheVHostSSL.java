package entities;

import interfaces.*;

@VHostLinePattern(pattern = "Use\\s+(?<VHostDirective>\\w+)\\s+(?<VHostName>[\\w\\.\\-]+)\\s+(?<DocumentRoot>([\\w\\.\\-\\/_,]+|\"[\\w\\.\\-\\/_, ]+\"))\\s+(?<CertificatePath>([\\w\\.\\-\\/_,]+|\"[\\w\\.\\-\\/_, ]+\"))")
public class ApacheVHostSSL extends ApacheVHost
{
	public String CertificatePath;
}
