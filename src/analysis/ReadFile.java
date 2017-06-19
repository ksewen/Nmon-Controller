package analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Kseven
 *
 */

public class ReadFile {
	public List<String> get(String filePath) throws Exception {
		String[] fileList = null;
		List<String> excelList = null;
		if (filePath != null) {
			excelList = new ArrayList<String>();
			File file = new File(filePath);
			File[] list = file.listFiles();
			if (list.length > 0) {
				fileList = new String[list.length];
				for (int i = 0; i < list.length; i++) {
					if (list[i].isFile()) {
						fileList[i] = list[i].getName();
					}
				}
				
				for (int i = 0; i < fileList.length; i++) {
					String fileType = fileList[i].substring(fileList[i].lastIndexOf(".") + 1, fileList[i].length());
					if (fileType.equals("xlsx")) {
						excelList.add(fileList[i]);
					}
				}
			}
		}
		return excelList;
	}
}