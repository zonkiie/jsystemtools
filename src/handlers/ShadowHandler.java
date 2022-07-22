package handlers;

import interfaces.*;
import entities.*;
import java.lang.*;
import java.util.*;
import java.util.regex.*;

@PublicCallable
public class ShadowHandler implements CRUDLS<UnixShadow>, Handler
{
	@PublicCallable
	public String info()
	{
		return "Handles Password Management";
	}
	
}
