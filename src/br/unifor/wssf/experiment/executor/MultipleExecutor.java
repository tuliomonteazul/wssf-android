package br.unifor.wssf.experiment.executor;

import java.util.List;

import br.unifor.wssf.experiment.manager.ExperimentManager;
import br.unifor.wssf.view.execution.battery.BatteryStatus;

public class MultipleExecutor extends Thread {
	
	private static final int TENTATIVAS = 3;
	private final ExecutionLog executionLog;
	private BatteryStatus batteryStatus;
	
	public MultipleExecutor(BatteryStatus batteryStatus, ExecutionLog executionLog) {
		super();
		this.batteryStatus = batteryStatus;
		this.executionLog = executionLog;
	}
	
	@Override
	public void run() {
		executeExperiments();
	}
	
	public void executeExperiments() {
		try {
			ExecutionSetup executionSetup = new ExecutionSetup();
			List<Execution> executions = executionSetup.readSetupFile();
			final int totalExecucoes = executions.size();
			
			executionLog.log("----------------------");
			executionLog.log("Iniciando execuções");
			executionLog.log("Total: "+ totalExecucoes);
			executionLog.log("----------------------");
			
			long tempoTotalInicio = System.currentTimeMillis();
			
			int count = 1;
			for (Execution execution : executions) {

				tryToExecute(totalExecucoes, count, execution);
				
				count++;
			}
			
			long tempoTotalFim = System.currentTimeMillis();
			float tempoTotal = (float) (tempoTotalFim - tempoTotalInicio) / 1000;
			
			executionLog.log("Fim das execuções");
			executionLog.log("Tempo total consumido: "+ tempoTotal + " segundos");
		} catch (Exception e) {
			e.printStackTrace();
			executionLog.log("Não foi possível completar as execuções.");
			executionLog.log(e.getMessage());
		}
	}

	private void tryToExecute(final int totalExecucoes, int count,
			Execution execution) {
		
		int tentativa = 0;
		while (tentativa < TENTATIVAS) {
			
			try {
				long tempoExecInicio = System.currentTimeMillis();
				
				executionLog.log("Execução "+ count + "/" + totalExecucoes);
				executionLog.log("Replica: "+ execution.getReplicaToInvoke());
				executionLog.log("Política: "+ execution.getPolicyId());
				
				ExperimentManager experimentManager = new ExperimentManager(execution.getReplicaToInvoke(), execution.getPolicyId(), batteryStatus);
				experimentManager.execute();
				
				long tempoExecFim = System.currentTimeMillis();
				float tempoExec = (float) (tempoExecFim - tempoExecInicio) / 1000;
				
				executionLog.log("Fim da execução "+ count + " ("+tempoExec + " segundos)");
				executionLog.log("----------------------");
				
				break;
			} catch (Exception e) {
				e.printStackTrace();
				executionLog.log("Não foi possível completar a execução "+count);
				tentativa++;
				executionLog.log("Tentando novamente. Tentativa ("+tentativa+"/"+TENTATIVAS+")");
				executionLog.log(e.getMessage());
			}
		
		}
	}
}
