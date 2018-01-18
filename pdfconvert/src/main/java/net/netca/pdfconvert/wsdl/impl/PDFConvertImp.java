package net.netca.pdfconvert.wsdl.impl;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import net.netca.pdfconvert.api.html.Html;
import net.netca.pdfconvert.api.openoffice.Office;
import net.netca.pdfconvert.api.swf.SWF;
import net.netca.pdfconvert.constant.PDFConstant;
import net.netca.pdfconvert.dto.FileDataDto;
import net.netca.pdfconvert.exception.PDFConvertException;
import net.netca.pdfconvert.wsdl.PDFConvert;

@WebService(endpointInterface = "net.netca.pdfconvert.wsdl.PDFConvert", serviceName = "pdfConvert", portName = "PdfConvertServicePort", targetNamespace = "http://pdfconvert.cnca.net/")
@Component("pdfConvertClass")
public class PDFConvertImp implements PDFConvert {
	private static Log log = LogFactory.getLog(PDFConvertImp.class);
	@Resource
	private WebServiceContext context;

	public void printContext() {
		MessageContext ctx = context.getMessageContext();
		Set<String> set = ctx.keySet();
		for (String key : set) {
			log.info("{" + key + "," + ctx.get(key) + "}");
			try {
				log.info("key.scope:" + ctx.getScope(key));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				throw new PDFConvertException(e.getMessage());
			}
		}
	}

	public FileDataDto convert(FileDataDto fileDataDto) throws IOException {
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
		String tempTargetExtension = "." + fileDataDto.getTargetExtension().replaceAll("\\.", "");

		DataHandler dataHandler = null;
		if (PDFConstant.openoffice.contains(tempSourceExtension) && PDFConstant.pdf.contains(tempTargetExtension)) {
			Office pdf = new Office(fileDataDto);
			if (System.getProperty("os.name").indexOf("Windows") == -1) {
				pdf.convert2PDF_lunix();
			} else {
				pdf.convert2PDF_win();
			}

			dataHandler = new DataHandler(new FileDataSource(pdf.getConvertDto().target));
			fileDataDto.setDataHandler(dataHandler);
			fileDataDto.setTimeUse(pdf.getConvertDto().timeUse);

			if (new File(pdf.getConvertDto().target).length() > 0) {
				fileDataDto.setCode("200");
			} else {
				fileDataDto.setCode("300");
			}
		} else if (PDFConstant.pdf.contains(tempSourceExtension) && PDFConstant.swf.contains(tempTargetExtension)) {
			SWF pdf = new SWF(fileDataDto);
			pdf.convertToSWF();

			dataHandler = new DataHandler(new FileDataSource(pdf.getConvertDto().target));
			fileDataDto.setDataHandler(dataHandler);
			fileDataDto.setTimeUse(pdf.getConvertDto().timeUse);
			if (new File(pdf.getConvertDto().target).length() > 0) {
				fileDataDto.setCode("200");
			} else {
				fileDataDto.setCode("200");
			}
		} else if (PDFConstant.html.contains(tempSourceExtension) && PDFConstant.pdf.contains(tempTargetExtension)) {
			Html pdf = new Html(fileDataDto);
			pdf.convertToPDF();

			dataHandler = new DataHandler(new FileDataSource(pdf.getConvertDto().target));
			fileDataDto.setDataHandler(dataHandler);
			fileDataDto.setTimeUse(pdf.getConvertDto().timeUse);

			if (new File(pdf.getConvertDto().target).length() > 0) {
				fileDataDto.setCode("200");
			} else {
				fileDataDto.setCode("200");
			}
		} else {
			throw new PDFConvertException("暂不支持该类型转换！");
		}
		log.info("-------");
		return fileDataDto;
	}
}