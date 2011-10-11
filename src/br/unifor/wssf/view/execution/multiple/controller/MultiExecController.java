package br.unifor.wssf.view.execution.multiple.controller;

import br.unifor.wssf.core.WSSFInvocationListener;
import br.unifor.wssf.core.WSSFInvocationThread;
import br.unifor.wssf.view.execution.multiple.MultiExecActivity;

public class MultiExecController implements WSSFInvocationListener {

	private MultiExecActivity multiExecActivity;
	
	public MultiExecController(MultiExecActivity multiExecActivity) {
		super();
		this.multiExecActivity = multiExecActivity;
	}

	@Override
	public void serverResponseReceived(WSSFInvocationThread invocationThread,
			byte[] resp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serverConnectionOpened(WSSFInvocationThread invocationThread) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serverDataReceived(WSSFInvocationThread invocationThread,
			int qtBytesReaded) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serverExceptionOccurred(WSSFInvocationThread invocationThread,
			Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serverRequestCanceled(WSSFInvocationThread invocationThread) {
		// TODO Auto-generated method stub
		
	}

}
