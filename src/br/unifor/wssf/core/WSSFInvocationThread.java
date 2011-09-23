package br.unifor.wssf.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class WSSFInvocationThread extends Thread {
	
	private long connectionTime = 0;
	private int bytesReceived = 0;
	private double transferRate = 0;
	private byte[] request;
	private String hostName;
	private int hostPort;
	
	private Logger logger = Logger.getLogger("experimentLog");
	
	public WSSFInvocationThread(byte[] request) throws MalformedURLException {
		setRequest(request);
		URL urlRequest = HttpUtils.getURL(request);
		hostName = urlRequest.getHost();
		hostPort = urlRequest.getPort() == -1 ? 80 : urlRequest.getPort();
	}
	
	public byte[] getRequest() {
		return request;
	}

	public void setRequest(byte[] request) {
		this.request = request;
	}

	public String getHostName() {
		return hostName;
	}

	public int getHostPort() {
		return hostPort;
	}
	
	public long getConnectionTime() {
		return connectionTime;
	}

	public void setConnectionTime(long connectionTime) {
		this.connectionTime = connectionTime;
	}

	public double getTransferRate() {
		return transferRate;
	}

	private void setTransferRate(double transferRate) {
		this.transferRate = transferRate;
	}
	
	public void addBytesReceived(int bytesReceived){
		this.bytesReceived += bytesReceived;
		long timeElapsed = System.currentTimeMillis() - connectionTime;
		setTransferRate((this.bytesReceived)/timeElapsed);
	}

	public int getBytesReceived() {
		return bytesReceived;
	}

	private Boolean isRunning = Boolean.TRUE;
	
	public boolean isRunning() {
		synchronized (isRunning) {
			return isRunning;	
		}
	}

	public void stopInvocation() throws IOException{
		synchronized (isRunning) {
			this.isRunning = Boolean.FALSE;
			logger.info(this.toString());
			closeConnection();
		}
	}
	
	private List<WSSFInvocationListener> invocationListenerList = new ArrayList<WSSFInvocationListener>();
    
	public List<WSSFInvocationListener> getInvocationListenerList() {
		return invocationListenerList;
	}
    
	public void addWSSFInvocationListener(WSSFInvocationListener invocationListener){
		this.invocationListenerList.add(invocationListener);
	}
	
	public abstract void closeConnection() throws IOException;


	@Override
	public String toString() {
		
		StringBuffer buff = new StringBuffer();
		buff.append("threadId=").append(getId()).append(',').append("host=").append(getHostName()).append(',').append("port=").append(getHostPort()).append(",bytesReceived=").append(getBytesReceived()).append(",elapsedTime=").append(System.currentTimeMillis()-getConnectionTime()/1000.0);
		/*buff.append("Host: ").append(getHostName()).append("\n")
			.append("Port: ").append(getHostPort()).append("\n")
			.append("Transfer rate: ").append(transferRate).append(" KB/s").append("\n")
			.append("KBytes received: "+(bytesReceived/1024.0));
		*/
		return buff.toString();
	}

}
