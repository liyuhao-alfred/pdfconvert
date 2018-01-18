package net.netca.pdfconvert.exception;

import net.netca.pdfconvert.constant.StatusConstant;

public class PDFConvertException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String msg;
	private int code = StatusConstant.SYSTEMERROR;

	public PDFConvertException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public PDFConvertException(String msg, Throwable t) {
		super(msg, t);
		this.msg = msg;
	}

	public PDFConvertException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
