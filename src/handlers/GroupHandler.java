package handlers;

import interfaces.*;
import entities.*;
import java.lang.*;
import java.util.*;
import java.util.regex.*;

@PublicCallable
public class GroupHandler implements CRUDLS<UnixGroup>, Handler
{
	@PublicCallable
	public String info()
	{
		return "Handles Group Management";
	}
	
	public CRUDLS<UnixGroup> add(UnixGroup o)
	{
		
	}
}
