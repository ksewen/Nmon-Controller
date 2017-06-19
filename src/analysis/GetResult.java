package analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @author Kseven
 *
 */

public class GetResult extends SwingWorker<List<String>, String> {
	private int columnWidth = 0; // default 15
	private int rowHeight = 0; // default 85
	private List<String> excelList = null;
	private String sourcePath = null;
	private String resultPath = null;
	private String excelName = null;
	private JProgressBar progressBar = null;
	private JLabel labelInfo = null;

	public GetResult(List<String> excelList, String sourcePath, String resultPath, String excelName, int columnWidth, int rowHeight, JProgressBar progressBar,
			 JLabel labelInfo) {
		this.excelList = excelList;
		this.sourcePath = sourcePath;
		this.resultPath = resultPath;
		this.columnWidth = columnWidth;
		this.rowHeight = rowHeight;
		this.progressBar = progressBar;
		this.labelInfo = labelInfo;
		if (!excelName.equals(null)) {
			this.excelName = excelName;
		}
	}

	@Override
	protected List<String> doInBackground() throws Exception {
		progressBar.setMaximum(excelList.size() + 1);
		labelInfo.setVisible(true);
		labelInfo.setText("Loading...");
		List<String> disFileList = new ArrayList<String>();
		Map<String, Map<String, String[]>> result = new HashMap<String, Map<String, String[]>>();
		for (int i = 0; i < excelList.size(); i++) {
			String fileName = excelList.get(i);
			String filePath = sourcePath + "/" + fileName;
			ReadExcel readExcel = new ReadExcel();
			XSSFWorkbook workbook = (XSSFWorkbook) readExcel.openWorkbook(filePath);
			Map<String, String[]> resultItem = null;
			if (workbook != null) {
				System.out.println("----------------------------------------------------");
				System.out.println("正在分析 : " + filePath);
				System.out.println("----------------------------------------------------");
				resultItem = new HashMap<String, String[]>();
				AnalysisSheet analysis = new AnalysisSheet(workbook);
				resultItem.put("CPU_ALL", analysis.sheetCpuAll());
				resultItem.put("MEM", analysis.sheetMem());
				resultItem.put("DISK_SUMM", analysis.sheetDiskSumm());
				resultItem.put("DISKBUSY", analysis.sheetDiskBusy());
			} else {
				throw new Exception("读取workbook时发生错误");
			}
			result.put(fileName, resultItem);
			disFileList.add(filePath);
			publish("进度:" + (i + 1) + "/" + excelList.size() + ":" + fileName);
		}
		if (result.size() > 0) {
			WriteExcel writeExcel = new WriteExcel();
			writeExcel.doWrite(result, excelList, resultPath, excelName, columnWidth, rowHeight);
		} else {
			System.err.println("读取nmon分析结果时发生错误");
		}
		progressBar.setValue(0);
		labelInfo.setText("Version 1.0");
		return disFileList;
	}

	@Override
	protected void process(List<String> chunks) {
		String text = chunks.get(chunks.size() - 1);
		int jpbValue = Integer.parseInt(text.substring(text.indexOf(":") + 1, text.indexOf("/")).trim());
		progressBar.setValue(jpbValue + 1);
		labelInfo.setText(text);
	}

}
