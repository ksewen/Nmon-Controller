package control;

import java.util.ArrayList;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

/**
 * 
 * @author Kseven
 *
 */

public class Setup {
	private String hostname = null;
	private int port = 22;
	private String username = null;
	private String password = null;
	private String shellCommand = null;
	
	public Setup(String key, ArrayList<String> nodeStatus) {
		this.hostname = key;
		this.username = nodeStatus.get(0);
		this.password = nodeStatus.get(1);
		this.shellCommand = nodeStatus.get(3);
	}
	
	public Boolean install(JSch jsch, Ssh2 s2, String targetPath) throws JSchException, Exception {
		boolean flag = true;
		flag = s2.sshShell(jsch, "ls -l ~/Monitor\n");
		if (!flag) {
			//Monitor目录不存在，可以继续安装
			flag = s2.sshShell(jsch, "mkdir ~/Monitor\n");
			if (flag) {
				Sftp sftp = new Sftp(s2.getHostname(), s2.getPort(), s2.getUsername(), s2.getPassword());
				ChannelSftp csftp = sftp.connect();
				flag = sftp.upload(targetPath + "/Monitor", "./nmon/linux/nmon", csftp);
				if (flag) {
					System.out.println("部署成功");
				} else {
					throw new Exception("setup failed");
				}
			}	
		} else {
			//Monitor目录已存在，先将原来的目录删除
			flag = s2.sshShell(jsch, "rm -rf ~/Monitor\n");
			if (flag) {
				System.out.println("删除成功，请重新运行setup方法");
			} else {
				throw new Exception("delete Monitor error");
			}
		}
		return flag;
	}
	
	public Boolean uninstall(JSch jsch, Ssh2 s2) throws JSchException, Exception {
		boolean flag = true;
		flag = s2.sshShell(jsch, "rm -rf ~/Monitor\n");
		return flag;
	}
}
