package gui;

import java.awt.Checkbox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CheckBoxTree extends JTree {
	private String xmlPath = "./src/config/Nodes.xml";
	
	private HashMap<String, ArrayList<String>> nodesMap = new HashMap<String, ArrayList<String>>();
	private HashMap<String, Boolean> nodeStatusMap = new HashMap<String, Boolean>();
	
	public CheckBoxTree(String path) throws ParserConfigurationException, SAXException, IOException {
		File xmlFile = null;
		if (path.equals("null")) {
			xmlFile = new File(xmlPath);
		} else {
			xmlFile = new File(path);
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlFile);
		Element element = doc.getDocumentElement();
		
		CheckBoxTreeNode rootNode = new CheckBoxTreeNode("System");
		
		if (element.getNodeType() == Node.ELEMENT_NODE) {
			if (element.hasChildNodes()) {
				NodeList sysNodeList = element.getChildNodes();
				for (int i = 0; i < sysNodeList.getLength(); i++) {
					Node sysNode = sysNodeList.item(i);
					if (sysNode.getNodeType() == Node.ELEMENT_NODE) {
						CheckBoxTreeNode sysCheckBoxNode = new CheckBoxTreeNode(sysNode.getAttributes().getNamedItem("sysname").toString().split("\"")[1]);
						rootNode.add(sysCheckBoxNode);
						if (sysNode.hasChildNodes()) {
							NodeList serverNodeList = sysNode.getChildNodes();
							for (int j = 0; j < serverNodeList.getLength(); j++) {
								Node serverNode = serverNodeList.item(j);
								if (serverNode.getNodeType() == Node.ELEMENT_NODE) {
									CheckBoxTreeNode serverCheckBoxNode = new CheckBoxTreeNode(serverNode.getAttributes().getNamedItem("srvname").toString().split("\"")[1]);
									sysCheckBoxNode.add(serverCheckBoxNode);
									if (serverNode.hasChildNodes()) {
										NodeList nodeList = serverNode.getChildNodes();
										for (int k = 0; k < nodeList.getLength(); k++) {
											Node node = nodeList.item(k);
											if (node.getNodeType() == Node.ELEMENT_NODE) {
												CheckBoxTreeNode nodeCheckBoxNode = new CheckBoxTreeNode(node.getAttributes().getNamedItem("ip").toString().split("\"")[1]);
												nodeStatusMap.put(node.getAttributes().getNamedItem("ip").toString().split("\"")[1], false);
												ArrayList<String> nodeInfo = new ArrayList<String>();
												nodeInfo.add(node.getAttributes().getNamedItem("user").toString().split("\"")[1]);
												nodeInfo.add(node.getAttributes().getNamedItem("pwd").toString().split("\"")[1]);
												nodeInfo.add(node.getAttributes().getNamedItem("systype").toString().split("\"")[1]);
												nodeInfo.add(node.getAttributes().getNamedItem("installhome").toString().split("\"")[1]);
												nodesMap.put(node.getAttributes().getNamedItem("ip").toString().split("\"")[1], nodeInfo);
												serverCheckBoxNode.add(nodeCheckBoxNode);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		DefaultTreeModel model = new DefaultTreeModel(rootNode);
//		this.addMouseListener(new CheckBoxTreeNodeSelectionListener());
		this.addMouseListener(new MouseAdapter() {
			@Override  
		    public void mouseClicked(MouseEvent event)  
		    { 
		        JTree tree = (JTree)event.getSource();  
		        int x = event.getX();
		        int y = event.getY();
		        int row = tree.getRowForLocation(x, y);
		        TreePath path = tree.getPathForRow(row);  
		        if(path != null)  
		        {  
		            CheckBoxTreeNode node = (CheckBoxTreeNode)path.getLastPathComponent();  
		            if(node != null)  
		            {
		                boolean isSelected = !node.isSelected();
		                node.setSelected(isSelected); 
		                ((DefaultTreeModel)tree.getModel()).nodeStructureChanged(node);
		                
		                if (node.getChildCount() == 0) {
		                	nodeStatusMap.remove(node.getName().toString());
							nodeStatusMap.put(node.getName().toString(), isSelected);
						} else {
							int childrenCount = node.getChildCount();
							for (int i = 0; i < childrenCount; i++) {
								if (node.getChildAt(i).getChildCount() == 0) {
									nodeStatusMap.remove(node.getChildAt(i).toString());
									nodeStatusMap.put(node.getChildAt(i).toString(), isSelected);
								} else {
									int subChildrenCount = node.getChildAt(i).getChildCount();
									for (int j = 0; j < subChildrenCount; j++) {
										nodeStatusMap.remove(node.getChildAt(i).getChildAt(j).toString());
										nodeStatusMap.put(node.getChildAt(i).getChildAt(j).toString(), isSelected);
									}
								}
							}
						}
		            }  
		        }
		    }
		});
		this.setModel(model);
		this.setCellRenderer(new CheckBoxTreeCellRenderer());
		this.setRootVisible(false);
		this.setShowsRootHandles(true);
	}
	
	public HashMap<String, ArrayList<String>> getNodesMap() {
		return nodesMap;
	}
	
	public HashMap<String, Boolean> getNodeStatusMap() {
		return nodeStatusMap;
	}
}
