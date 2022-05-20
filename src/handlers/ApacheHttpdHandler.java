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
			{
				String line = "Use ApacheRedirect www2.example.com http://www.google.de 301";
				ApacheVHostName result = parseLine(line);
				System.out.println(ToStringBuilder.reflectionToString(result));
				System.out.println("Directive rebuilt:" + result.toDirective());
			}
			{
				String line = getLineEntryForVHostName("www.example.com");
				System.out.println(line);
			}
			{
				ApacheVHostSSL entry = new ApacheVHostSSL();
				entry.VHostName = "support.example.com";
				entry.DocumentRoot = "/home/example/support/";
				entry.CertificatePath = "/etc/apache2/certificates/support.example.com.cert";
				add(entry);
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
	
	private String getVHostLinePattern(Class clazz)
	{
		List<Class> inheritanceList = (new ClassInheritance()).getClassTree(clazz);
		Collections.reverse(inheritanceList);
		String attrString = "";
		for(Class singleClazz: inheritanceList)
		{
			System.err.println("Class:" + singleClazz);
			java.lang.annotation.Annotation an = singleClazz.getAnnotation(VHostLinePattern.class);
			System.err.println("Annotation:" + an);
			//VHostLinePattern vhlp = singleClazz.getAnnotation(VHostLinePattern.class);
			//VHostLinePattern vhlp = inheritanceList.get(i).getAnnotation(VHostLinePattern.class);
			//attrString += inheritanceList.get(i).getDeclaredAnnotation(VHostLinePattern.class)).pattern() + (i > 1?"\\s+":"");
		}
		return attrString;
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
			Matcher matcher = Pattern.compile("^\\s*Use\\s+(?<VHostDirective>\\w+)").matcher(line);
			
			if(matcher.find() && matcher.groupCount() >= 1)
			{
				instance = (ApacheVHostName) Class.forName("entities." + matcher.group("VHostDirective")).getDeclaredConstructor().newInstance();
				VHostLinePattern vhlp = instance.getClass().getAnnotation(VHostLinePattern.class);
				String vp = getVHostLinePattern(instance.getClass());
				Matcher innerMatcher = Pattern.compile(vhlp.pattern()).matcher(line);
				//System.err.println("InnerPattern:" + innerPattern.pattern());
				
				if(innerMatcher.find() && innerMatcher.groupCount() >= 2)
				{
					List<String> groupNames = RegexHelper.getRegexNamedGroups(innerMatcher.pattern().pattern());
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
	
	private String getLineEntryForVHostName(String vhostName) throws IOException
	{
		List<String> lines = loadVHostFileLines();
		String vhpattern = ApacheVHostName.class.getAnnotation(VHostLinePattern.class).pattern();
		for(String line: lines)
		{
			Matcher matcher = Pattern.compile(vhpattern).matcher(line);
			
			if(matcher.find() && matcher.groupCount() >= 2)
			{
				if(matcher.group("VHostName").equals(vhostName)) return line;
			}
		}
		return null;
	}
	
	private ApacheVHostName getVhostEntry(ApacheVHostName vhost) throws IOException
	{
		return getVhostEntry(vhost.VHostName);
	}
	
	private ApacheVHostName getVhostEntry(String vhostName) throws IOException
	{
		String line = getLineEntryForVHostName(vhostName);
		return parseLine(line);
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
		try
		{
			//ApacheVHostName entry = (ApacheVHostName) Class.forName("entities." + o.getClass().getSimpleName());
			BufferedWriter writer = new BufferedWriter(new FileWriter(this.VHostFile, true));
			writer.write(o.toDirective() + "\n");
			writer.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
		return this;
	}
	
	public CRUDLS<ApacheVHostName> add(List<ApacheVHostName> o)
	{
		return this;
	}
	
	public CRUDLS<ApacheVHostName> set(ApacheVHostName o)
	{
		try
		{
			List<String> lines = loadVHostFileLines();
			String vhpattern = ApacheVHostName.class.getAnnotation(VHostLinePattern.class).pattern();
			StringBuffer str = new StringBuffer("");
			//for(String line: lines)
			for(int i = 0; i < lines.size(); i++)
			{
				String line = lines.get(i);
				Matcher matcher = Pattern.compile(vhpattern).matcher(line);
				
				if(matcher.find() && matcher.groupCount() >= 2)
				{
					if(matcher.group("VHostName").equals(o.VHostName))
					{
						String newLine = o.toDirective();
						lines.set(i, newLine);
					}
				}
			}
			for(String line: lines) str.append(line).append("\n");
			BufferedWriter writer = new BufferedWriter(new FileWriter(this.VHostFile));
			writer.write(str.toString());
			writer.close();
			return this;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public CRUDLS<ApacheVHostName> set(List<ApacheVHostName> vhlist)
	{
		try
		{
			List<String> lines = loadVHostFileLines();
			String vhpattern = ApacheVHostName.class.getAnnotation(VHostLinePattern.class).pattern();
			StringBuffer str = new StringBuffer("");
			
			for(int i = 0; i < lines.size(); i++)
			{
				String line = lines.get(i);
				Matcher matcher = Pattern.compile(vhpattern).matcher(line);
				if(matcher.find() && matcher.groupCount() >= 2)
				{
					for(ApacheVHostName entry: vhlist)
					{
						if(matcher.group("VHostName").equals(entry.VHostName))
						{
							String newLine = entry.toDirective();
							lines.set(i, newLine);
						}
					}
				}
			}
			for(String line: lines) str.append(line).append("\n");
			BufferedWriter writer = new BufferedWriter(new FileWriter(this.VHostFile));
			writer.write(str.toString());
			writer.close();
			return this;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public CRUDLS<ApacheVHostName> rename(String oldName, String newName)
	{
		return this;
	}
	
	public ApacheVHostName get(String vhostName)
	{
		try
		{
			return parseLine(getLineEntryForVHostName(vhostName));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public ApacheVHostName get(ApacheVHostName vhost)
	{
		return get(vhost.VHostName);
		//return new ApacheVHost();
	}

	public List<ApacheVHostName> list()
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(VHostFile));
			List<ApacheVHostName> lines = new Vector<ApacheVHostName>();
			String line = null;
			while((line = reader.readLine()) != null) lines.add(parseLine(line));
			return lines;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public List<ApacheVHostName> search(ApacheVHostName o)
	{
		return new Vector<ApacheVHostName>();
	}

	public CRUDLS<ApacheVHostName> delete(ApacheVHostName vhn)
	{
		return this;
	}
	
	public CRUDLS<ApacheVHostName> delete(String vhn)
	{
		return this;
	}
	
	public CRUDLS<ApacheVHostName> delete(List<ApacheVHostName> vhn)
	{
		return this;
	}
}

