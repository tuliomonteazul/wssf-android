package br.unifor.wssf.core;

public interface WSSFInvocationListener {

	void serverResponseReceived(WSSFInvocationThread invocationThread, byte[] resp);
	void serverConnectionOpened(WSSFInvocationThread invocationThread);
	void serverDataReceived(WSSFInvocationThread invocationThread, int qtBytesReaded);
	void serverExceptionOccurred (WSSFInvocationThread invocationThread, Exception e );
	// TODO trocar para connection close
	void serverRequestCanceled(WSSFInvocationThread invocationThread);
}
