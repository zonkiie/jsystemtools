package handlers;

import interfaces.*;
import entities.*;
import java.lang.*;
import java.util.*;
import java.util.regex.*;
import java.security.*;

@PublicCallable
public class ShadowHandler implements CRUDLS<UnixShadow>, Handler
{
	@PublicCallable
	public String info()
	{
		return "Handles Password Management";
	}
	
	public CRUDLS<UnixShadow> add(UnixShadow o)
	{
		return this;
	}
	
	public CRUDLS<UnixShadow> add(List<UnixShadow> o)
	{
		return this;
	}
	
	public CRUDLS<UnixShadow> set(UnixShadow o)
	{
		try
		{
			List<String> Params = new ArrayList<String>(){{add("usermod");}};
			if(o.newPassword != null)
			{
				o.sp_pwdp = getSecurePassword(o.newPassword, getSalt());
				Params.add("-p");
				Params.add(o.sp_pwdp);
			}
			if(o.sp_expire != null)
			{
				Params.add("-e");
				Params.add(o.sp_expire.toString());
			}
			if(o.sp_inact != null)
			{
				Params.add("-i");
				Params.add(o.sp_inact.toString());
			}
			Params.add(o.sp_namp);
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
	
	public CRUDLS<UnixShadow> set(List<UnixShadow> o)
	{
		for(UnixShadow entry: o) set(entry);
		return this;
	}
	
	public CRUDLS<UnixShadow> rename(String from, String to)
	{
		try
		{
			return this;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	public CRUDLS<UnixShadow> rename(UnixShadow oldObject, UnixShadow renamedObject)
	{
		try
		{
			rename(oldObject.sp_namp, renamedObject.sp_namp);
			return this;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	@PublicCallable
	public UnixShadow get(String id)
	{
		try
		{
			ExecutorReturn executorReturn = SimpleExecutor.execute("getent", "shadow", id);
			if(executorReturn.returnCode != 0) throw new Exception("Something failed! Return code:" + executorReturn.returnCode);
			UnixShadow entry = parseLine(executorReturn.toString());
			return entry;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public UnixShadow get(UnixShadow o)
	{
		return get(o.sp_namp);
	}
	
	@PublicCallable
	public List<UnixShadow> list()
	{
		try
		{
			List<UnixShadow> entryList = new ArrayList<UnixShadow>();
			ExecutorReturn executorReturn = SimpleExecutor.execute("getent", "shadow");
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
	
	public List<UnixShadow> search(UnixShadow o)
	{
		try
		{
			List<UnixShadow> entryList = new ArrayList<UnixShadow>();;
			return entryList;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public CRUDLS<UnixShadow> delete(UnixShadow o)
	{
		return this;
	}
	
	public CRUDLS<UnixShadow> delete(List<UnixShadow> userList)
	{
		return this;
	}
	
	public CRUDLS<UnixShadow> delete(String id)
	{
		try
		{
			return this;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public UnixShadow parseLine(String line)
	{
		UnixShadow entry = new UnixShadow();
		String [] els = line.split(":");
		if(els.length != 9) return null; //Something failed. 9 Entries are nesessary.
		entry.sp_namp = els[0];
		entry.sp_pwdp = els[1];
		entry.sp_lstchg = Long.valueOf(els[2]);
		entry.sp_min = Long.valueOf(els[3]);
		entry.sp_max = Long.valueOf(els[4]);
		entry.sp_warn = Long.valueOf(els[5]);
		entry.sp_inact = Long.valueOf(els[6]);
		entry.sp_expire = Long.valueOf(els[7]);
		entry.sp_flag = Long.valueOf(els[8]);
		return entry;
	}
	
	private static String getSecurePassword(String passwordToHash, String salt) {
		String generatedPassword = null;
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			// Add password bytes to digest
			md.update(salt.getBytes());
			
			// Get the hash's bytes
			byte[] bytes = md.digest(passwordToHash.getBytes());
			
			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			
			// Get complete hashed password in hex format
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	
	// Add salt
	private static String getSalt()
			throws NoSuchAlgorithmException, NoSuchProviderException 
	{
		// Always use a SecureRandom generator
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");

		// Create array for salt
		byte[] salt = new byte[16];

		// Get a random salt
		sr.nextBytes(salt);

		// return salt
		return salt.toString();
	}
	
}
