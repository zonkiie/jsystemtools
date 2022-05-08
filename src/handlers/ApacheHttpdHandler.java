package handlers;

import interfaces.*;
import entities.*;
import utils.*;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.lang.*;
import java.lang.reflect.*;
import org.apache.commons.lang3.builder.*;

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
			{
				String line = "Use ApacheVHost www.example.com /home/example/htdocs";
				Object result = parseLine(line);
				System.out.println(ToStringBuilder.reflectionToString(result));
			}
			{
				String line = "Use ApacheVHost www.example.com \"/home/example/htdocs 1/bla bla\"";
				Object result = parseLine(line);
				System.out.println(ToStringBuilder.reflectionToString(result));
			}
			{
				String line = "Use ApacheRedirect www2.example.com http://www.google.de 301";
				Object result = parseLine(line);
				System.out.println(ToStringBuilder.reflectionToString(result));
			}
			
		}
		catch(Exception ex)
		{
			System.err.println("Exception " + ex.getMessage());
		}
		return null;
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
	
		try
		{
			/*Scanner scanner = new Scanner(line);
			scanner.findInLine("Use\\s+(\\w+)\\s+([\\w\\.\\-]+)\\s+([\\w\\.\\-\\/]+)\\s?(\\w+)?");
			MatchResult result = scanner.match();
			//result.groupCount()
			ApacheVHostName instance = (ApacheVHostName) Class.forName("entities." + result.group(1)).getDeclaredConstructor().newInstance();
			System.err.println("Class:" + instance.getClass());
			instance.getClass().getField("VHostName").set(instance, result.group(2));
			instance.getClass().getField("DocumentRoot").set(instance, result.group(3));
			scanner.close();*/
			ApacheVHostName instance = null;
			//Pattern usageLine = Pattern.compile("Use\\s+(?<VHostDirective>\\w+)\\s+(?<VHostName>[\\w\\.\\-]+)\\s+(?<DocumentRoot>[\\w\\.\\-\\/]+)\\s?(?<RedirectType>\\w+)?");
			Pattern usageLine = Pattern.compile("Use\\s+(?<VHostDirective>\\w+)");
			Matcher matcher = usageLine.matcher(line);
			
			if(matcher.find() && matcher.groupCount() >= 1)
			{
				instance = (ApacheVHostName) Class.forName("entities." + matcher.group("VHostDirective")).getDeclaredConstructor().newInstance();
				VHostLinePattern vhlp = instance.getClass().getAnnotation(VHostLinePattern.class);
				Pattern innerPattern = Pattern.compile(vhlp.pattern());
				Matcher innerMatcher = innerPattern.matcher(line);
				//System.err.println("InnerPattern:" + innerPattern.pattern());
				
				if(innerMatcher.find() && innerMatcher.groupCount() >= 2)
				{
					List<String> groupNames = RegexHelper.getRegexNamedGroups(innerPattern.pattern());
					Iterator<String> groupNamesIterator = groupNames.iterator();
					while(groupNamesIterator.hasNext())
					{
						String fieldName = groupNamesIterator.next();
						if(fieldName.equals("VHostDirective")) continue;
						if(ReflectionHelper.hasField(instance.getClass(), fieldName)) instance.getClass().getField(fieldName).set(instance, innerMatcher.group(fieldName));
					}
				}
			}
			return instance;
		}
		//catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException ex)
		catch(Exception ex)
		{
			System.err.println("Exception " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
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

