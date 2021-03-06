package br.unifor.wssf.experiment.manager;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import br.unifor.wssf.core.WSSFInvocationListener;
import br.unifor.wssf.core.replicas.TextFileReplicaDAO;
import br.unifor.wssf.experiment.Experiment;
import br.unifor.wssf.experiment.dao.ExperimentDAO;
import br.unifor.wssf.experiment.dao.TextFileExperimentDAO;
import br.unifor.wssf.os.SystemStatus;
import br.unifor.wssf.proxy.SimpleHttpClient;
import br.unifor.wssf.proxy.jProxy;

public class ExperimentManager {

    private String urlString;
    private String serverSelectionPolicyName;
    public static Experiment experiment;
    private int clientTimeout = 60000; // um minuto
    private jProxy jProxy;
    private SystemStatus systemStatus;
    private ExperimentDAO experimentDAO;
    private TextFileReplicaDAO replicaDAO;
    
    public ExperimentManager(String replicaId, String policyId, int clientTimeout, Context context) throws SecurityException, IOException {
      this(replicaId,policyId, context);
      this.clientTimeout = clientTimeout * 1000;
    }
    
	public ExperimentManager(String replicaId, String policyId, Context context) throws SecurityException, IOException {
		this.replicaDAO = TextFileReplicaDAO.getInstance();
		this.urlString = getReplicaURLString(replicaId);
		this.serverSelectionPolicyName = getPolicyName(policyId);
		this.systemStatus = new SystemStatus(context);
		this.experimentDAO = new TextFileExperimentDAO(context);
		createExperiment(replicaId, policyId);
	}
    
	private void createExperiment(String replicaId, String policyId) {
		experiment = new Experiment();
		long currentTime = System.currentTimeMillis();
		experiment.setId(replicaId +"."+ policyId +"."+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(currentTime)));
		experiment.setTime(new Date(currentTime));
		experiment.setRequestedURL(urlString);
		experiment.setPolicyName(serverSelectionPolicyName);
	}

	private String getReplicaURLString(String replicaId) throws IOException{
		
		int sequence = Integer.parseInt(replicaId.substring(1));
		
		List<String> l = replicaDAO.getReplicaListString();
		
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
    
	public Experiment execute(WSSFInvocationListener... invocationListeners) throws Exception{
		
		Log.d("experiment", "Iniciando proxy na porta 8080...");
		jProxy = new jProxy(8080,"",0,60, invocationListeners);
		
		jProxy.setDebug(0, null);
		jProxy.start();
		
		waitForProxyStart();
		
		Log.d("experiment", "Iniciando cliente...");
		SimpleHttpClient c = new SimpleHttpClient();
		c.setProxy("localhost", "8080");

		long startInvocation = System.currentTimeMillis();
		String message = c.requisitar(urlString, serverSelectionPolicyName, clientTimeout);
		long endInvocation = System.currentTimeMillis();
		Integer elapsedTime = new Integer((int)(endInvocation - startInvocation));
		experiment.setElapsedTime(elapsedTime);
		experiment.setRequestStatus(message); //TODO implementar requestStatus
		experiment.setDataReceived(c.getResponseLength());
		experiment.setBatteryLevel(systemStatus.getBatteryStatus().getLevel());
		experiment.setAllocatedMemory(systemStatus.getMemoryStatus().getAllocated());
		Log.d("experiment", ""+experiment.getAllocatedMemory());
		Log.d("experiment", ""+experiment.getBatteryLevel());
		
		Log.d("experiment", "Fim do Experimento. Status: "+experiment.getRequestStatus() + ". Memória: "+experiment.getAllocatedMemory());
		
		
		Log.d("experiment", "Threads ativas " + Thread.activeCount());
		
		experimentDAO.insertExperiment(experiment);
		experimentDAO.commit();
		
		jProxy.sendCloseMessage();
		
		waitForProxyClose();
		
		return experiment;
	}
	
	private void waitForProxyClose() throws InterruptedException {
		while (jProxy.isRunning() || jProxy.isAlive()) {
			Thread.sleep(500);
		}
	}

	private void waitForProxyStart() throws InterruptedException {
		boolean conected = false;
		while (!conected) {
			try {
				Socket socket = new Socket("localhost", 8080);
				conected = socket.isConnected();
			} catch (Exception e) {
				Log.d("experiment", "Proxy ainda não iniciado, tentanto novamente em alguns segundos");
				Thread.sleep(500);
			}
		}
	}

	public jProxy getjProxy() {
		return jProxy;
	}
}
