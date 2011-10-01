package br.unifor.wssf.proxy.experiment;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import android.os.Environment;
import android.util.Log;
import br.unifor.wssf.core.WSSFInvocationListener;
import br.unifor.wssf.core.replicas.TextFileReplicaDAO;
import br.unifor.wssf.proxy.SimpleHttpClient;
import br.unifor.wssf.proxy.jProxy;

public class ExperimentManager {

    private String urlString;
    private String serverSelectionPolicyName;
    public static Experiment experiment;
    private long currentTime;
    private static Logger logger2 = Logger.getLogger("experimentLog");
    private int clientTimeout = 300000; // cinco minutos
    private jProxy jProxy;
    
    public ExperimentManager(String replicaId, String policyId, int clientTimeout) throws SecurityException, IOException {
      this(replicaId,policyId);
      this.clientTimeout = clientTimeout * 1000;
    }
    
	public ExperimentManager(String replicaId, String policyId) throws SecurityException, IOException {
		urlString = getReplicaURLString(replicaId);
		serverSelectionPolicyName = getPolicyName(policyId);
		experiment = new Experiment();
		currentTime = System.currentTimeMillis();
		experiment.setId(replicaId +"."+ policyId +"."+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(currentTime)));
		experiment.setTime(new Date(currentTime));
		experiment.setRequestedURL(urlString);
		experiment.setPolicyName(serverSelectionPolicyName);
		
	}
    
	private String getReplicaURLString(String replicaId) throws IOException{
		
		int sequence = Integer.parseInt(replicaId.substring(1));
		
		List<String> l = TextFileReplicaDAO.getInstance().getReplicaListString();
		
		int count = 1;
		
		for (String s:l){
			if (!s.equals("") && sequence == count) return s;
			if (!s.equals("")) count++;
		}
		
		return "";

	}
	
    private String getPolicyName(String policyId){
		
		if (policyId == null) {
			throw new IllegalArgumentException();
		} else if (policyId.equals("P")) {
			return "Parallel";
		} else if (policyId.equals("FC")) {
			return "FirstConnectionPolicy";
		} else if (policyId.equals("FR")) {
			return "FirstReadPolicy";
		} else if (policyId.startsWith("BP")) {
			return "BoxPlotPolicy"+policyId.substring(policyId.indexOf('['));
		} else {
			return "NoPolicy";
		}
		
	}
    
	public void execute(WSSFInvocationListener... invocationListeners) throws Exception{
		
		Log.d("experiment", "Iniciando proxy na porta 8080...");
		jProxy = new jProxy(8080,"",0,60, invocationListeners);
		
//		File file = new File(TextFileReplicaDAO.REPLICA_FILE_PATH + "/log/log_proxy_"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(currentTime)) +".txt");
//		File logDir = new File(TextFileReplicaDAO.REPLICA_FILE_PATH + "/log/");
//		if (!logDir.isDirectory()) {
//			logDir.mkdir();
//		}
//		if (file.createNewFile()) {
//			PrintStream ps = new PrintStream(file);
//		}
		jProxy.setDebug(1, null);
		Log.d("thread", "starting... isRunning: "+jProxy.isRunning() +" - isInterrupted: "+jProxy.isInterrupted() + " - isDaemon: "+jProxy.isDaemon() + " - isAlive: "+jProxy.isAlive());
		jProxy.start();
		Log.d("thread", "started! isRunning: "+jProxy.isRunning() +" - isInterrupted: "+jProxy.isInterrupted() + " - isDaemon: "+jProxy.isDaemon() + " - isAlive: "+jProxy.isAlive());
		
		// TODO este sleep corrige o problema da nao funcionar o proxy. alterar para uma maneira mais elegante
		Thread.sleep(1000);
		Log.d("thread", "waked! isRunning: "+jProxy.isRunning() +" - isInterrupted: "+jProxy.isInterrupted() + " - isDaemon: "+jProxy.isDaemon() + " - isAlive: "+jProxy.isAlive());
		
		Log.d("experiment", "Iniciando cliente...");
		SimpleHttpClient c = new SimpleHttpClient();
		c.setProxy("localhost", "8080");

		long startInvocation = System.currentTimeMillis();
		Log.d("experiment", "Cliente requisitando " + urlString + " com a política " + serverSelectionPolicyName);
		String message = c.requisitar(urlString, serverSelectionPolicyName, clientTimeout);
		long endInvocation = System.currentTimeMillis();
		Integer elapsedTime = new Integer((int)(endInvocation - startInvocation));
		experiment.setElapsedTime(elapsedTime);
		experiment.setRequestStatus(message); //TODO implementar requestStatus
		experiment.setDataReceived(c.getResponseLength());
		Log.d("experiment", "Fim do Experimento: "+experiment);
		ExperimentDAO dao = new ExcelExperimentDAO();
		dao.insertExperiment(experiment);
		dao.commit();
		
	}
	
	public jProxy getjProxy() {
		return jProxy;
	}
}
