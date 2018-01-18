package net.netca.pdfconvert.api.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.netca.pdfconvert.constant.PDFConstant;
import net.netca.pdfconvert.dto.ConvertDto;
import net.netca.pdfconvert.dto.FileDataDto;
import net.netca.pdfconvert.exception.PDFConvertException;

public class Html {
	private static Log log = LogFactory.getLog(Html.class);
	private ConvertDto convertDto;

	public Html(FileDataDto fileDataDto) {
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
		if (!PDFConstant.html.contains(tempSourceExtension.toLowerCase())) {
			throw new PDFConvertException("源文件扩展名非法！");
		}

		String tempTargetExtension = "." + fileDataDto.getTargetExtension().replaceAll("\\.", "");
		if (!PDFConstant.pdf.contains(tempTargetExtension.toLowerCase())) {
			throw new PDFConvertException("目的文件扩展名非法！");
		}

		convertDto = PDFConstant.formatConvertDto(fileDataDto, tempSourceExtension, tempTargetExtension);
	}

	/**
	 * html转pdf
	 * 
	 * @param source
	 *            html路径，可以是硬盘上的路径，也可以是网络路径
	 * @param target
	 *            pdf保存路径
	 * @return 转换成功返回true
	 */
	public boolean convertToPDF() {
		long start = System.currentTimeMillis();
		System.out.println("打开文档:" + convertDto.source);
		System.out.println("转换文档到PDF:" + convertDto.target);

		StringBuilder cmd = new StringBuilder();
		cmd.append(PDFConstant.htmlToPdf_exePath);
		cmd.append(" ");
		// cmd.append(" --header-line");// 页眉下面的线
		// cmd.append(" --header-center " + "页眉中间内容");// 页眉中间内容
		// cmd.append(" --margin-top 3cm ");// 设置页面上边距 (default 10mm)
		// cmd.append(" --header-html " + "网址");// (添加一个HTML页眉,后面是网址)
		// cmd.append(" --header-spacing 5 ");// (设置页眉和内容的距离,默认0)
		// cmd.append(" --footer-center " + "中心位置的页脚内容");// 设置在中心位置的页脚内容
		// cmd.append(" --footer-html " + "网址");// (添加一个HTML页脚,后面是网址)
		// cmd.append(" --footer-line");// * 显示一条线在页脚内容上)
		// cmd.append(" --footer-spacing 5 ");// (设置页脚和内容的距离)
		cmd.append(convertDto.source);
		cmd.append(" ");
		cmd.append(convertDto.target);

		try {
			Process proc = Runtime.getRuntime().exec(cmd.toString());
			HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());
			HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
			error.start();
			output.start();
			proc.waitFor();
			long end = System.currentTimeMillis();
			System.out.println("转换完成,用时：" + (end - start) + "ms.");
			convertDto.timeUse = (end - start) + "ms";
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PDFConvertException("文件转换失败！" + e.getMessage());
		}
	}

	private class HtmlToPdfInterceptor extends Thread {
		private InputStream is;

		public HtmlToPdfInterceptor(InputStream is) {
			this.is = is;
		}

		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(is, "utf-8");
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null) {
					System.out.println(line.toString()); // 输出内容
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				throw new PDFConvertException("文件转换多线程启动失败！" + e.getMessage());
			}
		}
	}

	public ConvertDto getConvertDto() {
		return convertDto;
	}

	public void setConvertDto(ConvertDto convertDto) {
		this.convertDto = convertDto;
	}
}