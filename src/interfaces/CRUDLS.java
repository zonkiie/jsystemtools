package interfaces;

import java.lang.*;
import java.util.*;

public interface CRUDLS<T>
{
	public CRUDLS<T> add(T o);
	public CRUDLS<T> set(T o);
	public T get(T o);
	public List<T> list();
	public List<T> search(T o);
	public CRUDLS<T> delete(T o);
}

