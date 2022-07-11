package entities;
import java.lang.*;
import org.apache.commons.lang3.builder.*;

public class UnixShadow
{
	public String	sp_namp = null;		/* Login name */
	public String	sp_pwdp = null;		/* Encrypted password */
	public Long		sp_lstchg = null;		/* Date of last change (measured in days since 1970-01-01 00:00:00 +0000 (UTC)) */
	public Long		sp_min = null;			/* Min # of days between changes */
	public Long		sp_max = null;			/* Max # of days between changes */
	public Long		sp_warn = null;		/* # of days before password expires to warn user to change it */
	public Long		sp_inact = null;		/* # of days after password expires until account is disabled */
	public Long		sp_expire = null;		/* Date when account expires (measured in days since 1970-01-01 00:00:00 +0000 (UTC)) */
	public Long		sp_flag = null;			/* Reserved */
	
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
}
