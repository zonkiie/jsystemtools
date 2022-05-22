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
	
	public String getVHostLinePattern(Class clazz)
	{
		List<Class> inheritanceList = (new ClassInheritance()).getClassTree(clazz);
		Collections.reverse(inheritanceList);
		String attrString = "";
		for(Class singleClazz: inheritanceList)
		{
			attrString += ((VHostLinePattern)singleClazz.getAnnotation(VHostLinePattern.class)).pattern() + "\\s+";
		}
		if(attrString.endsWith("\\s+")) attrString = attrString.substring(0, attrString.length() - "\\s+".length());
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
				//VHostLinePattern vhlp = instance.getClass().getAnnotation(VHostLinePattern.class);
				//String vp = getVHostLinePattern(instance.getClass());
				//System.err.println("Pattern:" + vp);
				Matcher innerMatcher = Pattern.compile(getVHostLinePattern(instance.getClass())).matcher(line);
				//Matcher innerMatcher = Pattern.compile(vhlp.pattern()).matcher(line);
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
	
	public String getLineEntryForVHostName(String vhostName) throws IOException
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
	
	public ApacheVHostName getVhostEntry(ApacheVHostName vhost) throws IOException
	{
		return getVhostEntry(vhost.VHostName);
	}
	
	public ApacheVHostName getVhostEntry(String vhostName) throws IOException
	{
		String line = getLineEntryForVHostName(vhostName);
		return parseLine(line);
	}
	
	private Class getVhostClass(ApacheVHostName o)
	{
		return null;
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
	
	public CRUDLS<ApacheVHostName> add(List<ApacheVHostName> vhostList)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(this.VHostFile, true));
			for(ApacheVHostName entry: vhostList) writer.write(entry.toDirective() + "\n");
			writer.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
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
		try
		{
			List<String> lines = loadVHostFileLines();
			BufferedWriter writer = new BufferedWriter(new FileWriter(getVHostFile()));
			for(String line: lines)
			{
				ApacheVHostName entry = parseLine(line);
				if(entry.VHostName.equals(oldName))
				{
					entry.VHostName = newName;
					line = entry.toDirective() + "\n";
				}
				writer.write(line);
			}
			writer.close();
			return this;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public CRUDLS<ApacheVHostName> rename(ApacheVHostName oldObject, ApacheVHostName renamedObject)
	{
		try
		{
			List<String> lines = loadVHostFileLines();
			BufferedWriter writer = new BufferedWriter(new FileWriter(getVHostFile()));
			for(String line: lines)
			{
				ApacheVHostName entry = parseLine(line);
				if(entry.VHostName.equals(oldObject.VHostName) && entry.getClass().getName().equals(oldObject.getClass().getName()))
				{
					line = renamedObject.toDirective() + "\n";
				}
				writer.write(line);
			}
			writer.close();
			return this;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
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
		try
		{
			List<String> lines = loadVHostFileLines();
			for(String line: lines)
			{
				ApacheVHostName entry = parseLine(line);
				if(entry.VHostName.equals(vhost.VHostName) && entry.getClass().getName().equals(vhost.getClass().getName())) return entry;
			}
			// not found
			return null;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
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
		try
		{
			List<String> lines = loadVHostFileLines();
			BufferedWriter writer = new BufferedWriter(new FileWriter(getVHostFile()));
			for(String line: lines)
			{
				ApacheVHostName entry = parseLine(line);
				if(entry.VHostName.equals(vhn.VHostName) && entry.getClass().getName().equals(vhn.getClass().getName())) continue;
				writer.write(line);
			}
			writer.close();
			return this;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public CRUDLS<ApacheVHostName> delete(String vhn)
	{
		try
		{
			List<String> lines = loadVHostFileLines();
			BufferedWriter writer = new BufferedWriter(new FileWriter(getVHostFile()));
			for(String line: lines)
			{
				ApacheVHostName entry = parseLine(line);
				if(entry.VHostName.equals(vhn)) continue;
				writer.write(line);
			}
			writer.close();
			return this;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public CRUDLS<ApacheVHostName> delete(List<ApacheVHostName> vhostList)
	{
		try
		{
			List<String> lines = loadVHostFileLines();
			BufferedWriter writer = new BufferedWriter(new FileWriter(getVHostFile()));
			for(String line: lines)
			{
				ApacheVHostName entry = parseLine(line);
				boolean found = false;
				for(ApacheVHostName gentry: vhostList) {
					if(entry.VHostName.equals(gentry.VHostName) && entry.getClass().getName().equals(gentry.getClass().getName())) found = true;
				}
				if(found) continue;
				writer.write(line);
			}
			writer.close();
			return this;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
}

