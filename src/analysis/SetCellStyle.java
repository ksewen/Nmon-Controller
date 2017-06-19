package analysis;

import java.awt.Color;

/**
 * 
 * @author Kseven
 *
 */

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SetCellStyle {
	private XSSFWorkbook workbook = null;

	public SetCellStyle(XSSFWorkbook workbook) {
		if (workbook != null) {
			this.workbook = workbook;
		} else {
			System.err.println("输入的工作表为空");
		}
	}

	public XSSFCellStyle styleTitle() {
		XSSFCellStyle cellStyle = null;
		if (workbook != null) {
			cellStyle = workbook.createCellStyle();
			XSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 18);
			font.setFontName("黑体");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			cellStyle.setFont(font);
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
			cellStyle.setFillForegroundColor(new XSSFColor(new Color(83, 142, 213)));
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		} else {
			System.err.println("生成样式发生错误！程序终止");
		}
		return cellStyle;
	}

	public XSSFCellStyle styleHeader() {
		XSSFCellStyle cellStyle = null;
		if (workbook != null) {
			cellStyle = workbook.createCellStyle();
			XSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 10);
			font.setFontName("宋体");
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			cellStyle.setFont(font);
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
			cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cellStyle.setWrapText(true);
		} else {
			System.err.println("生成样式发生错误！程序终止");
		}
		return cellStyle;
	}

	public XSSFCellStyle styleData() {
		XSSFCellStyle cellStyle = null;
		if (workbook != null) {
			cellStyle = workbook.createCellStyle();
			XSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 10);
			font.setFontName("宋体");
			cellStyle.setFont(font);
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
			cellStyle.setFillForegroundColor(new XSSFColor(new Color(197, 217, 241)));
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cellStyle.setWrapText(true);
		} else {
			System.err.println("生成样式发生错误！程序终止");
		}
		return cellStyle;
	}
}
