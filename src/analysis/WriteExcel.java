package analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @author Kseven
 *
 */

@SuppressWarnings("deprecation")
public class WriteExcel {
	public void doWrite(Map<String, Map<String, String[]>> result, List<String> excelList, String resultPath, String excelName, int columnWidth, int rowHeight) {
		File f = new File(resultPath + "/" + excelName);
		if (f.exists()) {
			f.delete();
			System.err.println("删除已存在的重复文件");
		}

		int fileCount = result.size();
		boolean isCreateSuccess = false;

		XSSFWorkbook workbook = null;
		workbook = new XSSFWorkbook();
		SetCellStyle scs = new SetCellStyle(workbook);
		if (workbook != null) {
			XSSFSheet sheet = workbook.createSheet("nmon_result");

			XSSFRow row_0 = sheet.createRow(0);
			;
			XSSFCell cell_00 = row_0.createCell(0, Cell.CELL_TYPE_STRING);
			cell_00.setCellValue("nmon结果汇总");
			cell_00.setCellStyle(scs.styleTitle());
			CellRangeAddress regionServerName_00 = new CellRangeAddress(0, 0, (short) 0, (short) (fileCount * 3 - 1));
			sheet.addMergedRegion(regionServerName_00);

			for (int i = 0; i < (fileCount * 3); i++) {
				sheet.setColumnWidth(i, columnWidth * 256);
			}

			XSSFRow row_1 = sheet.createRow(1);
			XSSFRow row_2 = sheet.createRow(2);
			XSSFRow row_3 = sheet.createRow(3);
			sheet.getRow(3).setHeightInPoints(rowHeight);
			for (int i = 0; i < fileCount; i++) {
				String fileName = excelList.get(i);
				Map<String, String[]> nmonResult = result.get(fileName);
					XSSFCell cell_10 = row_1.createCell((i * 3), Cell.CELL_TYPE_STRING);
					String title = fileName.split("nmon")[0];
					cell_10.setCellValue(title.substring(0, title.length() - 1));
					CellRangeAddress regionServerName_10 = new CellRangeAddress(1, 1, (short) (i * 3),
							(short) (i * 3 + 2));
					sheet.addMergedRegion(regionServerName_10);
					cell_10.setCellStyle(scs.styleHeader());
					XSSFCell cell_11 = row_1.createCell((i * 3 + 1), Cell.CELL_TYPE_STRING);
					cell_11.setCellStyle(scs.styleHeader());
					XSSFCell cell_12 = row_1.createCell((i * 3 + 2), Cell.CELL_TYPE_STRING);
					cell_12.setCellStyle(scs.styleHeader());

					XSSFCell cell_20 = row_2.createCell(i * 3, Cell.CELL_TYPE_STRING);
					cell_20.setCellValue("CPU%");
					cell_20.setCellStyle(scs.styleHeader());
					XSSFCell cell_21 = row_2.createCell((i * 3 + 1), Cell.CELL_TYPE_STRING);
					cell_21.setCellValue("MEM%");
					cell_21.setCellStyle(scs.styleHeader());
					XSSFCell cell_22 = row_2.createCell((i * 3 + 2), Cell.CELL_TYPE_STRING);
					cell_22.setCellValue("I/O");
					cell_22.setCellStyle(scs.styleHeader());

					XSSFCell cell_30 = row_3.createCell((i * 3), Cell.CELL_TYPE_STRING);
					cell_30.setCellValue("avg:" + nmonResult.get("CPU_ALL")[1] + "\nmax:" + nmonResult.get("CPU_ALL")[0]);
					cell_30.setCellStyle(scs.styleData());
					XSSFCell cell_31 = row_3.createCell((i * 3 + 1), Cell.CELL_TYPE_STRING);
					cell_31.setCellValue("avg:" + nmonResult.get("MEM")[1] + "\nmax:" + nmonResult.get("MEM")[0]);
					cell_31.setCellStyle(scs.styleData());
					XSSFCell cell_32 = row_3.createCell((i * 3 + 2), Cell.CELL_TYPE_STRING);
					cell_32.setCellValue("avg(R/W):" + nmonResult.get("DISK_SUMM")[1] + "\nmax(R/W):"
							+ nmonResult.get("DISK_SUMM")[0] + "\navg(%Busy)" + nmonResult.get("DISKBUSY")[1] + "\nmax(%Busy)"
							+ nmonResult.get("DISKBUSY")[0]);
					cell_32.setCellStyle(scs.styleData());
			}
		}
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(resultPath + "/" + excelName);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "创建输出流失败!", "错误", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		try {
			workbook.write(stream);
			stream.flush();
			stream.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "创建" + resultPath + "/" + excelName + "文件失败!", "错误",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		isCreateSuccess = true;
		if (isCreateSuccess) {
			System.out.println("======================================");
			System.out.println("nmon结果处理成功,已输出到 : " + resultPath + "/" + excelName);
			System.out.println("======================================");
		} else {
			System.err.println("======================================");
			System.err.println("nmon结果处理失败，请检查源文件后重试");
			System.err.println("======================================");
		}
		JOptionPane.showMessageDialog(null, "成功生成结果文件 : " + resultPath + "/" + excelName);
	}
}
