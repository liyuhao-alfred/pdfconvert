package net.netca.pdfconvert.dto;

import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlRootElement(name = "FileDataDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileDataDto {

	/**
	 * html转pdf：网上的url(html转pdf使用网址时候，必填，其他可以不填)
	 */
	private String url;
	/**
	 * success ：200 fail： 300 error：400
	 */
	private String code;
	/**
	 * 用时
	 */
	private String timeUse;
	/**
	 * 源文件的扩展名（必填）
	 */
	private String sourceExtension;
	/**
	 * 目的文件的扩展名（必填）
	 */
	private String targetExtension;
	/**
	 * 文件
	 */
	@XmlMimeType("application/octet-stream")
	private DataHandler dataHandler;

	public String getTimeUse() {
		return timeUse;
	}

	public void setTimeUse(String timeUse) {
		this.timeUse = timeUse;
	}

	public String getSourceExtension() {
		return sourceExtension;
	}

	public void setSourceExtension(String sourceExtension) {
		this.sourceExtension = sourceExtension;
	}

	public String getTargetExtension() {
		return targetExtension;
	}

	public void setTargetExtension(String targetExtension) {
		this.targetExtension = targetExtension;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public DataHandler getDataHandler() {
		return dataHandler;
	}

	public void setDataHandler(DataHandler dataHandler) {
		this.dataHandler = dataHandler;
	}

	@Override
	public String toString() {
		return "FileDataDto [url=" + url + ", timeUse=" + timeUse + ", sourceExtension=" + sourceExtension
				+ ", targetExtension=" + targetExtension + ", dataHandler=" + dataHandler + "]";
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}