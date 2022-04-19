import java.lang.*;
import java.util.*;

interface CRUDLS<T>
{
	public CRUDLS add(T o);
	public CRUDLS set(T o);
	public T get();
	public List<T> list();
	public List<T> search(T o);
	public CRUDLS delete(T o);
}

