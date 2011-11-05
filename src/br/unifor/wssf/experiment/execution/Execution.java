package br.unifor.wssf.experiment.execution;

import java.io.Serializable;

public class Execution implements Serializable {
	private static final long serialVersionUID = 7795832205255892232L;
	
	private String replicaToInvoke;
	private String policyId;

	public Execution(String replica, String policy) {
		super();
		this.replicaToInvoke = replica;
		this.policyId = policy;
	}

	public static Execution createFromLineFile(String line) {
		String[] splittedLine = line.split(" ");
		return new Execution(splittedLine[0], splittedLine[1]);
	}

	public String getReplicaToInvoke() {
		return replicaToInvoke;
	}

	public String getPolicyId() {
		return policyId;
	}

	
}
