package net.netca.pdfconvert.wsdl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface HelloWord {
	@WebMethod(operationName = "sayHai")
	public String sayHi(@WebParam(name = "name") String name);

	@WebMethod(operationName = "sayHai34")
	public String sayHi34(@WebParam(name = "name34") String name34);
}
