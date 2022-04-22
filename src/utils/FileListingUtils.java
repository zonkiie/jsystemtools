import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;

public class FileListingUtils
{
	public static List<File> scanFiles(File path, Boolean recursive)
	{
		List<File> fileList = new ArrayList<File>();
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
	public static List<File> scanFiles(File path, String pattern, Boolean recursive) throws IOException
	{
		List<File> fileList = new ArrayList<File>();
		for(File entry: path.listFiles())
		{
			if(entry.isDirectory() && recursive == true) fileList.addAll(scanFiles(entry, pattern, recursive));
			else if(entry.isFile() && entry.getCanonicalPath().matches(pattern)) fileList.add(entry);
		}
		return fileList;
	}
	
	public static List<File> scanArchive(File path, String pattern, Boolean recursive) throws IOException
	{
		List<File> fileList = new ArrayList<File>();
		JarFile jarFile = new JarFile(path);
		Enumeration<JarEntry> e = jarFile.entries();
		while (e.hasMoreElements()) {
			JarEntry jarEntry = e.nextElement();
			if (jarEntry.getName().matches(pattern) && !jarEntry.isDirectory()) {
				fileList.add(new File(jarEntry.getName()));
			}
		}
		return fileList;
	}
	
	public static List<File> scanArchive(File path, Boolean recursive) throws IOException
	{
		List<File> fileList = new ArrayList<File>();
		JarFile jarFile = new JarFile(path);
		Enumeration<JarEntry> e = jarFile.entries();
		while (e.hasMoreElements()) {
			JarEntry jarEntry = e.nextElement();
			if (!jarEntry.isDirectory()) {
				fileList.add(new File(jarEntry.getName()));
			}
		}
		return fileList;
	}
	
}
