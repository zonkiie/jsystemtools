package utils;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;

public class ClassScannerUtils
{
	public static Set<Class> getAllClassesInPath(File path, Boolean recursive) throws IOException, MalformedURLException, ClassNotFoundException
	{
		Set<Class> classList = new HashSet<Class>();
		for(File f: FileListingUtils.scanFiles(path, ".*\\.class$", recursive))
		{
			System.out.println("Scan:" + f.getPath());
			URLClassLoader cl = URLClassLoader.newInstance(new URL[]{f.getAbsoluteFile().getParentFile().toURI().toURL()});
			Class clazz = cl.loadClass(f.getPath());
			classList.add(clazz);
		}
		for(File archive: FileListingUtils.scanFiles(path, ".*\\.jar", recursive))
		{
			URLClassLoader cl = URLClassLoader.newInstance(new URL[] { new URL("jar:file:" + archive.getPath() + "!/") });
			for(File archContent: FileListingUtils.scanArchive(archive, ".*\\.class$", recursive))
			{
				System.out.println("Scan:" + archive.getPath() + "/" + archContent.getPath());
				Class clazz = cl.loadClass(archContent.getPath());
				classList.add(clazz);
			}
		}
		return classList;
	}
}
