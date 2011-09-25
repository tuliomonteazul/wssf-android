package br.unifor.wssf.core.policy;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import br.unifor.wssf.core.WSSFInvocationListener;
import br.unifor.wssf.core.WSSFProxy;
import br.unifor.wssf.core.replicas.ReplicaDAO;
import br.unifor.wssf.core.replicas.TextFileReplicaDAO;

/**
 * Represents selection strategies for accessing replicated web resources.
 * 
 * @author Wesley Sousa
 * @version 1.0 - 29/10/2009
 */
public abstract class ServerSelectionPolicy {

	private List<URL> replicaList;
	private WSSFProxy proxy;
	protected WSSFInvocationListener[] invocationListeners;
		
	public WSSFProxy getProxy() {
		return proxy;
	}

	public void setProxy(WSSFProxy proxy) {
		this.proxy = proxy;
	}

	public List<URL> getReplicaList(URL url){
		if (replicaList == null) {
			ReplicaDAO replicaDAO;
			try {
				replicaDAO = TextFileReplicaDAO.getInstance();
				replicaList =  replicaDAO.getReplicas(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		return replicaList;
	}
	
	public abstract byte[] invoke(byte[] request)throws Exception;

	public void setInvocationListener(WSSFInvocationListener... invocationListeners) {
		this.invocationListeners = invocationListeners;
	}
	
}
