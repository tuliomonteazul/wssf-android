package br.unifor.wssf.core;

public interface WSSFProxy {

	WSSFInvocationThread createWSSFInvocationThread(byte[] request) throws Exception;
	
}
