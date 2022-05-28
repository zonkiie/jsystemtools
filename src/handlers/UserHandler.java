package handlers;

import interfaces.*;
import entities.*;

@PublicCallable
public class UserHandler implements /*CRUDLS<UnixPasswd>, */ Handler
{
	@PublicCallable
	public String info()
	{
		return "Handles User Management";
	}
}
