	public ApacheVHostName parseLine(String line) 
	{
		//, vhostName, target, type;
		new Scanner(line).findAll("Use\\s+(\\w+)\\s+([\\w\\.\\-]+)\\s+([\\w\\.\\-\\/]+)\\s?(\\w+)?").forEach(result -> {
			try
			{
				for (int i=1; i<=result.groupCount(); i++) System.err.println(result.group(i));
				
				String className = "entities." + result.group(1);
				System.err.println("ClassName:" + className);
				
				//Class clazz = Class.forName(className);
				
				ApacheVHostName instance = (ApacheVHostName) Class.forName("entities." + result.group(1)).getDeclaredConstructor().newInstance();
				//instance = Class.forName(className).getDeclaredConstructor().newInstance();
				/*Field fieldVhostName = instance.getClass().getDeclaredField("VHostName");
				fieldVhostName.set(instance, result.group(1));*/
				/*String typeName = result.group(1);
				String vhostName = result.group(2);
				String target = result.group(3);
				String type = result.group(4);*/
				return instance;
			}
			catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex)
			{
				System.err.println("Exception " + ex.getMessage());
			}
		});
		
		//return instance;
		return null;
	}
