package br.unifor.wssf.view.controller;

import java.io.IOException;

import android.util.Log;
import br.unifor.wssf.core.WSSFInvocationListener;
import br.unifor.wssf.core.WSSFInvocationThread;
import br.unifor.wssf.view.ProgressActivity;
import br.unifor.wssf.view.widget.ReplicaProgressBar;

public class ProgressController implements WSSFInvocationListener {
	
	private ProgressActivity activity;
	
	public ProgressController(final ProgressActivity activity) {
		this.activity =  activity;
	}

	@Override
	public void serverResponseReceived(final WSSFInvocationThread invocationThread,
			byte[] resp) {
		Log.d("progress", "response "+ invocationThread.toString());
		
		int replicaID = invocationThread.getReplicaID();
		ReplicaProgressBar progressBar = (ReplicaProgressBar) activity.getProgressBar(replicaID);
		progressBar.setColor(ReplicaProgressBar.DATA_REC_COLOR);
		progressBar.setText("dados recebidos!");
		progressBar.setProgress(100);
		
		try {
			invocationThread.stopInvocation();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void serverConnectionOpened(WSSFInvocationThread invocationThread) {
		Log.d("progress", "connection "+invocationThread.toString());
		
		int replicaID = invocationThread.getReplicaID();
		ReplicaProgressBar progressBar = (ReplicaProgressBar) activity.getProgressBar(replicaID);
		progressBar.setColor(ReplicaProgressBar.CONECTED_COLOR);
		progressBar.setText("conectado!");
		progressBar.setProgress(100);
	}

	@Override
	public void serverDataReceived(WSSFInvocationThread invocationThread,
			int qtBytesReaded) {
		Log.d("progress", "data " +invocationThread.toString());

		int replicaID = invocationThread.getReplicaID();
		ReplicaProgressBar progressBar = (ReplicaProgressBar) activity.getProgressBar(replicaID);
		progressBar.setColor(ReplicaProgressBar.DATA_REC_COLOR);
		progressBar.setText("recebendo dados...");
		
		final int received = invocationThread.getBytesReceived();
		final int length = invocationThread.getContentLength();
		final int progress = (received * 100) / length;
		
		progressBar.setProgress(progress);
		
	}

	@Override
	public void serverExceptionOccurred(WSSFInvocationThread invocationThread,
			Exception e) {
		Log.d("progress", "exception " +invocationThread.toString());
		
		int replicaID = invocationThread.getReplicaID();
		ReplicaProgressBar progressBar = (ReplicaProgressBar) activity.getProgressBar(replicaID);
		progressBar.setColor(ReplicaProgressBar.EXCEPTION_COLOR);
		progressBar.setText("erro!");
		progressBar.setProgress(100);
	}

}
