package gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.Container;
import java.awt.Font;
import javax.swing.border.EtchedBorder;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import control.Setup;
import gui.Analysis;
import gui.Install;
import java.awt.Color;

/**
 * 
 * @author Kseven
 *
 */

public class Home extends JFrame {
	JProgressBar progressBar = null;
	JLabel runInfo = null;
	private HashMap<String, ArrayList<String>> nodesMap = null;
	private HashMap<String, Boolean> nodeStatusMap = null;
	private HashMap<String, ArrayList<String>> isSelectedMap = null;

	public Home() {
		init();
		this.setVisible(true);
		this.setTitle("Nmon Collector");
		this.setResizable(false);
		this.setBounds(300, 150, 461, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void init() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{564, 0};
		gridBagLayout.rowHeights = new int[]{340, 20, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		final JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP);
		tab.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagConstraints gbc_tab = new GridBagConstraints();
		gbc_tab.fill = GridBagConstraints.BOTH;
		gbc_tab.insets = new Insets(0, 0, 5, 0);
		gbc_tab.gridx = 0;
		gbc_tab.gridy = 0;
		getContentPane().add(tab, gbc_tab);
		
		Container container = this.getLayeredPane();

//		tab.add(install, "install");
		
		
		JPanel progress = new JPanel();
		progress.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagConstraints gbc_progress = new GridBagConstraints();
		gbc_progress.fill = GridBagConstraints.BOTH;
		gbc_progress.gridx = 0;
		gbc_progress.gridy = 1;
		getContentPane().add(progress, gbc_progress);
		GridBagLayout gbl_progress = new GridBagLayout();
		gbl_progress.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_progress.rowHeights = new int[]{0, 0};
		gbl_progress.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_progress.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		progress.setLayout(gbl_progress);
		
		progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.insets = new Insets(0, 0, 0, 5);
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 0;
		progress.add(progressBar, gbc_progressBar);
		
		runInfo = new JLabel("Version 1.0");
		runInfo.setForeground(Color.DARK_GRAY);
		runInfo.setFont(new Font("宋体", Font.PLAIN, 11));
		runInfo.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_runInfo = new GridBagConstraints();
		gbc_runInfo.gridwidth = 7;
		gbc_runInfo.fill = GridBagConstraints.BOTH;
		gbc_runInfo.insets = new Insets(0, 0, 0, 5);
		gbc_runInfo.gridx = 1;
		gbc_runInfo.gridy = 0;
		progress.add(runInfo, gbc_runInfo);
		
		CheckBoxTree nodesList = null;
		try {
			nodesList = new CheckBoxTree("null");
			nodesMap = nodesList.getNodesMap();
			nodeStatusMap = nodesList.getNodeStatusMap();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final JScrollPane nodesListScrollPane = new JScrollPane(nodesList);
		nodesListScrollPane.setBounds(0, 0, 257, 236);
		final JButton reloadButton = new JButton("重  载");
		final JPanel nodesPanel = new JPanel();
		nodesPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		nodesPanel.setBounds(10, 60, 257, 236);
		add(nodesPanel);		
		nodesPanel.setLayout(null);
		nodesPanel.add(nodesListScrollPane);
		
		JButton installButton = new JButton("部  署");
		installButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isSelectedMap = new HashMap<String, ArrayList<String>>();
				for (String key : nodeStatusMap.keySet()) {
					if (nodeStatusMap.get(key)) {
						isSelectedMap.put(key, nodesMap.get(key));
					}
				}
				
//				System.out.println(isSelectedMap.size());
				for (String ip : isSelectedMap.keySet()) {
					if (isSelectedMap.get(ip) != null) {
						Setup setup = new Setup(ip, isSelectedMap.get(ip));
						System.out.println(ip + "; " + isSelectedMap.get(ip));
					}
				}
				System.out.println("======================");
			}
		});
		
		final Install install = new Install(progressBar, runInfo, reloadButton, installButton);	
		install.add(nodesPanel);
		tab.add(install, "Install");
		
		reloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent reload) {
				CheckBoxTree reloadNodeList = null;
				try {
					reloadNodeList = new CheckBoxTree(install.getXmlPath());
					nodesMap = reloadNodeList.getNodesMap();
					nodeStatusMap = reloadNodeList.getNodeStatusMap();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JScrollPane reNodesListScrollPane = new JScrollPane(reloadNodeList);
				reNodesListScrollPane.setBounds(0, 0, 257, 236);
				nodesPanel.removeAll();
				nodesPanel.setLayout(null);
				nodesPanel.add(reNodesListScrollPane);
				nodesPanel.updateUI();
			}
		});
		
		Analysis analysis = new Analysis(progressBar, runInfo);
		tab.add(analysis, "Analysis");
	}
}
