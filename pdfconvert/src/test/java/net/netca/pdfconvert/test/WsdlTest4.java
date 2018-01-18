package net.netca.pdfconvert.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import net.netca.pdfconvert.constant.PDFConstant;
import net.netca.pdfconvert.dto.FileDataDto;
import net.netca.pdfconvert.util.DateUtil;
import net.netca.pdfconvert.util.FileUtil;
import net.netca.pdfconvert.wsdl.PDFConvert;

public class WsdlTest4 {

	public static void main(String[] args) throws IOException {
		String source = "";
		String sourceExtension = "";
		String targetExtension = "";

		source = "C:/Users/alfred/Desktop/pdf转换/转正汇报书.pptx";
		sourceExtension = FileUtil.getExtensionName(source);
		targetExtension = ".pdf";
		// test(source, sourceExtension, targetExtension);

		source = "C:/Users/alfred/Desktop/pdf转换/转正汇报书.ppt";
		sourceExtension = FileUtil.getExtensionName(source);
		targetExtension = ".pdf";
		// test(source, sourceExtension, targetExtension);

		source = "C:/Users/alfred/Desktop/pdf转换/列表.xlsx";
		sourceExtension = FileUtil.getExtensionName(source);
		targetExtension = ".pdf";
		// test(source, sourceExtension, targetExtension);

		source = "C:/Users/alfred/Desktop/pdf转换/列表.xls";
		sourceExtension = FileUtil.getExtensionName(source);
		targetExtension = ".pdf";
		test(source, sourceExtension, targetExtension);

		source = "C:/Users/alfred/Desktop/pdf转换/广东省电子商务认证有限公司_实习期_培养方案(JAVA2017).docx";
		sourceExtension = FileUtil.getExtensionName(source);
		targetExtension = ".pdf";
		// test(source, sourceExtension, targetExtension);

		source = "C:/Users/alfred/Desktop/pdf转换/广东省电子商务认证有限公司_实习期_培养方案(JAVA2017).doc";
		sourceExtension = FileUtil.getExtensionName(source);
		targetExtension = ".pdf";
		// test(source, sourceExtension, targetExtension);

		source = "C:/Users/alfred/Desktop/pdf转换/NETCA_logo.png";
		sourceExtension = FileUtil.getExtensionName(source);
		targetExtension = ".pdf";
		// test(source, sourceExtension, targetExtension);

		source = "C:/Users/alfred/Desktop/pdf转换/NETCA_logo.jpg";
		sourceExtension = FileUtil.getExtensionName(source);
		targetExtension = ".pdf";
		// test(source, sourceExtension, targetExtension);

		source = "C:/Users/alfred/Desktop/pdf转换/广东省电子商务认证有限公司.pdf";
		sourceExtension = FileUtil.getExtensionName(source);
		targetExtension = ".swf";
		// test(source, sourceExtension, targetExtension);

		source = "C:/Users/alfred/Desktop/pdf转换/最好用Html转pdf的工具.html";
		sourceExtension = FileUtil.getExtensionName(source);
		targetExtension = ".pdf";
		// test(source, sourceExtension, targetExtension);

		source = "http://www.cnblogs.com/xionggeclub/p/6144241.html";
		sourceExtension = FileUtil.getExtensionName(source);
		targetExtension = ".pdf";
		// test(source, sourceExtension, targetExtension);

	}

	public static void test(String sourcePath, String sourceExtension, String targetExtension) throws IOException {
		System.out.println("===================================================");
		// 服务WSDL Document的地址
		URL url = new URL("http://192.168.20.137:6592/pdfconvert/webservice/webservicepdfConvert?wsdl");
		QName qname = new QName("http://pdfconvert.cnca.net/", "pdfConvert");
		Service service = Service.create(url, qname);

		PDFConvert pdf = service.getPort(PDFConvert.class);
		pdf.printContext();

		FileDataDto fileDataDtoSend = new FileDataDto();
		if (new File(sourcePath).exists()) {
			DataHandler datahandler = new DataHandler(new FileDataSource(sourcePath));
			fileDataDtoSend.setDataHandler(datahandler);
		} else {
			fileDataDtoSend.setUrl(sourcePath);
		}

		fileDataDtoSend.setSourceExtension(sourceExtension);
		fileDataDtoSend.setTargetExtension(targetExtension);
		System.out.println(fileDataDtoSend);

		String targetPath = PDFConstant.baseFloderPath + DateUtil.getDate() + "-rec/" + System.currentTimeMillis()
				+ fileDataDtoSend.getTargetExtension();
		FileDataDto fileDataDtoRece = pdf.convert(fileDataDtoSend);
		FileUtil.createFile(targetPath, fileDataDtoRece.getDataHandler().getDataSource());

		System.out.println("待转换文件：" + sourcePath);
		System.out.println("转换到文件：" + targetPath);
		System.out.println("耗时：" + fileDataDtoRece.getTimeUse());
		System.out.println("===================================================");
	}

}