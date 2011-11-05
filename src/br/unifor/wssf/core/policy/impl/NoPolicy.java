package br.unifor.wssf.core.policy.impl;

import java.util.logging.Logger;

import br.unifor.wssf.core.WSSFInvocationListener;
import br.unifor.wssf.core.WSSFInvocationThread;
import br.unifor.wssf.core.WSSFProxy;
import br.unifor.wssf.core.policy.ServerSelectionPolicy;
import br.unifor.wssf.experiment.manager.ExperimentManager;

public class NoPolicy extends ServerSelectionPolicy implements WSSFInvocationListener{
    
	private boolean firstRead = true;
	private Logger logger = Logger.getLogger("experimentLog");
	private byte[] response;
	private long startTime;
	private Exception invokeException;
	
	public synchronized byte[] invoke(byte[] request) throws Exception {
		
		WSSFProxy proxy = getProxy();
		WSSFInvocationThread i = proxy.createWSSFInvocationThread(request);
//		monitor.addWSSFInvocationThread(i);
		i.addWSSFInvocationListener(this);
		
		if (invocationListeners != null) {
			 i.addWSSFInvocationListener(invocationListeners);
		 }
		
//		monitor.setTitle(monitor.getTitle() + " - " + i.getHostName());
		logger.info("Iniciando WSSFInvocationThread: " + i);
		startTime = System.currentTimeMillis();
		i.start();
		
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (invokeException != null) throw invokeException;
		
		return response;
	}

	public synchronized void serverConnectionOpened(WSSFInvocationThread invocationThread) {
		ExperimentManager.experiment.setFirstConnectionTime(new Integer((int)(System.currentTimeMillis()-startTime)));
		logger.info(invocationThread.toString());
	}

	public synchronized void serverDataReceived(WSSFInvocationThread invocationThread,
			int qtBytesReaded) {	
		if (firstRead) {
			firstRead = false;
			ExperimentManager.experiment.setFirstReadTime(new Integer((int)(System.currentTimeMillis()-startTime)));
			logger.info("FirstRead! " + invocationThread);
		} 
			
	}

	public synchronized void serverResponseReceived(WSSFInvocationThread invocationThread,
			byte[] resp) {		
		logger.info(invocationThread.toString());
		ExperimentManager.experiment.setSelectedServer(invocationThread.getHostName());
		this.response = resp;
		this.notify();
	}

	public synchronized void serverExceptionOccurred(WSSFInvocationThread invocationThread,
			Exception e) {
		logger.info(invocationThread.toString() +", exception="+ e.getMessage());
		ExperimentManager.experiment.setSelectedServer(invocationThread.getHostName());
		ExperimentManager.experiment.setRequestStatus(e.getMessage());
		invokeException = e;
		this.notify();		
	}

	public synchronized void serverConnectionClosed(WSSFInvocationThread invocationThread) {
		
	}
}
