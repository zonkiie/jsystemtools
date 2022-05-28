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
	private final static String BLACK = "\033[0;30m", BLACK_BOLD = "\33[1;30m", RED = "\033[0;31m", RESET = "\033[0m";
	private static List<Class> ModuleList = List.of(ApacheHttpdHandler.class, utils.ClassInheritance.class, UserHandler.class);
	
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

			String ConfigFile = null, module = null, VHostFile = null, method = null;
			boolean verbose = false;
			Vector<String> callArgs = new Vector<String>();

			//options.addOption("help", false, "print this message");
			options.addOption(Option.builder("help").required(false).hasArg(false).longOpt("help").desc("print this message").build());
			options.addOption(Option.builder("modulelist").required(false).hasArg(false).longOpt("modulelist").desc("print module list information").build());
			options.addOption(Option.builder("verbose").required(false).hasArg(false).longOpt("verbose").desc("verbose operation").build());
			options.addOption(Option.builder("config").required(false).hasArg(true).longOpt("config").optionalArg(false).desc("use configfile <configfile>").build());
			options.addOption(Option.builder("module").required(false).hasArg(true).longOpt("module").optionalArg(false).desc("use module <module>").build());
			options.addOption(Option.builder("method").required(false).hasArg(true).longOpt("method").optionalArg(false).desc("use method <method>").build());
			options.addOption(Option.builder("arg").required(false).hasArg(true).longOpt("arg").optionalArg(false).desc("use argument <argument>").build());
			options.addOption(Option.builder("classlist").required(false).hasArg(false).longOpt("classlist").optionalArg(false).desc("list all classes in all subdirs and jars").build());
			options.addOption(Option.builder("ls").required(false).hasArg(true).longOpt("ls").optionalArg(true).desc("dir listing").build());
			//options.addOption("ls", false, "dir listing");
			final CommandLine line = parser.parse(options, args);

			// check multiple values
			//final String[] opts = line.getOptionValues("D");
			final Option[] parsedOptions = line.getOptions();
			for(Option singleOption: parsedOptions)
			{
				if(singleOption.getOpt().equals("verbose")) verbose = true;
				if(verbose) System.err.println("Option " + singleOption.getOpt() + ":" + singleOption.getValue());
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
						// Former Code: Handler h = (Handler)c.newInstance();
						Handler h = (Handler)c.getDeclaredConstructor().newInstance();
						System.out.format("%s%-35s: %s%s\n", RED, c.getName(), RESET, h.info());
					}
					System.out.println("Note: Use Canoncal Name including Package Name for a call! Otherwise a Class/Module cannot be found!");
					System.exit(0);
				}
				if(singleOption.getOpt().equals("classlist"))
				{
					for(Class clazz: ClassScannerUtils.getAllClassesInPath(new File("build/classes"), true)) System.out.println(clazz.getCanonicalName());
					//for(String className: ClassScannerUtils.getAllClassNamesInPath(new File("."), true)) System.out.println(className);
					System.exit(0);
				}
				if(singleOption.getOpt().equals("config")) ConfigFile = singleOption.getValue();
				if(singleOption.getOpt().equals("module")) module = singleOption.getValue();
				if(singleOption.getOpt().equals("method")) method = singleOption.getValue();
				if(singleOption.getOpt().equals("arg")) callArgs.add(singleOption.getValue());
			}
			if(module != null && method != null)
			{
				System.out.println(CallDispatcher.Call(module, method, callArgs.toArray()));
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

