package br.unifor.wssf.core.policy.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.unifor.wssf.core.HttpUtils;
import br.unifor.wssf.core.WSSFInvocationListener;
import br.unifor.wssf.core.WSSFInvocationThread;
import br.unifor.wssf.core.WSSFProxy;
import br.unifor.wssf.core.policy.ServerSelectionPolicy;
import br.unifor.wssf.proxy.experiment.ExperimentManager;

/**
 *   Esta pol�tica invoca concorrentemente todas as r�plicas conhecidas. 
 *   Ser� mantida a invoca��o que iniciar primeiro a leitura dos dados de 
 *   resposta do servidor e, imediatamente, as demais invoca��es ser�o encerradas. 
 *   Caso essa invoca��o seja bem sucedida, seu resultado � retornado � 
 *   aplica��o cliente. Caso essa invoca��o falhe, invoca-se novamente de 
 *   forma concorrente todas as r�plicas conhecidas exceto a que falhou. 
 *   Este processo se repete at� que uma r�plica responda com sucesso 
 *   ou ent�o at� que todas as r�plicas falhem, quando propaga-se uma 
 *   exce��o para o cliente.
 *   
 * @author Wesley Sousa
 * @version 1.0 - 02/10/2009
 */
public class FirstReadPolicy extends ServerSelectionPolicy implements WSSFInvocationListener{
    
	private boolean firstRead;
	private WSSFInvocationThread firstReadInvocationThread;
	private byte[] response;
	List<WSSFInvocationThread> invocationList;
	List<URL> replicaList;
	
	private Logger logger = Logger.getLogger("experimentLog");
	
	public synchronized byte[]  invoke(byte[] request) throws Exception {
		
		URL url = HttpUtils.getURL(request);
		replicaList = getReplicaList(url);
		WSSFProxy proxy = getProxy();
//		WSSFGraphicMonitor monitor = new WSSFGraphicMonitor("FirstReadPolicy");
		invocationList = new ArrayList<WSSFInvocationThread>();
		
		while(replicaList.size() > 0){
			
			firstRead = true;
			firstReadInvocationThread = null;
			
			int count = 0;
			for (URL u : replicaList){
				byte[] newRequest = HttpUtils.replaceURL(url,u,request);
				WSSFInvocationThread invocationThread = proxy.createWSSFInvocationThread(newRequest);
				invocationThread.setReplicaID(count++);
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
				replicaList.remove(HttpUtils.getURL(firstReadInvocationThread.getRequest()));
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
	}

	public synchronized void serverDataReceived(WSSFInvocationThread invocationThread, int qtBytesReaded) {
		
		if (firstRead) {
			firstRead = false;
			firstReadInvocationThread = invocationThread;
			logger.info("FirstRead! " + firstReadInvocationThread);
			for (WSSFInvocationThread i : invocationList){
				if (i.getId() != firstReadInvocationThread.getId()) {
					try {
						i.stopInvocation();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	public synchronized void serverExceptionOccurred(WSSFInvocationThread invocationThread,
			Exception e) {

		try {
			replicaList.remove(HttpUtils.getURL(invocationThread.getRequest()));
			if (firstReadInvocationThread == invocationThread){
				this.notify();
			}		
		} catch (MalformedURLException e1) {
			// TODO Fazer tratamento adequado nesta exce��o
			e1.printStackTrace();
		}

		
	}
	
}
