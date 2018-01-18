package net.netca.pdfconvert.api.openoffice;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

import net.netca.pdfconvert.constant.PDFConstant;
import net.netca.pdfconvert.dto.ConvertDto;
import net.netca.pdfconvert.dto.FileDataDto;
import net.netca.pdfconvert.exception.PDFConvertException;

public class Office {
	private static Log log = LogFactory.getLog(Office.class);
	private ConvertDto convertDto;

	private static OfficeManager officeManager;

	public Office(FileDataDto fileDataDto) {
		if (fileDataDto == null) {
			throw new PDFConvertException("传入数据为空！");
		}

		if (StringUtils.isEmpty(fileDataDto.getSourceExtension())) {
			throw new PDFConvertException("源文件扩展名为空！");
		}
		if (StringUtils.isEmpty(fileDataDto.getTargetExtension())) {
			throw new PDFConvertException("目的文件扩展名为空！");
		}
		

		String tempSourceExtension = "." + fileDataDto.getSourceExtension().replaceAll("\\.", "");
		if (!PDFConstant.openoffice.contains(tempSourceExtension.toLowerCase())) {
			throw new PDFConvertException("源文件扩展名非法！");
		}

		String tempTargetExtension = "." + fileDataDto.getTargetExtension().replaceAll("\\.", "");
		if (!PDFConstant.pdf.contains(tempTargetExtension.toLowerCase())) {
			throw new PDFConvertException("目的文件扩展名非法！");
		}

		convertDto = PDFConstant.formatConvertDto(fileDataDto, tempSourceExtension, tempTargetExtension);
	}

	public boolean convert2PDF_win() {
		try {
			long start = System.currentTimeMillis();
			startService();
			log.info("进行文档转换:" + convertDto.source);
			log.info("文档转换到:" + convertDto.target);
			OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
			converter.convert(new File(convertDto.source), new File(convertDto.target));
			stopService();
			long end = System.currentTimeMillis();
			log.info("转换完成,用时：" + (end - start) + "ms.");
			convertDto.timeUse = (end - start) + "ms";
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PDFConvertException("文件转换失败！" + e.getMessage());
		}
	}

	public boolean convert2PDF_lunix() {
		long start = System.currentTimeMillis();
		try {
			log.info("进行文档转换:" + convertDto.source);
			log.info("文档转换到:" + convertDto.target);
			OpenOfficeConnection connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);
			connection.connect();

			DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
			converter.convert(new File(convertDto.source), new File(convertDto.target));

			connection.disconnect();
			long end = System.currentTimeMillis();
			log.info("转换完成,用时：" + (end - start) + "ms.");
			convertDto.timeUse = (end - start) + "ms";
			return true;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new PDFConvertException("文件转换失败！" + e.getMessage());
		}
	}

	public void startService() {
		DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
		try {
			log.info("准备启动服务....");
			configuration.setOfficeHome(PDFConstant.openoffice_home);// 设置OpenOffice.org安装目录
			configuration.setPortNumbers(PDFConstant.port); // 设置转换端口，默认为8100
			configuration.setTaskExecutionTimeout(1000 * 60 * 5L);// 设置任务执行超时为5分钟
			configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);// 设置任务队列超时为24小时

			officeManager = configuration.buildOfficeManager();
			officeManager.start(); // 启动服务
			log.info("office转换服务启动成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PDFConvertException("office转换服务启动失败!" + e.getMessage());
		}
	}

	public void stopService() {
		log.info("关闭office转换服务....");
		if (officeManager != null) {
			officeManager.stop();
		}
		log.info("关闭office转换成功!");
	}

	public ConvertDto getConvertDto() {
		return convertDto;
	}

	public void setConvertDto(ConvertDto convertDto) {
		this.convertDto = convertDto;
	}

}
