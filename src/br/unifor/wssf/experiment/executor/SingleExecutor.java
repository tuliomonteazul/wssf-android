package br.unifor.wssf.experiment.executor;

import android.content.Context;
import android.util.Log;
import br.unifor.wssf.core.WSSFInvocationListener;
import br.unifor.wssf.experiment.execution.Execution;
import br.unifor.wssf.experiment.manager.ExperimentManager;

public class SingleExecutor extends Thread {
	
	private Execution execution;
	private Context context;
	private WSSFInvocationListener[] invocationListeners;
	
	public SingleExecutor(Execution execution, Context context, WSSFInvocationListener... invocationListeners) {
		super();
		this.execution = execution;
		this.context = context;
		this.invocationListeners = invocationListeners;
	}
	
	@Override
	public void run() {
		executeExperiment();
	}
	
	private void executeExperiment() {
		try {
			ExperimentManager experimentManager = new ExperimentManager(execution.getReplicaToInvoke(), execution.getPolicyId(), context);
			experimentManager.execute(invocationListeners);

		} catch (Exception e1) {
			e1.printStackTrace();
			Log.e("executor", "Ocorreu um erro: " + e1.getMessage());
			Log.e("executor", "Fim do Experimento: "
					+ ExperimentManager.experiment);
		}
	}

}
