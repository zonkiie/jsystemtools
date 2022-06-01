package handlers;

import interfaces.*;
import entities.*;
import java.lang.*;
import java.util.*;

@PublicCallable
public class UserHandler implements CRUDLS<UnixPasswd>, Handler
{
	public String homeBaseDir = "/home";
	@PublicCallable
	public String info()
	{
		return "Handles User Management";
	}
	
	public CRUDLS<UnixPasswd> add(UnixPasswd o)
	{
		try
		{
			List<String> Params = new ArrayList<String>(){{add("useradd");}};
			if(o.pw_dir != null)
			{
				Params.add("-d");
				Params.add("-m");
				Params.add(o.pw_dir);
			}
			if(o.pw_shell != null)
			{
				Params.add("-s");
				Params.add(o.pw_shell);
			}
			if(o.pw_gecos != null)
			{
				Params.add("-c");
				Params.add(o.pw_gecos);
			}
			if(o.pw_uid != null)
			{
				Params.add("-u");
				Params.add(o.pw_uid.toString());
			}
			if(o.pw_gid != null)
			{
				Params.add("-g");
				Params.add(o.pw_gid.toString());
			}
			Params.add(o.pw_name);
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
	
	public CRUDLS<UnixPasswd> add(List<UnixPasswd> o)
	{
		return null;
	}
	
	public CRUDLS<UnixPasswd> set(UnixPasswd o)
	{
		try
		{
			List<String> Params = new ArrayList<String>(){{add("usermod");}};
			if(o.pw_dir != null)
			{
				Params.add("-d");
				Params.add("-m");
				Params.add(o.pw_dir);
			}
			if(o.pw_shell != null)
			{
				Params.add("-s");
				Params.add(o.pw_shell);
			}
			if(o.pw_gecos != null)
			{
				Params.add("-c");
				Params.add(o.pw_gecos);
			}
			if(o.pw_uid != null)
			{
				Params.add("-u");
				Params.add(o.pw_uid.toString());
			}
			if(o.pw_gid != null)
			{
				Params.add("-g");
				Params.add(o.pw_gid.toString());
			}
			Params.add(o.pw_name);
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
	
	public CRUDLS<UnixPasswd> set(List<UnixPasswd> o)
	{
		return null;
	}
	
	public CRUDLS<UnixPasswd> rename(String from, String to)
	{
		try
		{
			ExecutorReturn executorReturn = SimpleExecutor.execute("usermod", "-d", homeBaseDir + "/" + to, "-l", to, from);
			if(executorReturn.returnCode != 0) throw new Exception("Something failed! Return code:" + executorReturn.returnCode + ", Return String:" + executorReturn.toString());
			return this;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public CRUDLS<UnixPasswd> rename(UnixPasswd oldObject, UnixPasswd renamedObject)
	{
		return null;
	}
	
	@PublicCallable
	public UnixPasswd get(String id)
	{
		try
		{
			ExecutorReturn executorReturn = SimpleExecutor.execute("getent", "passwd", id);
			if(executorReturn.returnCode != 0) throw new Exception("Something failed! Return code:" + executorReturn.returnCode);
			UnixPasswd entry = parseLine(executorReturn.toString());
			return entry;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public UnixPasswd get(UnixPasswd o)
	{
		return null;
	}
	
	@PublicCallable
	public List<UnixPasswd> list()
	{
		try
		{
			List<UnixPasswd> entryList = new ArrayList<UnixPasswd>();
			ExecutorReturn executorReturn = SimpleExecutor.execute("getent", "passwd");
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
	
	public List<UnixPasswd> search(UnixPasswd o)
	{
		return null;
	}
	
	public CRUDLS<UnixPasswd> delete(UnixPasswd o)
	{
		return null;
	}
	
	public CRUDLS<UnixPasswd> delete(List<UnixPasswd> o)
	{
		return null;
	}
	
	public CRUDLS<UnixPasswd> delete(String id)
	{
		return null;
	}
	
	public UnixPasswd parseLine(String line)
	{
		UnixPasswd entry = new UnixPasswd();
		String [] els = line.split(":");
		if(els.length != 7) return null; //Something failed. 7 Entries are nesessary.
		entry.pw_name = els[0];
		entry.pw_passwd = els[1];
		entry.pw_uid = Integer.valueOf(els[2]);
		entry.pw_gid = Integer.valueOf(els[3]);
		entry.pw_gecos = els[4];
		entry.pw_dir = els[5];
		entry.pw_shell = els[6];
		return entry;
	}
}
