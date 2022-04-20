package handlers;

import interfaces.*;
import entities.*;
import java.util.*;

public class ApacheHttpdHandler implements CRUDLS<ApacheVHostName>
{
	private String getLineEntryForVHostName(ApacheVHostName o)
	{
		return "";
	}
	
	private String setLineEntryForVHost(ApacheVHostName o)
	{
		return "";
	}

	public CRUDLS<ApacheVHostName> add(ApacheVHostName o)
	{
		return this;
	}
	
	public CRUDLS<ApacheVHostName> set(ApacheVHostName o)
	{
		return this;
	}
	
	public ApacheVHostName get(ApacheVHostName o)
	{
		return new ApacheVHost();
	}

	public List<ApacheVHostName> list()
	{
		return new Vector<ApacheVHostName>();
	}
	
	public List<ApacheVHostName> search(ApacheVHostName o)
	{
		return new Vector<ApacheVHostName>();
	}

	public CRUDLS<ApacheVHostName> delete(ApacheVHostName vhn)
	{
		return this;
	}
}
