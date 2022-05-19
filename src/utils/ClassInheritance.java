package utils;

import java.lang.*;
import java.util.*;
import interfaces.*;

@PublicCallable
public class ClassInheritance implements Handler
{
	@PublicCallable
	public String info()
	{
		return "Test class inheritance";
	}
	
	@PublicCallable
	public String testClassInheritance()
	{
		entities.ApacheVHostSSL entity = new entities.ApacheVHostSSL();
		List<Class> inheritanceTree = getClassTree(entity);
		String cl = "";
		for(Class clazz: inheritanceTree)
		{
			cl += clazz.getName() + ";";
		}
		return cl;
	}
	
	public List<Class> getClassTree(Object object)
	{
		return getClassTree(object.getClass());
	}
	
	public List<Class> getClassTree(Class clazz)
	{
		return getClassTree(new ArrayList<Class>() {{ add(clazz); }} );
	}
	
	public List<Class> getClassTree(List<Class> clazzList)
	{
		if(clazzList == null || clazzList.size() == 0) return null;
		while(!(clazzList.get(clazzList.size() - 1).getName().equals(Object.class.getName())))
		{
			if(clazzList.get(clazzList.size() - 1).getSuperclass().getName().equals(Object.class.getName())) break;
			clazzList.add(clazzList.get(clazzList.size() - 1).getSuperclass());
		}
		return clazzList;
	}
}
