package entities;

import interfaces.*;

@VHostLinePattern(pattern = "Use\\s+(?<VHostDirective>\\w+)\\s+(?<VHostName>[\\w\\.\\-]+)\\s+(?<RedirectTarget>[\\w\\.\\-\\/\\:]+)\\s+(?<RedirectType>\\d+)\\s+(?<CertificatePath>[\\w\\.\\-\\/]+)")
public class ApacheRedirectSSL extends ApacheRedirect
{
	public String CertificatePath;
}
