package interfaces;

import java.lang.*;
import java.util.*;

public interface CRUDLS<T>
{
	public CRUDLS<T> add(T o);
	public CRUDLS<T> add(List<T> o);
	public CRUDLS<T> set(T o);
	public CRUDLS<T> set(List<T> o);
	public CRUDLS<T> rename(String oldName, String newName);
	public T get(String o);
	public T get(T o);
	public List<T> list();
	public List<T> search(T o);
	public CRUDLS<T> delete(T o);
	public CRUDLS<T> delete(List<T> o);
	public CRUDLS<T> delete(String o);
}

