package net.netca.pdfconvert.api.swf;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.netca.pdfconvert.constant.PDFConstant;
import net.netca.pdfconvert.dto.ConvertDto;
import net.netca.pdfconvert.dto.FileDataDto;
import net.netca.pdfconvert.exception.PDFConvertException;

public class SWF {
	private static Log log = LogFactory.getLog(SWF.class);
	private ConvertDto convertDto;

	public SWF(FileDataDto fileDataDto) {
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
		if (!PDFConstant.pdf.contains(tempSourceExtension.toLowerCase())) {
			throw new PDFConvertException("源文件扩展名非法！");
		}

		String tempTargetExtension = "." + fileDataDto.getTargetExtension().replaceAll("\\.", "");
		if (!PDFConstant.swf.contains(tempTargetExtension.toLowerCase())) {
			throw new PDFConvertException("目的文件扩展名非法！");
		}

		convertDto = PDFConstant.formatConvertDto(fileDataDto, tempSourceExtension, tempTargetExtension);
	}

	public boolean convertToSWF() {
		long start = System.currentTimeMillis();
		String command = "";
		if (System.getProperty("os.name").indexOf("Windows") == -1) {
			command = PDFConstant.pdfToSwf_path + " " + convertDto.source + " " + convertDto.target;
		} else {
			command = PDFConstant.pdfToSwf_path + " \"" + convertDto.source + "\" -o " + convertDto.target + " -T 9 -f";
		}

		try {
			log.info("打开文档:" + convertDto.source);
			log.info("转换文档到PDF:" + convertDto.target);
			java.lang.Process process = Runtime.getRuntime().exec(command);
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				log.error(e.getMessage(), e);
				throw new PDFConvertException("文件转换失败！" + e.getMessage());
			}

			long end = System.currentTimeMillis();
			log.info("转换完成,用时：" + (end - start) + "ms.");
			convertDto.timeUse = (end - start) + "ms";
			return true;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new PDFConvertException("文件转换失败！" + e.getMessage());
		}

	}

	public ConvertDto getConvertDto() {
		return convertDto;
	}

	public void setConvertDto(ConvertDto convertDto) {
		this.convertDto = convertDto;
	}
}
