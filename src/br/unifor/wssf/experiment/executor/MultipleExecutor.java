package br.unifor.wssf.experiment.executor;

import java.util.List;

import br.unifor.wssf.experiment.ExperimentManager;
import br.unifor.wssf.view.execution.multiple.MultiExecActivity;
import br.unifor.wssf.view.execution.multiple.controller.MultiExecController;

public class MultipleExecutor extends Thread {
	
	private final MultiExecController multiExecController;
	private final ExecutionLog executionLog;
	
	public MultipleExecutor(MultiExecActivity multiExecActivity, ExecutionLog executionLog) {
		super();
		this.multiExecController = new MultiExecController(multiExecActivity);
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
			
			long tempoInicio = System.currentTimeMillis();
			
			int count = 1;
			for (Execution execution : executions) {
				executionLog.log("Execução "+ count + "/" + totalExecucoes);
				executionLog.log("Replica: "+ execution.getReplicaToInvoke());
				executionLog.log("Política: "+ execution.getPolicyId());
				
				ExperimentManager experimentManager = new ExperimentManager(execution.getReplicaToInvoke(), execution.getPolicyId());
				experimentManager.execute(multiExecController);
				
				executionLog.log("Fim da execução "+ count);
				executionLog.log("----------------------");
				
				count++;
			}
			
			long tempoFim = System.currentTimeMillis();
			float tempoTotal = (float) (tempoFim - tempoInicio) * 1000;
			
			executionLog.log("Fim das execuções");
			executionLog.log("Tempo total consumido: "+ tempoTotal);
		} catch (Exception e) {
			executionLog.log("Não foi possível completar as execuções.");
			executionLog.log(e.getMessage());
			e.printStackTrace();
		}
	}
}
