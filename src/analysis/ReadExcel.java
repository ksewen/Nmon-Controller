package analysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @author Kseven
 *
 */

public class ReadExcel {
	public Workbook openWorkbook(String filePath) throws FileNotFoundException, IOException {		
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		InputStream stream = null;
		stream = new FileInputStream(filePath);
		Workbook wb = null;
		if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook(stream);
		} else {
			JOptionPane.showMessageDialog(null, "请选择正确的nmon分析结果(仅支持*.xlsx格式)!", "错误", JOptionPane.ERROR_MESSAGE);
			System.err.println("输入的文件不是正确的nmon分析结果");
		}
		return wb;
	}
}
