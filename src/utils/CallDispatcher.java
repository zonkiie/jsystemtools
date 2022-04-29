package utils;

import java.io.*;
import java.lang.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import utils.*;
import interfaces.*;

public class CallDispatcher
{
	public static Object Call(String ClassName, String MethodName, Object ... args)
	{
		try
		{
			Class clazz = Class.forName(ClassName);
			// Former Code: Object instance = clazz.newInstance();
			Object instance = clazz.getDeclaredConstructor().newInstance();
			PublicCallable classPublicCallable = instance.getClass().getAnnotation(PublicCallable.class);
			if(classPublicCallable == null) throw new InstantiationException("No allowed method found!"); //return null;
			Vector<Class> classVec = new Vector<Class>();
			for(Object arg: args) classVec.add(arg.getClass());
			for(Method m: clazz.getMethods())
			{
				if(!(m.getName().equals(MethodName))) continue;
				PublicCallable publicCallable = m.getAnnotation(PublicCallable.class);
				// Security barrier wich allows only @PublicCallable annotated methods to be public called
				if(publicCallable == null) throw new NoSuchMethodException("No allowed method found!"); //return null;
				if(Arrays.equals(m.getParameterTypes(), classVec.toArray())) return m.invoke(instance, args);
			}
			throw new NoSuchMethodException("No suitable method found!");
		}
		catch(ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex)
		{
			System.err.println("Exception! " + ex.getMessage());
			ex.printStackTrace();
		}
		return null;
	}
}
