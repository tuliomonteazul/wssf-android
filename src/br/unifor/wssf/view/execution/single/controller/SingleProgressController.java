package br.unifor.wssf.view.execution.single.controller;

import android.util.Log;
import br.unifor.wssf.R;
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
		progressBar.setColor(R.drawable.green_progress);
		progressBar.setText("100%");
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
		progressBar.setColor(R.drawable.blue_progress);
		progressBar.setText("conectado");
		progressBar.setProgress(100);
	}

	@Override
	public synchronized void serverDataReceived(WSSFInvocationThread invocationThread,
			int qtBytesReaded) {
		Log.d("progress", "data " +invocationThread.toString());

		int replicaID = invocationThread.getReplicaID();
		ReplicaProgressBar progressBar = (ReplicaProgressBar) activity.getProgressBar(replicaID);
		progressBar.setColor(R.drawable.green_progress);
		
		final int received = invocationThread.getBytesReceived();
		final int length = invocationThread.getContentLength();
		final int progress = (received * 100) / length;
		progressBar.setText(progress+"%");
		
		progressBar.setProgress(progress);
		
	}

	@Override
	public synchronized void serverExceptionOccurred(WSSFInvocationThread invocationThread,
			Exception e) {
		Log.d("progress", "exception " +invocationThread.toString());
		
		int replicaID = invocationThread.getReplicaID();
		ReplicaProgressBar progressBar = (ReplicaProgressBar) activity.getProgressBar(replicaID);
		progressBar.setColor(R.drawable.red_progress);
		progressBar.setText("erro");
		progressBar.setProgress(100);
	}
	
	@Override
	public synchronized void serverConnectionClosed(WSSFInvocationThread invocationThread) {
		Log.d("progress", "canceled " +invocationThread.toString());
		
		int replicaID = invocationThread.getReplicaID();
		ReplicaProgressBar progressBar = (ReplicaProgressBar) activity.getProgressBar(replicaID);
		progressBar.setText("cancelada");
		if (progressBar.getProgress() == 100) {
			progressBar.setProgress(0);
		}
		progressBar.setColor(R.drawable.gray_progress);
		progressBar.setProgress(progressBar.getProgress()+1);
//		progressBar.refreshDrawableState();
		
	}

}
