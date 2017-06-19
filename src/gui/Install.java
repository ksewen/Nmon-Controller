package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Color;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

/**
 * 
 * @author Kseven
 *
 */

public class Install extends JPanel{
	private String xmlPath = "./src/config/Nodes.xml";
	private JFileChooser chooser = new JFileChooser("./src/config");
	public Install(final JProgressBar progressbar, final JLabel runInfo, final JButton reloadButton, final JButton installButton) {
		setLayout(null);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		controlPanel.setBounds(10, 10, 430, 44);
		add(controlPanel);
		controlPanel.setLayout(null);
		
		final JLabel pathLabel = new JLabel();
		pathLabel.setText("请选择配置文件...");
		pathLabel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pathLabel.setForeground(Color.DARK_GRAY);
		pathLabel.setFont(new Font("黑体", Font.PLAIN, 13));
		pathLabel.setBounds(10, 10, 175, 24);
		controlPanel.add(pathLabel);
		
		final JButton editButton = new JButton("编  辑");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String openConfig = "notepad " + xmlPath;
				if (!pathLabel.getText().equals("请选择配置文件...")) {
					xmlPath = pathLabel.getText();
					openConfig = "notepad " + xmlPath;
				}
				try {
					Process notepadConf = Runtime.getRuntime().exec(openConfig);
				} catch (IOException e) {
					System.out.println("打开配置文件失败!");
					e.printStackTrace();
				}
			}
		});
		editButton.setFont(new Font("黑体", Font.PLAIN, 12));
		editButton.setBounds(269, 10, 73, 24);
		controlPanel.add(editButton);
		
//		reloadButton.setEnabled(false);
		reloadButton.setFont(new Font("黑体", Font.PLAIN, 12));
		reloadButton.setBounds(347, 10, 73, 24);
		controlPanel.add(reloadButton);
		
		final JButton browserButton = new JButton("浏  览");
		browserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent chooseFile) {
				if (chooseFile.getSource() == browserButton) {
					final List<String> typeList = new ArrayList<String>();
					typeList.add("xml");
					chooser.setFileFilter(new FileFilter() {
						
						@Override
						public String getDescription() {
							return "*.xml";
						}
						
						@Override
						public boolean accept(File f) {
							if (f.isDirectory()) {
								return true;
							}
							String name = f.getName();
							int p = name.lastIndexOf(".");
							if (p == -1) {
								return false;
							}
							String suffix = name.substring(p + 1).toLowerCase();
							return typeList.contains(suffix);
						}
					});
					int intRetVal = chooser.showOpenDialog(null);
					if (intRetVal == JFileChooser.APPROVE_OPTION) {
						xmlPath = chooser.getSelectedFile().getPath();
						pathLabel.setText(xmlPath);
					} else if (intRetVal == 1) {
						return;
					}
				}
			}
		});
		browserButton.setFont(new Font("黑体", Font.PLAIN, 12));
		browserButton.setBounds(191, 10, 73, 24);
		controlPanel.add(browserButton);
		
		JPanel logPanel = new JPanel();
		logPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		logPanel.setBounds(272, 60, 168, 236);
		add(logPanel);
		logPanel.setLayout(null);
		
		installButton.setFont(new Font("黑体", Font.PLAIN, 12));
		installButton.setBounds(8, 7, 73, 24);
		logPanel.add(installButton);
		
		JButton uninstallButton = new JButton("卸  载");
		uninstallButton.setFont(new Font("黑体", Font.PLAIN, 12));
		uninstallButton.setBounds(87, 7, 73, 24);
		logPanel.add(uninstallButton);
		
		JTextArea logArea = new JTextArea();
		logArea.setFont(new Font("黑体", Font.PLAIN, 12));
		logArea.setLineWrap(true);
		logArea.setText("使用说明:\n1. 选择配置文件(仅支持指定格式的xml文件)并单击“重载”按钮重新加载节点列表(默认加载./config/Nodes.xml)。\n2. 勾选需要操作的节点进行nmon的部署和卸载。\n3. 请开启目标服务器的sshd服务，并设置防火墙允许相应端口通过。");
		
		JScrollPane logScrollPane = new JScrollPane(logArea);
		logScrollPane.setBounds(8, 35, 153, 194);
		
		logPanel.add(logScrollPane);
	}
	
	public String getXmlPath() {
		return xmlPath;
	}
}
