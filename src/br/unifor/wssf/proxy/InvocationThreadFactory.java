package br.unifor.wssf.proxy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class InvocationThreadFactory {

	private List<InvocationThread> listInvocationThread = new ArrayList<InvocationThread>();
	
	private static InvocationThreadFactory instance = null;
	private InvocationThreadFactory() {
		super();
	}
	public static synchronized InvocationThreadFactory getInstance() {
		if (instance == null) {
			instance = new InvocationThreadFactory();
		}
		return instance;
	}
	
	public InvocationThread createInvocationThread(byte[] request)
			throws MalformedURLException {
		final InvocationThread invocationThread = new InvocationThread(request);
		listInvocationThread.add(invocationThread);
		return invocationThread;
	}
	
	public void closeAll() {
		for (InvocationThread invocationThread : listInvocationThread) {
			try {
				invocationThread.closeConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		listInvocationThread.clear();
	}
	
}
