package tests;

import interfaces.*;
import handlers.*;
import entities.*;
import utils.*;
import java.util.*;
import java.io.*;
import java.lang.*;
import org.apache.commons.lang3.builder.*;

@PublicCallable
public class TestApacheHttpdHandler
{
	@PublicCallable
	public String testHandler()
	{
		ApacheHttpdHandler httpdhandler = new ApacheHttpdHandler();
		httpdhandler.setVHostFile("/dev/shm/VhostFile.conf");
		try
		{
			String str = "Use ApacheVHost www.example.com /home/example/htdocs\nUse ApacheRedirect www2.example.com http://www.google.de\n";
			BufferedWriter writer = new BufferedWriter(new FileWriter(httpdhandler.getVHostFile()));
			writer.write(str);
			writer.close();
			{
				String line = "Use ApacheVHost www.example.com /home/example/htdocs";
				Object result = httpdhandler.parseLine(line);
				System.out.println(ToStringBuilder.reflectionToString(result));
			}
			{
				String line = "Use ApacheVHost www.example.com \"/home/example/htdocs 1/bla bla\"";
				Object result = httpdhandler.parseLine(line);
				System.out.println(ToStringBuilder.reflectionToString(result));
			}
			{
				String line = "Use ApacheRedirect www2.example.com http://www.google.de 301";
				Object result = httpdhandler.parseLine(line);
				System.out.println(ToStringBuilder.reflectionToString(result));
			}
			{
				String line = "Use ApacheRedirect www2.example.com http://www.google.de 301";
				ApacheVHostName result = httpdhandler.parseLine(line);
				System.out.println(ToStringBuilder.reflectionToString(result));
				System.out.println("Directive rebuilt:" + result.toDirective());
			}
			{
				String line = httpdhandler.getLineEntryForVHostName("www.example.com");
				System.out.println(line);
			}
			{
				ApacheVHostSSL entry = new ApacheVHostSSL();
				entry.VHostName = "support.example.com";
				entry.DocumentRoot = "/home/example/support/";
				entry.CertificatePath = "/etc/apache2/certificates/support.example.com.cert";
				httpdhandler.add(entry);
			}
			
		}
		catch(Exception ex)
		{
			System.err.println("Exception " + ex.getMessage());
		}
		return null;
	}
	
}

