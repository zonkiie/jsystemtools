package utils;

import java.lang.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.security.*;

public class ClassLoaderFromClassFile extends URLClassLoader
{
	public ClassLoaderFromClassFile(URL[] urls)
	{
		super(urls);
	}

	public Class getClassFromPath(File path) throws IOException
	{
		if(!path.exists()) throw new IOException("File " + path.getAbsolutePath() + " does not exist!");
		//System.out.println("Try " + path.getAbsolutePath());
		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(path));
		byte[] buffer = new byte[(int)path.length()];
		bufferedInputStream.read(buffer, 0, buffer.length);
		try
		{
			Class<?> clazz = defineClass((String)null, buffer, 0, buffer.length);
			return clazz;
		}
		catch(NoClassDefFoundError ex)
		{
			//System.err.println("NoClassDefFoundError:" + ex.getMessage());
			return null;
		}
	}
}
