import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.stream.*;
import entities.*;
import handlers.*;
import org.apache.commons.cli.*;

public class MainProgram
{
	
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
			//options.addOption("help", false, "print this message");
			options.addOption(Option.builder("help").required(false).hasArg(false).longOpt("help").desc("print this message").build());
			options.addOption("projecthelp", false, "print project help information");
			options.addOption("version", false, "print the version information and exit");
			options.addOption("quiet", false, "be extra quiet");
			options.addOption("verbose", false, "be extra verbose");
			options.addOption("debug", false, "print debug information");
			options.addOption("logfile", true, "use given file for log");
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
			}
			//System.out.println("Opts:" + Arrays.toString(parsedOptions));
        }
        catch(Exception ex)
        {
			System.err.println("Error:" + ex.getMessage());
        }
		
	}
}

