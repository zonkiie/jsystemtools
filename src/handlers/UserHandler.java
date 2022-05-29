package handlers;

import interfaces.*;
import entities.*;
import java.lang.*;
import java.util.*;

@PublicCallable
public class UserHandler implements /*CRUDLS<UnixPasswd>, */ Handler
{
	@PublicCallable
	public String info()
	{
		return "Handles User Management";
	}
	
	@PublicCallable
	public List<UnixPasswd> list()
	{
		try
		{
			List<UnixPasswd> entryList = new ArrayList<UnixPasswd>();
			ExecutorReturn executorReturn = SimpleExecutor.execute("getent", "passwd");
			if(executorReturn.returnCode != 0) throw new Exception("Something failed! Return code:" + executorReturn.returnCode);
			String [] lines = executorReturn.toString().split("\\R");
			for(String line: lines) entryList.add(parseLine(line));
			return entryList;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public UnixPasswd parseLine(String line)
	{
		UnixPasswd entry = new UnixPasswd();
		String [] els = line.split(":");
		if(els.length != 7) return null; //Something failed. 7 Entries are nesessary.
		entry.pw_name = els[0];
		entry.pw_passwd = els[1];
		entry.pw_uid = Integer.parseInt(els[2]);
		entry.pw_gid = Integer.parseInt(els[3]);
		entry.pw_gecos = els[4];
		entry.pw_dir = els[5];
		entry.pw_shell = els[6];
		return entry;
	}
}
