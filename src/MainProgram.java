import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.stream.*;
import entities.*;
import handlers.*;
import interfaces.*;
import utils.*;
import org.apache.commons.cli.*;

public class MainProgram
{
	private static List<Class> ModuleList = List.of(ApacheHttpdHandler.class);
	
	public static String ls(String dir) throws Exception
	{
		ExecutorReturn executorReturn = SimpleExecutor.execute("ls", "-la", dir);
		System.err.println("Return Code is " + executorReturn.returnCode);
		return executorReturn.toString();
	}
	
	public static void main(String [] args)
	{
		try
		{
			final CommandLineParser parser = new DefaultParser();
			final Options options = new Options();

			String ConfigFile = null, Module = null, VHostFile = null;

			//options.addOption("help", false, "print this message");
			options.addOption(Option.builder("help").required(false).hasArg(false).longOpt("help").desc("print this message").build());
			options.addOption("modulelist", false, "print module list information");
			options.addOption(Option.builder("config").required(false).hasArg(true).longOpt("config").optionalArg(false).desc("use configfile <configfile>").build());
			options.addOption(Option.builder("module").required(false).hasArg(true).longOpt("module").optionalArg(false).desc("use module <module>").build());
			options.addOption(Option.builder("classlist").required(false).hasArg(false).longOpt("classlist").optionalArg(false).desc("list all classes in all subdirs and jars").build());
			options.addOption(Option.builder("ls").required(false).hasArg(true).longOpt("ls").optionalArg(true).desc("dir listing").build());
			//options.addOption("ls", false, "dir listing");
			final CommandLine line = parser.parse(options, args);

			// check multiple values
			//final String[] opts = line.getOptionValues("D");
			final Option[] parsedOptions = line.getOptions();
			for(Option singleOption: parsedOptions)
			{
				System.out.println("Option " + singleOption.getOpt() + ":" + singleOption.getValue());
				if(singleOption.getOpt().equals("ls"))
				{
					String dir = (singleOption.getValue() != null)?singleOption.getValue():".";
					System.out.println(ls(dir));
					System.exit(0);
				}
				if(singleOption.getOpt().equals("help"))
				{
					String header = "My Testprogram\n\n";
					String footer = "\nNo support currently!\n";
					HelpFormatter formatter = new HelpFormatter();
					formatter.printHelp("MainProgram", header, options, footer, true);
					System.exit(0);
				}
				if(singleOption.getOpt().equals("modulelist"))
				{
					for(Class c: ModuleList)
					{
						Handler h = (Handler)c.newInstance();
						System.out.println(c.getName());
						System.out.println(h.info());
					}
					System.exit(0);
				}
				if(singleOption.getOpt().equals("classlist"))
				{
					/*for(Class clazz: ClassScannerUtils.getAllClassesInPath(new File("."), true))
					{
						System.out.println(clazz.getCanonicalName());
					}*/
					for(String className: ClassScannerUtils.getAllClassNamesInPath(new File("."), true))
					{
						System.out.println(className);
					}
					System.exit(0);
				}
				if(singleOption.getOpt().equals("config")) ConfigFile = singleOption.getValue();
				if(singleOption.getOpt().equals("module")) Module = singleOption.getValue();
				
			}
			//System.out.println("Opts:" + Arrays.toString(parsedOptions));
        }
        catch(Exception ex)
        {
			System.err.println("Error:" + ex.getMessage() + ", " + ex.getCause());
			ex.printStackTrace();
        }
		
	}
}

