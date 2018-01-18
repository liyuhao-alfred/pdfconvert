package net.netca.pdfconvert.schedule;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.netca.pdfconvert.constant.PDFConstant;
import net.netca.pdfconvert.util.FileUtil;

public class TaskDeleteFile {

	private final Log log = LogFactory.getLog(getClass());

	protected void execute() throws IOException {
		FileUtil.delFolder(PDFConstant.baseFloderPath);
		FileUtil.createFile(PDFConstant.baseFloderPath + "temp");
		log.info(String.format("开始文件夹 [%s]删除", PDFConstant.baseFloderPath));
	}

}
