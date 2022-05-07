package utils;

import java.lang.*;
import java.lang.reflect.*;

public class ReflectionHelper 
{
	public static boolean hasField(Class clazz, String fieldName)
	{
		for(Field f: clazz.getFields())
			if(f.getName().equals(fieldName)) return true;
		return false;
	}
	
	public static boolean hasMethod(Class clazz, String methodName)
	{
		for(Method m: clazz.getMethods())
			if(m.getName().equals(methodName)) return true;
		return false;
	}
}
