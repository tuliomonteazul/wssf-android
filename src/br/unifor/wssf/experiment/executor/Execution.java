package br.unifor.wssf.experiment.executor;

public class Execution {
	private String replicaToInvoke;
	private String policyId;

	public Execution(String line) {
		super();
		setupFromString(line);
	}
	
	public void setupFromString(String line) {
		String[] splittedLine = line.split(" ");
		this.replicaToInvoke = splittedLine[0];
		this.policyId = splittedLine[1];
	}

	public String getReplicaToInvoke() {
		return replicaToInvoke;
	}

	public String getPolicyId() {
		return policyId;
	}

	
}
