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
		try
		{
			List<String> Params = new ArrayList<String>(){{add("groupadd");}};
			if(o.gr_passwd != null)
			{
				Params.add("-p");
				Params.add(o.gr_passwd);
			}
			if(o.gr_gid != null)
			{
				Params.add("-g");
				Params.add(o.gr_gid.toString());
			}
			Params.add(o.gr_name);
			String[] arrayParams = Params.toArray(new String[Params.size()]);
			ExecutorReturn executorReturn = SimpleExecutor.execute(arrayParams);
			if(executorReturn.returnCode != 0) throw new Exception(executorReturn.toString());
			return this;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public CRUDLS<UnixGroup> add(List<UnixGroup> o)
	{
		for(UnixGroup entry: o) add(entry);
		return this;
	}
	
	public CRUDLS<UnixGroup> set(UnixGroup o)
	{
		try
		{
			List<String> Params = new ArrayList<String>(){{add("groupmod");}};
			if(o.gr_passwd != null)
			{
				Params.add("-p");
				Params.add(o.gr_passwd);
			}
			if(o.gr_gid != null)
			{
				Params.add("-g");
				Params.add(o.gr_gid.toString());
			}
			Params.add(o.gr_name);
			String[] arrayParams = Params.toArray(new String[Params.size()]);
			ExecutorReturn executorReturn = SimpleExecutor.execute(arrayParams);
			if(executorReturn.returnCode != 0) throw new Exception(executorReturn.toString());
			return this;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public CRUDLS<UnixGroup> set(List<UnixGroup> o)
	{
		for(UnixGroup entry: o) set(entry);
		return this;
	}
	
	public CRUDLS<UnixGroup> rename(String from, String to)
	{
		try
		{
			ExecutorReturn executorReturn = SimpleExecutor.execute("groupmod", "-n", to, from);
			if(executorReturn.returnCode != 0) throw new Exception("Something failed! Return code:" + executorReturn.returnCode + ", Return String:" + executorReturn.toString());
			return this;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public CRUDLS<UnixGroup> rename(UnixGroup oldObject, UnixGroup renamedObject)
	{
		try
		{
			rename(oldObject.gr_name, renamedObject.gr_name);
			return this;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	@PublicCallable
	public UnixGroup get(String id)
	{
		try
		{
			ExecutorReturn executorReturn = SimpleExecutor.execute("getent", "group", id);
			if(executorReturn.returnCode != 0) throw new Exception("Something failed! Return code:" + executorReturn.returnCode);
			UnixGroup entry = parseLine(executorReturn.toString());
			return entry;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public UnixGroup get(UnixGroup o)
	{
		return get(o.gr_name);
	}
	
	@PublicCallable
	public List<UnixGroup> list()
	{
		try
		{
			List<UnixGroup> entryList = new ArrayList<UnixGroup>();
			// getent group because this lists also groups which are not in /etc/group like nis(+), ldap, other naming services
			ExecutorReturn executorReturn = SimpleExecutor.execute("getent", "group");
			if(executorReturn.returnCode != 0) throw new Exception("Something failed! Return code:" + executorReturn.returnCode);
			for(String line: executorReturn.toString().split("\\R")) entryList.add(parseLine(line));
			return entryList;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public List<UnixGroup> search(UnixGroup o)
	{
		try
		{
			List<UnixGroup> entryList = new ArrayList<UnixGroup>();;
			for(UnixGroup entry: list())
			{
				if(entry.gr_name.matches(".*" + Pattern.quote(o.gr_name) + ".*")) entryList.add(entry);
			}
			return entryList;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public CRUDLS<UnixGroup> delete(UnixGroup o)
	{
		delete(o.gr_name);
		return this;
	}
	
	public CRUDLS<UnixGroup> delete(List<UnixGroup> groupList)
	{
		for(UnixGroup entry: groupList) delete(entry);
		return this;
	}
	
	public CRUDLS<UnixGroup> delete(String id)
	{
		try
		{
			ExecutorReturn executorReturn = SimpleExecutor.execute("groupdel", id);
			if(executorReturn.returnCode != 0) throw new Exception("Something failed! Return code:" + executorReturn.returnCode);
			return this;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public UnixGroup parseLine(String line)
	{
		UnixGroup entry = new UnixGroup();
		String [] els = line.split(":");
		if(els.length != 4) return null; //Something failed. 7 Entries are nesessary.
		entry.gr_name = els[0];
		entry.gr_passwd = els[1];
		entry.gr_gid = Integer.valueOf(els[2]);
		entry.gr_mem = Arrays.asList(els[3].split(","));
		return entry;
	}
}
