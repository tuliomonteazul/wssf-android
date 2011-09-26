package br.unifor.wssf.core.policy.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.unifor.wssf.core.HttpUtils;
import br.unifor.wssf.core.WSSFGraphicMonitor;
import br.unifor.wssf.core.WSSFInvocationListener;
import br.unifor.wssf.core.WSSFInvocationThread;
import br.unifor.wssf.core.WSSFProxy;
import br.unifor.wssf.core.policy.ServerSelectionPolicy;
import br.unifor.wssf.proxy.experiment.ExperimentManager;


/**
 * Invoca concorrentemente todas as r�plicas conhecidas. A invoca��o
 * que efetuar a conex�o primeiro ser� mantida e, imediatamente, as 
 * demais invoca��es ser�o encerradas. Caso essa invoca��o seja bem
 * sucedida, seu resultado � retornado � aplica��o cliente.
 * Caso essa invoca��o falhe, invoca-se novamente de forma concorrente
 * todas as r�plicas conhecidas exceto a que falhou. Este processo se
 * repete at� que uma r�plica responda com sucesso ou ent�o at� que
 * todas as r�plicas falhem, quando propaga-se uma exce��o para o
 * cliente.
 *   
 * @author Wesley Sousa
 * @version 1.0 - 02/10/2009
 */
public class FirstConnectionPolicy extends ServerSelectionPolicy implements WSSFInvocationListener{
    
	private boolean firstConnection = true;
	private WSSFInvocationThread firstConnectionInvocationThread;
	private byte[] response;
	List<WSSFInvocationThread> invocationList ;//= new ArrayList<WSSFInvocationThread>();
	List<URL> replicaList;
	
	private Logger logger = Logger.getLogger("experimentLog");
	
	public synchronized byte[]  invoke(byte[] request) throws Exception {
		
		URL url = HttpUtils.getURL(request);
		replicaList = getReplicaList(url);
		WSSFProxy proxy = getProxy();
		invocationList = new ArrayList<WSSFInvocationThread>();
		WSSFGraphicMonitor monitor = new WSSFGraphicMonitor("FirstConnectionPolicy");
		
		while (replicaList.size() > 0){

			firstConnection = true;
			firstConnectionInvocationThread = null;
			
			for (URL u : replicaList){
				byte[] newRequest = HttpUtils.replaceURL(url,u,request);
				WSSFInvocationThread invocationThread = proxy.createWSSFInvocationThread(newRequest);
				invocationList.add(invocationThread);
//				monitor.addWSSFInvocationThread(invocationThread);
			}
			
			for (WSSFInvocationThread i : invocationList){
				 i.addWSSFInvocationListener(this);

				 if (invocationListeners != null) {
					 i.addWSSFInvocationListener(invocationListeners);
				 }
				 
				 logger.info("Iniciando WSSFInvocationThread: " + i);
				 i.start();
			}

			this.wait();
			
			if (HttpUtils.getResponseCode(response) == HttpURLConnection.HTTP_OK){
				return response;
			} else {
				replicaList.remove(HttpUtils.getURL(firstConnectionInvocationThread.getRequest()));
				invocationList.clear();
			}
			
		} 
		
		return response;
		
	}

	public synchronized void serverResponseReceived(WSSFInvocationThread invocationThread, byte[] resp) {
		logger.info(invocationThread.toString());
		if(invocationThread.isRunning()){
			ExperimentManager.experiment.setSelectedServer(invocationThread.getHostName());
			this.response = resp;
			this.notify();
		}
	}

	public synchronized void serverConnectionOpened(WSSFInvocationThread invocationThread) {
		logger.info(invocationThread.toString());
		if (firstConnection) {
			firstConnection = false;
			firstConnectionInvocationThread = invocationThread;
			logger.info("FirstConnection! " + firstConnectionInvocationThread);
			for (WSSFInvocationThread i : invocationList){
				if (i.getId() != firstConnectionInvocationThread.getId()) {
					try {
						i.stopInvocation();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}

	int qtdLog = 0;
	int qtdBytesReceived = 0;
	public synchronized void serverDataReceived(WSSFInvocationThread invocationThread, int qtBytesReaded) {
		qtdLog++;
		qtdBytesReceived +=qtBytesReaded;
		if(qtdLog == 50){
			logger.info("MB received: "+(qtdBytesReceived/1000.0));
			qtdLog = 0;
		}
	}

	public synchronized void serverExceptionOccurred(WSSFInvocationThread invocationThread,
			Exception e) {

		try {
			replicaList.remove(HttpUtils.getURL(invocationThread.getRequest()));
			if (firstConnectionInvocationThread == invocationThread){
				this.notify();
			}		
		} catch (MalformedURLException e1) {
			// TODO Fazer tratamento adequado nesta exce��o
			e1.printStackTrace();
		}

		
	}
	
}
