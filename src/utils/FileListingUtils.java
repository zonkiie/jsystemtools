import java.io.*;
import java.util.*;

public class FileListingUtils
{
	public static List<File> scanFiles(File path, Boolean recursive)
	{
		List<Class> fileList = new ArrayList<Class>();
		for(File entry: path.listFiles())
		{
			if(entry.isDirectory() && recursive == true) fileList.addAll(scanFiles(entry, recursive));
			else if(entry.isFile()) fileList.add(entry);
		}
		return fileList;
	}
	
	/**
	* create a list of files which matches against the given regex pattern
	*/
	public static List<File> scanFiles(File path, String pattern, Boolean recursive)
	{
		List<Class> fileList = new ArrayList<Class>();
		for(File entry: path.listFiles())
		{
			if(entry.isDirectory() && recursive == true) fileList.addAll(scanFiles(entry, pattern, recursive));
			else if(entry.isFile() && entry.getCanonicalPath().matches(pattern)) fileList.add(entry);
		}
		return fileList;
	}
	
	public static List<File> scanFiles(File path, String pattern)
	{
		return scanFiles(path, pattern, true);
	}
	
	public static List<File> scanFiles(String path, Boolean recursive)
	{
		return scanFiles(new File(path), true);
	}
	
	public static List<File> scanFiles(String path, String pattern, Boolean recursive)
	{
		return scanFiles(new File(path), pattern, recursive);
	}
}
