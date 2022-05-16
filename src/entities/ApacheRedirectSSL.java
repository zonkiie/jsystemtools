package entities;

import interfaces.*;

@VHostLinePattern(pattern = "^\\s*Use\\s+(?<VHostDirective>\\w+)\\s+(?<VHostName>[\\w\\.\\-]+)\\s+(?<RedirectTarget>[\\w\\.\\-\\/\\:]+)\\s+(?<RedirectType>\\d+)\\s+(?<CertificatePath>([\\w\\.\\-\\/_,]+|\"[\\w\\.\\-\\/_, ]+\"))")
public class ApacheRedirectSSL extends ApacheRedirect
{
	public String CertificatePath;
	
	public String toDirective() throws javax.naming.NamingException
	{
		return super.toDirective() + " \"" + CertificatePath + "\"";
	}
}
