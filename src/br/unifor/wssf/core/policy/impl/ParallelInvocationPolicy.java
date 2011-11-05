package br.unifor.wssf.core.policy.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.unifor.wssf.core.HttpUtils;
import br.unifor.wssf.core.WSSFInvocationListener;
import br.unifor.wssf.core.WSSFInvocationThread;
import br.unifor.wssf.core.WSSFProxy;
import br.unifor.wssf.core.policy.ServerSelectionPolicy;
import br.unifor.wssf.experiment.manager.ExperimentManager;


/**
 * This policy invokes all servers "in parallel" (in practice,
 * the replicas are invoked concurrently, using threads). The
 * first response to be received in full is returned to the client
 * application; all pending invocations are then interrupted,
 * with their partial response, if any, being discarded. Only
 * in the case that all servers fail to respond, an invocation
 * exception will be returned to the application.
 * 
 * @author Wesley Sousa
 * @version 1.0 - 29/10/2009
 */
public class ParallelInvocationPolicy extends ServerSelectionPolicy implements WSSFInvocationListener{
    
	private int serversFail;
	private byte[] response;
	List<WSSFInvocationThread> invocationList = new ArrayList<WSSFInvocationThread>();
	//private HashMap<WSSFInvocationThread, Integer> dataReceivedTimes = null; //Apenas p/ experimento p/ calibrar pol�tica boxplot
	//boolean startOutliersStats = false;//Apenas p/ experimento p/ calibrar pol�tica boxplot
	//private int numberOfCallsToWait = 1;//Apenas p/ experimento p/ calibrar pol�tica boxplot
	//StringBuffer sb = new StringBuffer();//Apenas p/ experimento p/ calibrar pol�tica boxplot
	private Logger logger = Logger.getLogger("experimentLog");
	
	public synchronized byte[]  invoke(byte[] request) throws Exception {

		URL url = HttpUtils.getURL(request);
		List<URL> replicaList = getReplicaList(url);
		WSSFProxy proxy = getProxy();
		
		//dataReceivedTimes = new HashMap<WSSFInvocationThread, Integer>(replicaList.size()); //Apenas p/ experimento p/ calibrar pol�tica boxplot
		
		int count = 0;
		for (URL u : replicaList){
			byte[] newRequest = HttpUtils.replaceURL(url,u,request);
			System.out.println(new String(newRequest));
			WSSFInvocationThread invocationThread = proxy.createWSSFInvocationThread(newRequest);
			invocationThread.setReplicaID(count++);
			invocationList.add(invocationThread);
//			monitor.addWSSFInvocationThread(invocationThread);
		}
		
		for (WSSFInvocationThread i : invocationList){
			 i.addWSSFInvocationListener(this);

			 if (invocationListeners != null) {
				 i.addWSSFInvocationListener(invocationListeners);
			 }
			 
			 logger.info("Iniciando WSSFInvocationThread: " + i);
			 i.start();
		}
		
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return response;
	}

	public synchronized void serverResponseReceived(WSSFInvocationThread invocationThread, byte[] resp) {
		
		this.response = resp;
		int responseCode = -1;
		responseCode = HttpUtils.getResponseCode(resp);
		logger.info(invocationThread.toString() + "; responseCode="+responseCode);
		
		if (responseCode == 200){
			for (WSSFInvocationThread i : invocationList){
				if (i.getId() != invocationThread.getId()) {
					try {
						i.stopInvocation();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			this.notify();
		} else {
			serversFail++;
			if (serversFail >= invocationList.size()) this.notify();
		}
		
		//ExperimentManager.experiment.setDetails(sb);
		ExperimentManager.experiment.setSelectedServer(invocationThread.getHostName());
		
	}

	public synchronized void serverConnectionOpened(WSSFInvocationThread invocationThread) {
		logger.info(invocationThread.toString());
	}

	int qtdLog = 0;
	int qtdBytesReceived = 0;
	public synchronized void serverDataReceived(WSSFInvocationThread invocationThread, int qtBytesReaded) {
		qtdLog++;
		qtdBytesReceived +=qtBytesReaded;
		if(qtdLog == 100){
			logger.info("MB received: "+(qtdBytesReceived/1000.0));
			qtdLog = 0;
		}
		//Apenas p/ experimento p/ calibrar pol�tica boxplot
		/*if (numberOfCallsToWait < 20){
			int numberOfCallForThisThread = dataReceivedTimes.get(invocationThread);
		  	numberOfCallForThisThread++;
			if(!(numberOfCallForThisThread >= numberOfCallsToWait)){
				dataReceivedTimes.put(invocationThread, numberOfCallForThisThread);
			}else{
				if(numberOfCallsToWait==1){
					sb.append("numberOfCallsToWait");
					for (WSSFInvocationThread i : invocationList){
						sb.append(';').append(i.getHostName());
					}
					sb.append('\n');
				}
				sb.append(numberOfCallsToWait);
				for (WSSFInvocationThread i : invocationList){
					sb.append(';').append(i.getTransferRate());
				}
				sb.append('\n');
				numberOfCallsToWait++;
			}
		}*/
	}

	public synchronized void serverExceptionOccurred(WSSFInvocationThread invocationThread,
			Exception e) {
		// TODO Implementar tratamento deste evento. Verificar se est� implementado o caso "todas as r�plicas falharam"
		
	}
	
	public synchronized void serverConnectionClosed(WSSFInvocationThread invocationThread) {
		
	}
}
