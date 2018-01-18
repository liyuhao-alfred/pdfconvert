package net.netca.pdfconvert.wsdl;

import java.io.IOException;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.soap.MTOM;

import net.netca.pdfconvert.dto.FileDataDto;

@WebService(name = "PDFConvert")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@MTOM
public interface PDFConvert {
	public void printContext();

	public FileDataDto convert(@WebParam(name = "fileDataDto") FileDataDto fileDataDto) throws IOException;
}