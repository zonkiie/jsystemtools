package utils;

import java.io.*;
import java.lang.*;
import java.lang.reflect.*;
import java.util.*;

public class CallDispatcher
{
	public static Object Call(String ClassName, String MethodName, Object ... args)
	{
		try
		{
			Class clazz = Class.forName(ClassName);
			Object instance = clazz.newInstance();
			Vector<Class> classVec = new Vector<Class>();
			for(Object arg: args) classVec.add(arg.getClass());
			for(Method m: clazz.getMethods())
			{
				if(Arrays.equals(m.getParameterTypes(), classVec.toArray()))
				{
					return m.invoke(instance, args);
				}
			}
		}
		catch(ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException ex)
		{
			System.err.println(ex.getMessage());
		}
		return null;
	}
}
