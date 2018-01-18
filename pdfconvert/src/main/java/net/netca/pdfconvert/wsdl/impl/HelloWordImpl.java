package net.netca.pdfconvert.wsdl.impl;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

import net.netca.pdfconvert.wsdl.HelloWord;

@WebService(endpointInterface = "net.netca.pdfconvert.wsdl.HelloWord", serviceName = "hello")
@Component("helloClass")
public class HelloWordImpl implements HelloWord {

	public String sayHi(String name) {
		return "hello word " + name;
	}

	public String sayHi34(String name34) {
		// TODO Auto-generated method stub
		return null;
	}

}
