package control;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * 
 * @author Kseven
 *
 */

public class Ssh2 {
	private String hostname = null;
	private int port = 22;
	private String username = null;
	private String password = null;
	
	public Ssh2(String hostname, String username, String password) {
		this.hostname = hostname;
		this.username = username;
		this.password = password;
	}
	
	public Ssh2(String hostname,int port, String username, String password) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	public boolean sshShell(JSch jsch, String shellCommand) throws Exception, JSchException {
		boolean flag = true;
		Session session = jsch.getSession(username, hostname, port);
		ChannelExec channel = null;
		if (session != null) {
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect(30000);
			
			channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(shellCommand);
			channel.setInputStream(null);
//			channel.setErrStream(System.err);			
			channel.connect(1000);
			InputStream inStream = channel.getInputStream();
			InputStream errStream = channel.getErrStream();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(errStream, Charset.forName("UTF-8")));
			if ((reader.readLine()) != null) {
				flag = false;
			}
			
			reader.close();
			channel.disconnect();
			session.disconnect();
		} else {
			flag = false;
			throw new Exception("create session failed");
		}
		return flag;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
}
