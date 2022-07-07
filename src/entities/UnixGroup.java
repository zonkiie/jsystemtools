package entities;
import java.lang.*;
import java.util.*;
import org.apache.commons.lang3.builder.*;

public class UnixGroup
{
	public String gr_name = null;
	public String gr_passwd = null;
	public Integer gr_gid = null;
	public List<String> gr_mem = null;
	
	public String toString()
	{
		//return String.format("[User: %s, Passwd: %s, uid: %d, gid: %d, gecos: %s, homedir: %s, shell: %s]", pw_name, pw_passwd, pw_uid, pw_gid, pw_gecos, pw_dir, pw_shell);
		return ToStringBuilder.reflectionToString(this);
	}
}
