package entities;
import java.lang.*;
import org.apache.commons.lang3.builder.*;

public class UnixPasswd
{
	public String pw_name = null;       /* Benutzername */
	public String pw_passwd = null;     /* Passwort des Benutzers */
	public Integer pw_uid = null;        /* Benutzerkennung */
	public Integer pw_gid = null;        /* Gruppenkennung */
	public String pw_gecos = null;      /* Benutzerinformationen */
	public String pw_dir = null;        /* Home-Verzeichnis */
	public String pw_shell = null;
	
	public String toString()
	{
		//return String.format("[User: %s, Passwd: %s, uid: %d, gid: %d, gecos: %s, homedir: %s, shell: %s]", pw_name, pw_passwd, pw_uid, pw_gid, pw_gecos, pw_dir, pw_shell);
		return ToStringBuilder.reflectionToString(this);
	}
}
