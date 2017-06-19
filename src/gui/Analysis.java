package gui;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import analysis.GetResult;
import analysis.ReadFile;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import java.awt.BorderLayout;

/**
 * 
 * @author Kseven
 *
 */

public class Analysis extends JPanel {
	private final Font font = new Font("黑体", Font.PLAIN, 12);
	private JFileChooser chooser = new JFileChooser("./Analysis/source");
	private JProgressBar progressbar = null;
	private JLabel runInfo = null;
	private final SimpleDateFormat fileNameSdf = new SimpleDateFormat("HHmmss");
	boolean isListFile = false;
	String sourcePathStr = null;
	String targetPathStr = "./result";
	String excelName = "nmonresult_" + fileNameSdf.format(new Date().getTime()) + ".xlsx";
	int columnWidth = 15; // default 15
	int rowHeight = 85; // default 85
	private JTextField targetFileNameText;
	private JTextField rowHeightText;
	private JTextField columnWidthText;
	private List<String> excelList = null;
	Vector<Object> fileListData = null;

	public Analysis(final JProgressBar progressbar, final JLabel runInfo) {
		this.progressbar = progressbar;
		this.runInfo = runInfo;
		setLayout(null);

		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		mainPanel.setToolTipText("");
		mainPanel.setBounds(10, 10, 430, 110);
		add(mainPanel);
		mainPanel.setLayout(null);

		final JLabel sourcePath = new JLabel();
		sourcePath.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		sourcePath.setForeground(Color.DARK_GRAY);
		sourcePath.setFont(font);
		sourcePath.setText("请选择源文件存放路径...");
		sourcePath.setBounds(22, 11, 214, 24);
		mainPanel.add(sourcePath);

		final JLabel targetPath = new JLabel();
		targetPath.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		targetPath.setForeground(Color.DARK_GRAY);
		targetPath.setFont(font);
		targetPath.setText("请选择目标文件存放路径...");
		targetPath.setBounds(22, 45, 214, 24);
		mainPanel.add(targetPath);

		final JButton startButton = new JButton("Start!");
		startButton.setEnabled(false);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent argStart) {
				boolean exceptionflag = false;
				if (targetPath.getText().equals("请选择目标文件存放路径...")) {
					JOptionPane.showMessageDialog(null, "未设置目标路径,默认路径为Nmon Controller安装目录下的result目录", "提示",
							JOptionPane.WARNING_MESSAGE);
				} else {
					targetPathStr = targetPath.getText();
				}

				if (targetFileNameText.getText().equals("nmonresult_HHmmss")) {
					JOptionPane.showMessageDialog(null, "未设置目标文件名,默认文件名为:nmonresult_当前时间(格式HHmmss).xlsx", "提示",
							JOptionPane.WARNING_MESSAGE);
				} else {
					excelName = targetFileNameText.getText();
					String inputFileType = excelName.substring(excelName.lastIndexOf(".") + 1, excelName.length());
					if (!inputFileType.equals("xlsx")) {
						excelName = excelName + ".xlsx";
					}
				}

				if (!rowHeightText.getText().equals(null)) {
					try {
						if (Integer.parseInt(rowHeightText.getText()) == 0) {
							JOptionPane.showMessageDialog(null, "未设置行高,默认行高为85", "提示", JOptionPane.WARNING_MESSAGE);
						} else {
							rowHeight = Integer.parseInt(rowHeightText.getText());
						}
					} catch (Exception e) {
						exceptionflag = true;
					}
				}

				if (!columnWidthText.getText().equals(null)) {
					try {
						if (Integer.parseInt(columnWidthText.getText()) == 0) {
							JOptionPane.showMessageDialog(null, "未设置列宽,默认列宽为85", "提示", JOptionPane.WARNING_MESSAGE);
						} else {
							columnWidth = Integer.parseInt(columnWidthText.getText());
						}
					} catch (Exception e) {
						exceptionflag = true;
					}
				}

				if (exceptionflag) {
					JOptionPane.showMessageDialog(null, "请输入合法的正整数!", "错误", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				progressbar.setMinimum(1);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (isListFile) {
							List<String> excelSelectList = new ArrayList<String>();
							for (int i = 0; i < fileListData.size(); i++) {
								Vector<Object> fileListItem = (Vector<Object>) fileListData.get(i);
								if ((Boolean) fileListItem.get(0) == true) {
									excelSelectList.add((String) fileListItem.get(1));
								}
							}
							GetResult getResult = new GetResult(excelSelectList, sourcePathStr, targetPathStr, excelName, columnWidth, rowHeight, progressbar, runInfo);
							getResult.execute();
						} else {
							ReadFile readFile = new ReadFile();
							try {
								excelList = readFile.get(sourcePathStr);
								GetResult getResult = new GetResult(excelList, sourcePathStr, targetPathStr, excelName,
										columnWidth, rowHeight, progressbar, runInfo);
								getResult.execute();
							} catch (Exception e) {
								JOptionPane.showMessageDialog(null, "源目录内找不到合法的xlsx文件!", "错误",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
					}
				});
			}
		});
		startButton.setFont(new Font("Calibri", Font.BOLD, 20));
		startButton.setBounds(327, 11, 81, 58);
		mainPanel.add(startButton);
		
		final JPanel fileListPanel = new JPanel();
		fileListPanel.setBounds(22, 39, 384, 123);
		fileListPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		fileListPanel.setLayout(new BorderLayout(0, 0));

		final JButton listFileButton = new JButton("列出文件");
		listFileButton.setEnabled(false);
		final JButton sourcePathButton = new JButton("浏  览");
		sourcePathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (arg0.getSource() == sourcePathButton) {
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int intRetVal = chooser.showOpenDialog(null);
					if (intRetVal == JFileChooser.APPROVE_OPTION) {
						String filePath = chooser.getSelectedFile().getPath();
						sourcePath.setText(filePath);
						sourcePathStr = filePath;
						if (!sourcePathStr.equals("请选择源文件存放路径...")) {
							startButton.setEnabled(true);
							listFileButton.setEnabled(true);
						}
						fileListPanel.removeAll();
						fileListPanel.repaint();
						isListFile = false;
					} else if (intRetVal == 1) {
						return;
					}
				}
			}
		});
		sourcePathButton.setFont(font);
		sourcePathButton.setBounds(247, 11, 73, 24);
		mainPanel.add(sourcePathButton);

		final JButton targetPathButton = new JButton("浏  览");
		targetPathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg1) {
				if (arg1.getSource() == targetPathButton) {
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int intRetVal = chooser.showOpenDialog(null);
					if (intRetVal == JFileChooser.APPROVE_OPTION) {
						String filePath = chooser.getSelectedFile().getPath();
						targetPath.setText(filePath);
						targetPathStr = filePath;
					} else if (intRetVal == 1) {
						return;
					}
				}
			}
		});
		targetPathButton.setFont(font);
		targetPathButton.setBounds(246, 45, 73, 24);
		mainPanel.add(targetPathButton);

		JLabel targetFileNameLabel = new JLabel("目标文件名");
		targetFileNameLabel.setFont(font);
		targetFileNameLabel.setBounds(22, 79, 66, 15);
		mainPanel.add(targetFileNameLabel);

		targetFileNameText = new JTextField();
		targetFileNameText.setText("nmonresult_HHmmss");
		targetFileNameText.setForeground(Color.DARK_GRAY);
		targetFileNameText.setBounds(86, 76, 119, 21);
		mainPanel.add(targetFileNameText);
		targetFileNameText.setColumns(10);
		targetFileNameText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {

			}

			@Override
			public void focusGained(FocusEvent e) {
				targetFileNameText.setText("");
			}
		});

		JLabel rowHeightLabel = new JLabel("单元格行高");
		rowHeightLabel.setFont(new Font("黑体", Font.PLAIN, 12));
		rowHeightLabel.setBounds(210, 79, 66, 15);
		mainPanel.add(rowHeightLabel);

		rowHeightText = new JTextField();
		rowHeightText.setText("85");
		rowHeightText.setHorizontalAlignment(SwingConstants.CENTER);
		rowHeightText.setForeground(Color.DARK_GRAY);
		rowHeightText.setColumns(10);
		rowHeightText.setBounds(273, 76, 35, 21);
		mainPanel.add(rowHeightText);
		rowHeightText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				rowHeightText.setText("");
			}
		});

		JLabel columnWidthLabel = new JLabel("单元格列宽");
		columnWidthLabel.setFont(new Font("黑体", Font.PLAIN, 12));
		columnWidthLabel.setBounds(310, 79, 66, 15);
		mainPanel.add(columnWidthLabel);

		columnWidthText = new JTextField();
		columnWidthText.setText("15");
		columnWidthText.setHorizontalAlignment(SwingConstants.CENTER);
		columnWidthText.setForeground(Color.DARK_GRAY);
		columnWidthText.setColumns(10);
		columnWidthText.setBounds(373, 76, 35, 21);
		mainPanel.add(columnWidthText);
		columnWidthText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {

			}

			@Override
			public void focusGained(FocusEvent e) {
				columnWidthText.setText("");
			}
		});
		this.setBounds(300, 150, 449, 310);

		final JPanel fileList = new JPanel();
		fileList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		fileList.setBounds(10, 126, 430, 170);
		add(fileList);
		fileList.setLayout(null);
		
		fileList.add(fileListPanel);
		
		listFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent listFile) {
				ReadFile readFile = new ReadFile();
				try {
					excelList = readFile.get(sourcePathStr);
					if (excelList.size() > 0) {
						fileListPanel.removeAll();
						fileListPanel.repaint();
						isListFile = true;
						
						//生成文件列表
						//生成滚动条
						JScrollPane fileListScrollPane = new JScrollPane();
						fileListScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
						fileListPanel.add(fileListScrollPane, BorderLayout.WEST);
						//vector处理列表数据
						Vector<String> headers = new Vector<String>();
						headers.addElement("checkbox");
						headers.addElement("file name");
						fileList.updateUI();
						fileListData = new Vector<Object>();
						for (int i = 0; i < excelList.size(); i++) {
							Vector<Object> dataRow = new Vector<Object>();
							dataRow.add(new Boolean(true));
							dataRow.add(excelList.get(i));
							fileListData.add(dataRow);
						}
						//创建表格存放数据
						JTable fileListTable = new JTable(fileListData, headers){
							//重写isCellEditable方法，使单元格不可编辑
							public boolean isCellEditable(int row, int column) {
								boolean flag = false;
								if (column == 0) {
									flag = true;
								}
								return flag;
							};
						};
						fileListTable.setShowGrid(false);
						fileListTable.setBackground(new Color(238, 238, 238));
						//设置表格大小
						fileListTable.setPreferredScrollableViewportSize(new Dimension(362, 123));
						//设置表头不可见
						fileListTable.getTableHeader().setVisible(false);
						DefaultTableCellRenderer headersRenderer = new DefaultTableCellRenderer();
						headersRenderer.setPreferredSize(new Dimension(0, 0));
						fileListTable.getTableHeader().setDefaultRenderer(headersRenderer);
						
						//设置checkbox存放列居中
						DefaultTableCellRenderer checkRenderer = new DefaultTableCellRenderer();
						checkRenderer.setHorizontalAlignment(JTextField.CENTER);
						fileListTable.getColumn("checkbox").setCellRenderer(checkRenderer);
						//设置checkbox存放列大小
						TableColumn checkColumn = fileListTable.getColumnModel().getColumn(0);
						checkColumn.setPreferredWidth(25);
						checkColumn.setMaxWidth(25);
						checkColumn.setMinWidth(25);
						checkColumn.setCellEditor(fileListTable.getDefaultEditor(Boolean.class));
						checkColumn.setCellRenderer(fileListTable.getDefaultRenderer(Boolean.class));
						
						
						fileListScrollPane.setViewportView(fileListTable);
						fileList.updateUI();
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "源目录内找不到合法的xlsx文件!", "错误", JOptionPane.ERROR_MESSAGE);
					System.err.println("源目录" + sourcePathStr + "内找不到*.xlsx文件，退出");
					return;
				}
			}
		});

		listFileButton.setFont(font);
		listFileButton.setBounds(22, 10, 93, 23);
		fileList.add(listFileButton);

		JLabel listFileHelp = new JLabel("列出文件后勾选需要分析的文件(不点列出默认全选)");
		listFileHelp.setFont(font);
		listFileHelp.setBounds(125, 14, 279, 15);
		fileList.add(listFileHelp);
	}
}