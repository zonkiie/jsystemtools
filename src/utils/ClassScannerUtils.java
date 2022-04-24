package utils;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;

public class ClassScannerUtils
{
	public static Set<Class> getAllClassesInPath(File path, Boolean recursive) throws IOException, MalformedURLException
	{
		Set<Class> classList = new HashSet<Class>();
		for(File f: FileListingUtils.scanFiles(path, ".*\\.class$", recursive))
		{
			if(!f.exists()) throw new IOException("File " + f.getPath() + " not found!");
			//URLClassLoader cl = URLClassLoader.newInstance(new URL[]{path.toURI().toURL()});
			try
			{
				Class clazz = new ClassLoaderFromClassFile(new URL[]{path.toURI().toURL()}).getClassFromPath(f.getCanonicalFile());
				//System.out.println("Class: " + clazz.getCanonicalName());
				//Class clazz = cl.loadClass("file:/" + f.getCanonicalPath());
				if(clazz != null) classList.add(clazz);
			}
			catch(/*ClassNotFoundException*/ Exception ex)
			{
				//System.out.println("ClassFiles: Class not found:" + ex.getMessage());
			}
		}
		for(File archive: FileListingUtils.scanFiles(path, ".*\\.jar", recursive))
		{
			URLClassLoader cl = URLClassLoader.newInstance(new URL[] { new URL("jar:file:" + archive.getPath() + "!/") });
			for(File archContent: FileListingUtils.scanArchive(archive, ".*\\.class$", recursive))
			{
				//System.out.println("Scan:" + archive.getPath() + "/" + archContent.getPath());
				try
				{
					String className = archContent.getPath().replaceAll("\\.[^.]*$", "").replaceAll("/", ".");
					Class clazz = cl.loadClass(className);
					if(clazz != null) classList.add(clazz);
				}
				catch(ClassNotFoundException ex)
				{
					//System.out.println("Class not found:" + ex.getMessage());
				}
				catch(NoClassDefFoundError ex)
				{
					//System.out.println("NoClassDefFoundError:" + ex.getMessage());
				}
			}
		}
		return classList;
	}
	
	public static Set<String> getAllClassNamesInPath(File path, Boolean recursive) throws IOException, MalformedURLException
	{
		Set<String> classNameList = new HashSet<String>();
		for(Class clazz: getAllClassesInPath(path, recursive))
		{
			if(clazz == null || clazz.getCanonicalName() == null) continue;
			classNameList.add(clazz.getCanonicalName());
		}
		return classNameList;
	}
}
