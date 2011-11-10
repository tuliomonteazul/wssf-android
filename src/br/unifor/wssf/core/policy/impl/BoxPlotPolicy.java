package br.unifor.wssf.core.policy.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import br.unifor.wssf.core.HttpUtils;
import br.unifor.wssf.core.WSSFInvocationListener;
import br.unifor.wssf.core.WSSFInvocationThread;
import br.unifor.wssf.core.WSSFProxy;
import br.unifor.wssf.core.policy.ServerSelectionPolicy;
import br.unifor.wssf.experiment.manager.ExperimentManager;

/**
 * 
 * @author Leandro Sales (leandro.shp@gmail.com)
 * @version 1.0
 * 
 */
public class BoxPlotPolicy extends ServerSelectionPolicy implements WSSFInvocationListener{
    
	private static final int DEFAULT_NUMBER_CALLS = 10;
	private static final double DEFAULT_FATOR = 1.5;
	
	private List<URL> replicaFailList = new ArrayList<URL>();
	private byte[] response;
	List<WSSFInvocationThread> invocationList = new ArrayList<WSSFInvocationThread>();
	boolean startOutliersStats = false;
	boolean possuiGanhador = false;
	private HashMap<WSSFInvocationThread, Integer> dataReceivedTimes = null;
	BoxPlot boxPlot = null;
	
	private int numberOfCallsToWait;
	private double fator;
	
	private Logger logger = Logger.getLogger("invocation");

	public BoxPlotPolicy() {
		this(DEFAULT_NUMBER_CALLS, DEFAULT_FATOR);
	}
	
	public BoxPlotPolicy(int numberOfCallsToWait, double fator) {
		super();
		this.numberOfCallsToWait = numberOfCallsToWait;
		this.fator = fator;
		
	}

	public synchronized byte[]  invoke(byte[] request) throws Exception {
		
		URL url = HttpUtils.getURL(request);
		List<URL> replicaList = getReplicaList(url);
		WSSFProxy proxy = getProxy();
		
		if (replicaFailList.size() < replicaList.size()){
			
			dataReceivedTimes = new HashMap<WSSFInvocationThread, Integer>(replicaList.size());
			
			int count = 0;
			for (URL u : replicaList){
				//TODO: n�o criar o WSSFInvocationThread contido na replicaFailList
				byte[] newRequest = HttpUtils.replaceURL(url,u,request);
				WSSFInvocationThread invocationThread = proxy.createWSSFInvocationThread(newRequest);
				invocationThread.setReplicaID(count++);
//				monitor.addWSSFInvocationThread(invocationThread);
				dataReceivedTimes.put(invocationThread, 0);
				invocationList.add(invocationThread); 
			}
			boxPlot = new BoxPlot(invocationList,fator);
			
			
			for (WSSFInvocationThread i : invocationList){
				 i.addWSSFInvocationListener(this);

				 if (invocationListeners != null) {
					 i.addWSSFInvocationListener(invocationListeners);
				 }
				 
				 logger.info("Iniciando WSSFInvocationThread: " + i);
				 i.start();
			}

			this.wait();
			
		} else {
			return response;
		}
		
		int responseCode = HttpUtils.getResponseCode(response);
		
		return (responseCode == HttpURLConnection.HTTP_OK) ? response : invoke(request);
		
	}
	
	public synchronized void serverConnectionOpened(WSSFInvocationThread invocationThread) {
		logger.info(invocationThread.toString());
		
	}
	
	public synchronized void serverDataReceived(WSSFInvocationThread invocationThread, int qtBytesReaded) {
		log(invocationThread);
		if(!startOutliersStats){
			shouldStartOutliersStats(invocationThread);	
		}
		
		if(startOutliersStats && !possuiGanhador){
			//long currentTime = System.currentTimeMillis();
			boxPlot.calcularOutliers();
			if(!boxPlot.matarQuemDeveMorrer()){
				logger.info("Ninguem Morreu...");
				logger.info(invocationList.toString());
				
				//System.out.println("Matou sem outlier...");
				//boxPlot.matarOsPrimeiros(1);
			}
			possuiGanhador = boxPlot.getTamanhoAmostra() == 1;
			if(possuiGanhador){
				logger.info("VENCEDOR: " + invocationList);
			}
			//System.out.println(">>>> Tempo gasto pelo algoritmo boxplot: " + (System.currentTimeMillis()-currentTime) );
		}
	}

	public synchronized void serverResponseReceived(WSSFInvocationThread invocationThread, byte[] resp) {
		if(invocationThread.isRunning()){
			logger.info(invocationThread.toString());
			ExperimentManager.experiment.setSelectedServer(invocationThread.getHostName());
			int responseCode = HttpUtils.getResponseCode(resp);
			if (responseCode == HttpURLConnection.HTTP_OK){
				this.response = resp;
				this.notify();			
			}
			else{
				logger.info("Connection failed: "+ invocationThread.getHostName() + ". Error code: "+responseCode);
				try {
					invocationThread.stopInvocation();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		}
	}
	
	private void shouldStartOutliersStats(WSSFInvocationThread invocationThread){
		int numberOfCallForThisThread = dataReceivedTimes.get(invocationThread);
		numberOfCallForThisThread++;
		startOutliersStats = numberOfCallForThisThread >= numberOfCallsToWait;
		if(!startOutliersStats){
			dataReceivedTimes.put(invocationThread, numberOfCallForThisThread);
		}
	}
	
	
	private int qtdLog = 0;
	private void log(WSSFInvocationThread invocationThread){
		if(possuiGanhador){
			qtdLog++;
			if(qtdLog == 1000){
				logger.info(invocationThread.toString());
				qtdLog = 0;
			}
		}
	}

	public synchronized void serverExceptionOccurred(WSSFInvocationThread invocationThread,
			Exception e) {
		// TODO TODO Implementar tratamento deste evento. Verificar se est� implementado o caso "todas as r�plicas falharam"
		
	}
	
	public synchronized void serverConnectionClosed(WSSFInvocationThread invocationThread) {
		
	}
	
}

class BoxPlot{
	
	private ComparatorBoxPlot comparatorBoxPlot = new ComparatorBoxPlot();
	private List<WSSFInvocationThread> dados;
	private double fator = 1.5;
	int[] amostraValida = null;//indice da amostra v�lido, segue o padrao [inicio,fim)
	
	private Logger logger = Logger.getLogger("invocation");
	
	public BoxPlot(List<WSSFInvocationThread> dados, double fator){
		this.dados  = dados;
		this.fator = fator;
	}
	
	public int getTamanhoAmostra(){
		return dados.size();
	}
	
	public void calcularOutliers() {
		
		Collections.sort(dados, comparatorBoxPlot); //Ordem crescente de taxa de transfer�ncia
		//System.out.println("Dados: " + dados);
		
		int tamanhoAmostra = dados.size();
		//System.out.println("tamanhoAmostratamanhoAmostr: "+tamanhoAmostra);

		int porcao = new BigDecimal(tamanhoAmostra).divide(new BigDecimal(4),0, BigDecimal.ROUND_HALF_EVEN).intValue();
        //System.out.println("porcao: "+porcao);
		
		int inicioAmostra = 1;
		int fimAmostra = tamanhoAmostra;

		if (porcao > 0) {
			//Primeiro quartilho
			WSSFInvocationThread q1 = dados.get(porcao);
			//Segundo quartilho
			WSSFInvocationThread q3 = dados.get(3 * porcao - 1);
			
			double d = q3.getTransferRate() - q1.getTransferRate();
			d = d*fator;
			
			
			//limite para outliers inferior
			double limiteInferior = q1.getTransferRate() - d;
			//limite para outliers superior
			double limiteSuperior = q3.getTransferRate() + d;

			boolean passouOutliersInicial = false;
			boolean entrouOutliersFinal = false;

			for (int ii = 0; ii < dados.size(); ii++) {
				
				boolean outlier = false;
				Double dado = dados.get(ii).getTransferRate();
				if (dado < limiteInferior || dado > limiteSuperior) {
					outlier = true;
				}

				if (!outlier && !passouOutliersInicial) {
					inicioAmostra = ii;
					passouOutliersInicial = true;
				}

				if (outlier && passouOutliersInicial && !entrouOutliersFinal) {
					fimAmostra = ii;
					entrouOutliersFinal = true;
				}
			}
		}
		amostraValida = new int[] { inicioAmostra, fimAmostra }; 
	}
	
	//indica se alguma thread foi morta
	public boolean matarQuemDeveMorrer(){
		if(possuiOutlierSuperior()){//se tiver outliers superior, estes serao os unicos que permancererao
			logger.info("Possui outlier superior...");
			matarOsPrimeiros(amostraValida[1]);
			return true;
		}
		else if(possuiOutlierInferior()){
			logger.info("Possui outlier inferior...");
			matarOsPrimeiros(amostraValida[0]);
			return true;
		}
		return false;
	}
	
	public void matarOsPrimeiros(int n){
		while(n > 0){
			try {
				dados.remove(0).stopInvocation();
			} catch (IOException e) {}
			n--;
		}
	}
	
	public  boolean possuiOutlierInferior(){
		return amostraValida != null && amostraValida[0] > 0;
	}
	
	public  boolean possuiOutlierSuperior(){
		return amostraValida != null && amostraValida[1] < dados.size();
	}
}


class ComparatorBoxPlot implements Comparator<WSSFInvocationThread>{

	public int compare(WSSFInvocationThread o1, WSSFInvocationThread o2) {
		double transferRate1 = o1.getTransferRate();
		double transferRate2 = o2.getTransferRate();
		
		if(transferRate1 < transferRate2){
			return -1;
		}
		if(transferRate1 > transferRate2){
			return 1;
		}
		return 0;
	}
	
}

