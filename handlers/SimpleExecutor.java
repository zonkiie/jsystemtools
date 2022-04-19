package handlers;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.stream.*;

class SimpleExecutor
{
	public static SimpleExecutor newInstance()
	{
		return new SimpleExecutor();
	}
	
	public static String executeStr(String... command) throws Exception
	{
		Process process = new ProcessBuilder(command).start();
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		//To get the exit code, we can use: int exitCode = process.exitValue();
		//System.out.println("Exit Code is:" + process.waitFor());
		return br.lines().collect(Collectors.joining(System.lineSeparator()));
	}
	
	public static ExecutorReturn execute(String... command) throws Exception
	{
		Process process = new ProcessBuilder(command).start();
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		return ExecutorReturn.newInstance().setReturnCode(process.waitFor()).setReturnString(br.lines().collect(Collectors.joining(System.lineSeparator())));
	}
}

