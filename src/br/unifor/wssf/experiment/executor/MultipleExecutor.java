package br.unifor.wssf.experiment.executor;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import br.unifor.wssf.experiment.Experiment;
import br.unifor.wssf.experiment.execution.Execution;
import br.unifor.wssf.experiment.execution.log.ExecutionLog;
import br.unifor.wssf.experiment.execution.setup.ExecutionSetup;
import br.unifor.wssf.experiment.execution.setup.TextFileExecutionSetup;
import br.unifor.wssf.experiment.manager.ExperimentManager;
import br.unifor.wssf.os.SoundControl;

public class MultipleExecutor extends Thread {
	
	private static final int TENTATIVAS = 3;
	private final ExecutionLog executionLog;
	private Context context;
	private boolean stopRunning = false;
	private List<Handler> executionStoppedListeners;
	
	public MultipleExecutor(Context context, ExecutionLog executionLog) {
		super();
		this.context = context;
		this.executionLog = executionLog;
		this.executionStoppedListeners = new ArrayList<Handler>();
	}
	
	@Override
	public void run() {
		executeExperiments();
		SoundControl.getInstance(context).beep();
		fireExecutionStopped();
	}
	
	private void fireExecutionStopped() {
		for (Handler listener : executionStoppedListeners) {
			listener.sendEmptyMessage(0);
		}
	}
	
	public void addExecutionStoppedListener(Handler listener){
		executionStoppedListeners.add(listener);
	}

	public void askToStopExecution() {
		this.stopRunning = true;
	}
	
	public void executeExperiments() {
		try {
			ExecutionSetup executionSetup = TextFileExecutionSetup.getInstance();
			List<Execution> executions = executionSetup.getExecutions();
			final int totalExecucoes = executions.size();
			
			executionLog.log("----------------------");
			executionLog.log("Iniciando execuções");
			executionLog.log("Total: "+ totalExecucoes);
			executionLog.log("----------------------");
			
			long tempoTotalInicio = System.currentTimeMillis();
			
			int count = 1;
			for (Execution execution : executions) {
				
				if (stopRunning) break;

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
			executionLog.log(e.toString());
		}
	}

	private void tryToExecute(final int totalExecucoes, int count,
			Execution execution) {
		
		int tentativa = 0;
		while (tentativa < TENTATIVAS) {
			try {
				if (stopRunning) break;
				
				executionLog.log("Execução "+ count + "/" + totalExecucoes);
				executionLog.log("Replica: "+ execution.getReplicaToInvoke());
				executionLog.log("Política: "+ execution.getPolicyId());
				
				ExperimentManager experimentManager = new ExperimentManager(execution.getReplicaToInvoke(), execution.getPolicyId(), context);
				Experiment experiment = experimentManager.execute();
				
				executionLog.log("Fim da execução "+ count + " ("+experiment.getElapsedTime() + " segundos)");
				executionLog.log("----------------------");
				
				break;
			} catch (Exception e) {
				e.printStackTrace();
				executionLog.log("Erro na execução "+count);
				tentativa++;
				executionLog.log(e.toString());
				executionLog.log("Tentando novamente. Tentativa ("+tentativa+"/"+TENTATIVAS+")");
				Log.d("execution", "Erro na execução "+count);
				Log.e("execution",e.toString());
				Log.d("execution", "Tentando novamente. Tentativa ("+tentativa+"/"+TENTATIVAS+")");
			}
		}
		if (tentativa == TENTATIVAS) {
			executionLog.log("Não foi possível completar a execução "+count);
			Log.d("execution", "Não foi possível completar a execução "+count);
			executionLog.log("----------------------");
		}
	}
}
