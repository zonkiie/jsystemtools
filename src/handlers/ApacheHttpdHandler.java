package handlers;

import interfaces.*;
import entities.*;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.lang.reflect.*;

@PublicCallable
public class ApacheHttpdHandler implements CRUDLS<ApacheVHostName>, Handler
{
	private List<Class> VHostTypes = List.of(ApacheRedirect.class, ApacheRedirectSSL.class, ApacheVHost.class, ApacheVHostSSL.class);
	private String VHostFile = null;
	
	@PublicCallable
	public String info()
	{
		return "Handles HTTP Configuration";
	}
	
	@PublicCallable
	public String help()
	{
		return "Handles HTTP Configuration";
	}
	
	@PublicCallable
	public String echo2(String str1, String str2)
	{
		return "Hello " + str1 + ", " + str2;
	}
	
	@PublicCallable
	public String testHandler()
	{
		this.VHostFile = "/dev/shm/VhostFile.conf";
		try
		{
			String str = "Use ApacheVHost www.example.com /home/example/htdocs\nUse ApacheRedirect www2.example.com http://www.google.de\n";
			BufferedWriter writer = new BufferedWriter(new FileWriter(this.VHostFile));
			writer.write(str);
			writer.close();
			
		}
		catch(Exception ex)
		{
			System.err.println("Exception " + ex.getMessage());
			return null;
		}
	}
	
	public String getVHostFile()
	{
		return VHostFile;
	}

	public CRUDLS<ApacheVHostName> setVHostFile(String VHostFile)
	{
		this.VHostFile = VHostFile;
		return this;
	}

	public String loadVHostFile() throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(VHostFile));
		StringBuilder resultStringBuilder = new StringBuilder();
		String line = null;
		while((line = reader.readLine()) != null) resultStringBuilder.append(line).append("\n");
		return resultStringBuilder.toString();
	}

	public List<String> loadVHostFileLines() throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(VHostFile));
		List<String> lines = new Vector<String>();
		String line = null;
		while((line = reader.readLine()) != null) lines.add(line);
		return lines;
	}

	public List<ApacheVHostName> loadAndParseVHostFile() throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(VHostFile));
		List<ApacheVHostName> lines = new Vector<ApacheVHostName>();
		String line = null;
		while((line = reader.readLine()) != null) lines.add(parseLine(line));
		return lines;
	}

	public ApacheVHostName parseLine(String line)
	{
		//Scanner s = new Scanner(input);
		ApacheVHostName instance = null;
		new Scanner(line).findAll("Use (\\s+) (\\s+) (\\s+) ?(\\s+)?").forEach(result -> {
			String className = "entities." + result.group(1);
			System.err.println("ClassName:" + className);
			return className;
			/*instance = Class.forName("entities." + result.group(1)).getDeclaredConstructor().newInstance();
			Field fieldVhostName = instance.getClass().getDeclaredField("VHostName");
			fieldVhostName.set(instance, result.group(1));*/
            /*String typeName = result.group(1);
            String vhostName = result.group(2);
            String target = result.group(3);
            String type = result.group(4);*/
        });
		return instance;
	}

	private String getLineEntryForVHostName(ApacheVHostName o)
	{
		return null;
	}
	
	private String setLineEntryForVHost(ApacheVHostName o)
	{
		return null;
	}

	private Class getVhostClass(ApacheVHostName o)
	{
		return null;
	}

	public CRUDLS<ApacheVHostName> convert(ApacheVHostName o, Class clazz)
	{
		return this;
	}

	public CRUDLS<ApacheVHostName> add(ApacheVHostName o)
	{
		return this;
	}
	
	public CRUDLS<ApacheVHostName> set(ApacheVHostName o)
	{
		return this;
	}
	
	public ApacheVHostName get(ApacheVHostName o)
	{
		return new ApacheVHost();
	}

	public List<ApacheVHostName> list()
	{
		return new Vector<ApacheVHostName>();
	}
	
	public List<ApacheVHostName> search(ApacheVHostName o)
	{
		return new Vector<ApacheVHostName>();
	}

	public CRUDLS<ApacheVHostName> delete(ApacheVHostName vhn)
	{
		return this;
	}
}

