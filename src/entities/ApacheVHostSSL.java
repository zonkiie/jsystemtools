package entities;

import interfaces.*;

@VHostLinePattern(pattern = "^\\s*Use\\s+(?<VHostDirective>\\w+)\\s+(?<VHostName>[\\w\\.\\-]+)\\s+(?<DocumentRoot>([\\w\\.\\-\\/_,]+|\"[\\w\\.\\-\\/_, ]+\"))\\s+(?<CertificatePath>([\\w\\.\\-\\/_,]+|\"[\\w\\.\\-\\/_, ]+\"))")
// @VHostLinePattern(pattern = "(?<CertificatePath>([\\w\\.\\-\\/_,]+|\"[\\w\\.\\-\\/_, ]+\"))")
public class ApacheVHostSSL extends ApacheVHost
{
	public String CertificatePath;

	public String toDirective() throws javax.naming.NamingException
	{
		return super.toDirective() + " \"" + CertificatePath + "\"";
	}
}
