package control;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * 
 * @author Kseven
 *
 */

public class Sftp {
	private String hostname;
	private int port = 22;
	private String username;
	private String password;
	
	public Sftp(String hostname, String username, String password) {
		this.hostname = hostname;
		this.username = username;
		this.password = password;
	}
	
	public Sftp(String hostname, int port, String username, String password) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	public ChannelSftp connect() throws JSchException {
		ChannelSftp sftp = null;
		JSch jsch = new JSch();
		Session sshSession = jsch.getSession(username, hostname, port);
		sshSession.setPassword(password);
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		sshSession.setConfig(sshConfig);
		sshSession.connect();
		System.out.println("正在连接...");
		Channel channel = sshSession.openChannel("sftp");
		channel.connect();
		sftp = (ChannelSftp) channel;
		System.out.println("连接到 : " + hostname);
		return sftp;
	}
	
	public boolean mkdir(String directory, String dirName, ChannelSftp sftp) {
		boolean flag = true;
		try {
			sftp.mkdir(directory + "/" + dirName);
			System.out.println("创建" + directory + "/Monitor成功");
		} catch (SftpException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean upload(String directory, String uploadFile, ChannelSftp sftp) throws SftpException {
		boolean flag = true;
		sftp.cd(directory);
		File file = new File(uploadFile);
		try {
			sftp.put(new FileInputStream(file), file.getName());
			System.out.println("上传文件 " + uploadFile + " 到 " + directory);
		} catch (FileNotFoundException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) throws SftpException {
		boolean flag = true;
		sftp.cd(directory);
		File file = new File(saveFile);
		try {
			sftp.get(downloadFile, new FileOutputStream(file));
			System.out.println("下载文件 " + downloadFile + " success");
		} catch (FileNotFoundException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean delete(String directory, String deleteFile, ChannelSftp sftp) throws SftpException {
		boolean flag = true;
		sftp.cd(directory);
		try {
			sftp.rm(deleteFile);
			System.out.println("删除文件 " + deleteFile + " success");
		} catch (SftpException e) {
			flag = false;
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public boolean rmdir(String directory, String dirName, ChannelSftp sftp) {
		boolean flag = true;
		try {
			sftp.rmdir(directory + "/Monitor");
			System.out.println("删除" + directory + "/Monitor成功");
		} catch (SftpException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	public Vector<String> listFile(String directory, ChannelSftp sftp) throws SftpException {
		return sftp.ls(directory);
	}
	
	public void disConnected(ChannelSftp sftp) throws JSchException {
		Session session = sftp.getSession();
		if (sftp.getSession() != null) {
			session.disconnect();
		}
		System.out.println("断开连接");
	}
}
