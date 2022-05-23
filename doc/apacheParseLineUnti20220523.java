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
			//Matcher matcher = Pattern.compile("^\\s*Use\\s+(?<VHostDirective>\\w+)").matcher(line);
			String basePattern = ((VHostLinePattern)Class.forName("entities.ApacheVHostName").getAnnotation(VHostLinePattern.class)).pattern();
			Matcher matcher = Pattern.compile(basePattern).matcher(line);
			
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
						//if(ReflectionHelper.hasField(instance.getClass(), fieldName)) instance.getClass().getField(fieldName).set(instance, innerMatcher.group(fieldName));
						if(ReflectionHelper.hasField(instance.getClass(), fieldName))
						{
							StringBuilder value = new StringBuilder(innerMatcher.group(fieldName));
							if(value.length() > 0 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') value = value.deleteCharAt(value.length() - 1).deleteCharAt(0);
							instance.getClass().getField(fieldName).set(instance, value.toString());
						}
					}
				}
			}
			else
			{
				System.err.println("No match found!");
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
	
 
