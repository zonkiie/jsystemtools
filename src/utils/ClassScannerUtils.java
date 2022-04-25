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
		if(!path.exists()) throw new IOException("File " + path.getPath() + " not found!");
		Set<Class> classList = new HashSet<Class>();
		if(path.isFile() && path.getName().endsWith(".jar"))
		{
			URLClassLoader cl = URLClassLoader.newInstance(new URL[] { new URL("jar:file:" + path.getPath() + "!/") });
			for(File archContent: FileListingUtils.scanArchive(path, ".*\\.class$", recursive))
			{
				try
				{
					String className = archContent.getPath().replaceAll("\\.[^.]*$", "").replaceAll("/", ".");
					Class clazz = cl.loadClass(className);
					if(clazz != null && clazz.getCanonicalName() != null) classList.add(clazz);
				}
				// Multi Catch here
				catch(ClassNotFoundException | NoClassDefFoundError ex) { }
			}
			return classList;
		}
		else if(path.isFile() && path.getName().endsWith(".class"))
		{
			try
			{
				Class clazz = new ClassLoaderFromClassFile(new URL[]{path.toURI().toURL()}).getClassFromPath(path.getCanonicalFile());
				if(clazz != null && clazz.getCanonicalName() != null) classList.add(clazz);
			}
			catch(Exception ex) { }
			return classList;
		}
		else if(path.isDirectory())
		{
			for(File entry: path.listFiles())
			{
				Set<Class> subClassList = getAllClassesInPath(entry, recursive);
				if(subClassList != null) classList.addAll(subClassList);
			}
			return classList;
		}
		return null;
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
