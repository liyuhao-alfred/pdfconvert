package net.netca.pdfconvert.constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.netca.pdfconvert.dto.ConvertDto;
import net.netca.pdfconvert.dto.FileDataDto;
import net.netca.pdfconvert.exception.PDFConvertException;
import net.netca.pdfconvert.util.DateUtil;
import net.netca.pdfconvert.util.FileUtil;
import net.netca.pdfconvert.util.UrlUtil;

public class PDFConstant {
	private static Log log = LogFactory.getLog(PDFConstant.class);

	public static int wdDoNotSaveChanges = 0;// 不保存待定的更改。
	public static int wdFormatPDF = 17;// word转PDF 格式
	public static int ppSaveAsPDF = 32;// ppt 转PDF 格式

	public static String baseFloderPath = "C:/Users/alfred/Desktop/pdf转换/";

	public static List<String> jacobExcel = new ArrayList<String>(Arrays.asList(".xlsx", ".xls"));
	public static List<String> jacobImg = new ArrayList<String>(Arrays.asList(".png", ".jpg"));
	public static List<String> jacobPpt = new ArrayList<String>(Arrays.asList(".ppt", ".pptx"));
	public static List<String> jacobDoc = new ArrayList<String>(Arrays.asList(".doc", ".docx"));

	public static List<String> openoffice = new ArrayList<String>();

	public static List<String> html = new ArrayList<String>(Arrays.asList(".html", ".htm"));
	public static List<String> pdf = new ArrayList<String>(Arrays.asList(".pdf"));
	public static List<String> swf = new ArrayList<String>(Arrays.asList(".swf"));

	public static String pdfToSwf_path = "D:/Program Files/SWFTools/pdf2swf.exe";

	/**
	 * wkhtmltopdf在系统中的路径
	 */
	public static String htmlToPdf_exePath = "C:/Program Files/wkhtmltopdf/bin/wkhtmltopdf.exe";

	/**
	 * openoffice安装路径
	 */
	public static String openoffice_home = "C:/Program Files (x86)/OpenOffice 4/";
	public static int port[] = { 8100 };

	/**
	 * tomcat目录下的confg文件路径
	 */
	public static String conf_path = "conf";
	public static String properties_path = "pdfconfig.properties";

	static {
		openoffice.addAll(jacobExcel);
		openoffice.addAll(jacobImg);
		openoffice.addAll(jacobPpt);
		openoffice.addAll(jacobDoc);

		baseFloderPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + File.separator
				+ "pdfData" + File.separator;

		try {
			getPropInCatalinaHome();
		} catch (Exception e) {
			try {
				getPropInWebHome();
			} catch (Exception e2) {
				log.error(e2.getMessage(), e2);
			}
		}
	}

	private static void getPropInCatalinaHome() {
		String path = System.getProperty("catalina.home");
		path = path + File.separator + conf_path + File.separator + properties_path;
		System.out.println(path);
		Properties p = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(path);
			p.load(in);

			if (System.getProperty("os.name").indexOf("Windows") == -1) {
				htmlToPdf_exePath = p.getProperty("htmlToPdf_path_lunix");
				pdfToSwf_path = p.getProperty("pdfToSwf_path_lunix");
				openoffice_home = p.getProperty("openoffice_home_lunix");
			} else {
				htmlToPdf_exePath = p.getProperty("htmlToPdf_path_win");
				pdfToSwf_path = p.getProperty("pdfToSwf_path_win");
				openoffice_home = p.getProperty("openoffice_home_win");
			}

			String portS[] = p.getProperty("port").split(",");
			port = new int[portS.length];
			for (int i = 0; i < portS.length; i++) {
				port[i] = Integer.parseInt(portS[i]);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
					System.out.println(e.getMessage());
				}
			}
		}
	}

	private static void getPropInWebHome() {
		ResourceBundle resb = ResourceBundle.getBundle("config");
		if (System.getProperty("os.name").indexOf("Windows") == -1) {
			htmlToPdf_exePath = resb.getString("htmlToPdf_path_lunix");
			pdfToSwf_path = resb.getString("pdfToSwf_path_lunix");
			openoffice_home = resb.getString("openoffice_home_lunix");
		} else {
			htmlToPdf_exePath = resb.getString("htmlToPdf_path_win");
			pdfToSwf_path = resb.getString("pdfToSwf_path_win");
			openoffice_home = resb.getString("openoffice_home_win");
		}

		String portS[] = resb.getString("port").split(",");
		port = new int[portS.length];
		for (int i = 0; i < portS.length; i++) {
			port[i] = Integer.parseInt(portS[i]);
		}
	}

	/**
	 * 规范文件路径解决html2pdf\pdf2swf\openoffice unable to write to destination
	 * 
	 * @param fileDataDto
	 * @param convertDto
	 * @return
	 */
	public static ConvertDto formatConvertDto(FileDataDto fileDataDto, String tempSourceExtension,
			String tempTargetExtension) {
		ConvertDto convertDto = new ConvertDto();

		if (fileDataDto == null || StringUtils.isEmpty(tempSourceExtension)
				|| StringUtils.isEmpty(tempTargetExtension)) {
			throw new PDFConvertException("传入数据为空！");
		}

		String tempFileName = PDFConstant.baseFloderPath + DateUtil.getDate() + File.separator
				+ System.currentTimeMillis();
		convertDto.source = tempFileName + tempSourceExtension;
		convertDto.target = tempFileName + tempTargetExtension;

		if (fileDataDto.getDataHandler() == null) {
			convertDto.source = fileDataDto.getUrl();
			if (StringUtils.isEmpty(convertDto.source) || !UrlUtil.isConnect(convertDto.source)) {
				throw new PDFConvertException("该url无效！");
			}
		} else {
			try {
				FileUtil.createFile(convertDto.source, fileDataDto.getDataHandler().getDataSource());
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				throw new PDFConvertException("写文件到[" + convertDto.source + "]失败" + e.getMessage());
			}

			// 规范文件路径解决html2pdf unable to write to destination
			convertDto.source = new File(convertDto.source).getAbsolutePath();
		}

		try {
			FileUtil.createFile(convertDto.target);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new PDFConvertException("生成目的文件失败！" + e.getMessage());
		}

		// 规范文件路径解决html2pdf unable to write to destination
		convertDto.target = new File(convertDto.target).getAbsolutePath();

		return convertDto;
	}

}
