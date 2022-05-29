package entities;

public class UnixPasswd
{
	public String pw_name;       /* Benutzername */
	public String pw_passwd;     /* Passwort des Benutzers */
	public int pw_uid;        /* Benutzerkennung */
	public int pw_gid;        /* Gruppenkennung */
	public String pw_gecos;      /* Benutzerinformationen */
	public String pw_dir;        /* Home-Verzeichnis */
	public String pw_shell;
	
	public String toString()
	{
		return String.format("[User: %s, Passwd: %s, uid: %d, gid: %d, gecos: %s, homedir: %s, shell: %s]", pw_name, pw_passwd, pw_uid, pw_gid, pw_gecos, pw_dir, pw_shell);
	}
}
