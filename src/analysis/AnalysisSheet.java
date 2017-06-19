package analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @author Kseven
 *
 */

public class AnalysisSheet {
	private XSSFWorkbook wb = null;

	public AnalysisSheet(XSSFWorkbook wb) throws Exception {
		this.wb = wb;
	}

	public String[] sheetCpuAll() throws FileNotFoundException, IOException {
		String[] reCpuAll = null;
		int sheetIndex = 5;
		int column = 1;
		reCpuAll = new String[2];
		XSSFSheet sheet = wb.getSheetAt(sheetIndex);
		if (sheet.getLastRowNum() < 3) {
			System.err.println("源文件记录数量过少,停止分析!");
			System.exit(-1);
		}
		double cpuUserTotal = 0;
		double cpuUserMax = 0;
		int count = 0;
		System.out.println("Sheet:CPU_ALL --- 当前页共有数据 : " + (sheet.getLastRowNum() - 1) + "行");
		for (int i = 1; i <= sheet.getLastRowNum() - 2; i++) {
			XSSFRow row = sheet.getRow(i);
			if (null != row) {
				XSSFCell cell = row.getCell(column);
				if (null != cell) {
					if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
						double temp = cell.getNumericCellValue();
						cpuUserTotal = cpuUserTotal + temp;
						count++;
						if (cpuUserMax < temp) {
							cpuUserMax = temp;
						}
					}
				}
			}
		}
		reCpuAll[0] = Double.toString(cpuUserMax) + "%";
		reCpuAll[1] = new DecimalFormat("#.##").format((cpuUserTotal / (count))) + "%";
		return reCpuAll;
	}

	public String[] sheetMem() throws FileNotFoundException, IOException {
		String[] reMem = null;
		int sheetIndex = 13;
		int columnB2 = 1;
		int columnF2 = 5;
		int columnK2 = 10;
		int columnN2 = 13;
		int count = 0;
		reMem = new String[2];
		XSSFSheet sheet = wb.getSheetAt(sheetIndex);
		if (sheet.getLastRowNum() < 3) {
			System.err.println("源文件记录数量过少,停止分析!");
			System.exit(-1);
		}
		double memSum = 0;
		double memMax = 0;
		System.out.println("Sheet:MEM --- 当前页共有数据 : " + (sheet.getLastRowNum() + 1) + "行");
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);
			double memUsage = 0;
			double memTotal = 0;
			if (null != row) {
				for (int j = columnB2; j <= columnN2; j++) {
					XSSFCell cell_0 = row.getCell(columnB2);
					XSSFCell cell_1 = row.getCell(columnF2);
					XSSFCell cell_2 = row.getCell(columnK2);
					XSSFCell cell_3 = row.getCell(columnN2);
					if (null != cell_0 && null != cell_1 && null != cell_2 && null != cell_3) {
						if (cell_0.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
							memUsage = cell_0.getNumericCellValue();
							memTotal = cell_0.getNumericCellValue();
							memUsage = memUsage - cell_1.getNumericCellValue();
							memUsage = memUsage - cell_2.getNumericCellValue();
							memUsage = memUsage - cell_3.getNumericCellValue();
						}
					}
				}
				double temp = memUsage / memTotal;
				memSum = memSum + temp;
				count++;
				if (memMax < temp) {
					memMax = temp;
				}
			}
		}
		reMem[0] = new DecimalFormat("#.##%").format(memMax);
		reMem[1] = new DecimalFormat("#.##%").format(memSum / count);
		return reMem;
	}

	public String[] sheetDiskSumm() throws FileNotFoundException, IOException {
		String[] reDiskSumm = null;
		int sheetIndex = 4;
		int column = 1;
		double diskAvg = 0;
		double diskMax = 0;
		reDiskSumm = new String[2];
		XSSFSheet sheet = wb.getSheetAt(sheetIndex);
		if (sheet.getLastRowNum() < 6) {
			System.err.println("源文件记录数量过少,停止分析!");
			System.exit(-1);
		}
		XSSFRow row = null;
		XSSFCell cellRead = null;
		XSSFCell cellWrite = null;
		sheet.getLastRowNum();
		System.out.println("Sheet:DISK_SUMM --- 当前页共有数据 : " + (sheet.getLastRowNum()) + "行");
		row = sheet.getRow(sheet.getLastRowNum() - 5);
		if (null != row) {
			cellRead = row.getCell(column);
			cellWrite = row.getCell(column + 1);
			if ((null != cellRead) && (null != cellWrite)) {
				diskAvg = cellRead.getNumericCellValue() + cellWrite.getNumericCellValue();
			}
		}
		row = sheet.getRow(sheet.getLastRowNum() - 3);
		if (null != row) {
			cellRead = row.getCell(column);
			cellWrite = row.getCell(column + 1);
			if ((null != cellRead) && (null != cellWrite)) {
				diskMax = cellRead.getNumericCellValue() + cellWrite.getNumericCellValue();
			}
		}
		reDiskSumm[0] = new DecimalFormat("#.##").format(diskMax);
		reDiskSumm[1] = new DecimalFormat("#.##").format(diskAvg);

		return reDiskSumm;
	}

	public String[] sheetDiskBusy() throws FileNotFoundException, IOException {
		String[] reDiskBusy = null;
		int sheetIndex = 8;
		int count = 1;
		int index = 0;
		reDiskBusy = new String[2];
		XSSFSheet sheet = wb.getSheetAt(sheetIndex);
		if (sheet.getLastRowNum() < 6) {
			System.err.println("源文件记录数量过少,停止分析!");
			System.exit(-1);
		}
		System.out.println("Sheet:DISKBUSY --- 当前页共有数据 : " + sheet.getLastRowNum() + "行");
		XSSFRow row = sheet.getRow(sheet.getLastRowNum() - 5);
		if (null != row) {
			new HashMap<Double, Double>();
			double avgBusy = 0;
			double maxBusy = 0;
			for (int i = 1; i < row.getLastCellNum(); i++) {
				XSSFCell cell = row.getCell(i);
				if (null != cell) {
					double temp = cell.getNumericCellValue();
					if (avgBusy < temp) {
						avgBusy = temp;
						index = count;
					}
					count++;
				}
			}
			row = sheet.getRow(sheet.getLastRowNum() - 3);
			if (null != row) {
				XSSFCell cell = row.getCell(index);
				if (null != cell) {
					maxBusy = cell.getNumericCellValue();
				}
				reDiskBusy[0] = new DecimalFormat("#.#").format(maxBusy);
				reDiskBusy[1] = new DecimalFormat("#.#").format(avgBusy);

			}
		}
		return reDiskBusy;
	}
}
