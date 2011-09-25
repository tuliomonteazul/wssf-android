package br.unifor.wssf.view.controller;

import android.content.Context;
import android.util.Log;
import br.unifor.wssf.core.WSSFInvocationListener;
import br.unifor.wssf.core.WSSFInvocationThread;

public class ProgressController implements WSSFInvocationListener {
	
	private Context context;
	
	public ProgressController(final Context context) {
		this.context =  context;
	}

	@Override
	public void serverResponseReceived(WSSFInvocationThread invocationThread,
			byte[] resp) {
		Log.d("progress", "response "+ invocationThread.toString());
		
	}

	@Override
	public void serverConnectionOpened(WSSFInvocationThread invocationThread) {
		Log.d("progress", "connection "+invocationThread.toString());
		
	}

	@Override
	public void serverDataReceived(WSSFInvocationThread invocationThread,
			int qtBytesReaded) {
		Log.d("progress", "data "+invocationThread.toString());
		
	}

	@Override
	public void serverExceptionOccurred(WSSFInvocationThread invocationThread,
			Exception e) {
		Log.d("progress", "exception " +invocationThread.toString());
		
	}

}
