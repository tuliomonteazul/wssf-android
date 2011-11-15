package br.unifor.wssf.view.execution.single.controller;

import android.util.Log;
import br.unifor.wssf.core.WSSFInvocationListener;
import br.unifor.wssf.core.WSSFInvocationThread;
import br.unifor.wssf.view.execution.single.SingleProgressActivity;
import br.unifor.wssf.view.widget.ReplicaProgressBar;

public class SingleProgressController implements WSSFInvocationListener {
	
	private SingleProgressActivity activity;
	
	public SingleProgressController(final SingleProgressActivity activity) {
		this.activity =  activity;
	}

	@Override
	public synchronized void serverResponseReceived(final WSSFInvocationThread invocationThread,
			byte[] resp) {
		Log.d("progress", "response "+ invocationThread.toString());
		
		int replicaID = invocationThread.getReplicaID();
		ReplicaProgressBar progressBar = (ReplicaProgressBar) activity.getProgressBar(replicaID);
		progressBar.setColor(ReplicaProgressBar.DATA_REC_COLOR);
		progressBar.setText("dados recebidos!");
		progressBar.setProgress(100);
		
//		try {
//			invocationThread.stopInvocation();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public synchronized void serverConnectionOpened(WSSFInvocationThread invocationThread) {
		Log.d("progress", "connection "+invocationThread.toString());
		
		int replicaID = invocationThread.getReplicaID();
		ReplicaProgressBar progressBar = (ReplicaProgressBar) activity.getProgressBar(replicaID);
		progressBar.setColor(ReplicaProgressBar.CONECTED_COLOR);
		progressBar.setText("conectado!");
		progressBar.setProgress(100);
	}

	@Override
	public synchronized void serverDataReceived(WSSFInvocationThread invocationThread,
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
	public synchronized void serverExceptionOccurred(WSSFInvocationThread invocationThread,
			Exception e) {
		Log.d("progress", "exception " +invocationThread.toString());
		
		int replicaID = invocationThread.getReplicaID();
		ReplicaProgressBar progressBar = (ReplicaProgressBar) activity.getProgressBar(replicaID);
		progressBar.setColor(ReplicaProgressBar.EXCEPTION_COLOR);
		progressBar.setText("erro!");
		progressBar.setProgress(100);
	}
	
	@Override
	public synchronized void serverConnectionClosed(WSSFInvocationThread invocationThread) {
		Log.d("progress", "canceled " +invocationThread.toString());
		
		int replicaID = invocationThread.getReplicaID();
		ReplicaProgressBar progressBar = (ReplicaProgressBar) activity.getProgressBar(replicaID);
		progressBar.setColor(ReplicaProgressBar.CANCELED_COLOR);
		progressBar.setText("cancelada");
//		progressBar.refreshDrawableState();
		
	}

}
