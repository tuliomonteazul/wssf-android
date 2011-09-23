package br.unifor.wssf.proxy.experiment;

import java.util.Date;

public class Experiment {

	private String id;
	private Date time;
	private String requestedURL;
	private String policyName;
	private Integer dataReceived;
	private Integer elapsedTime;
	private String requestStatus;
	private String selectedServer;
	private Integer firstConnectionTime = new Integer(0);
	private Integer firstReadTime = new Integer(0);
	
	//private StringBuffer details;

	public Integer getFirstConnectionTime() {
		return firstConnectionTime;
	}

	public void setFirstConnectionTime(Integer firstConnectionTime) {
		this.firstConnectionTime = firstConnectionTime;
	}

	public Integer getFirstReadTime() {
		return firstReadTime;
	}

	public void setFirstReadTime(Integer firstReadTime) {
		this.firstReadTime = firstReadTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getRequestedURL() {
		return requestedURL;
	}

	public void setRequestedURL(String requestedURL) {
		this.requestedURL = requestedURL;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public Integer getDataReceived() {
		return dataReceived;
	}

	public void setDataReceived(Integer dataReceived) {
		this.dataReceived = dataReceived;
	}

	public Integer getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(Integer elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	
	public String getSelectedServer() {
		return selectedServer;
	}

	public void setSelectedServer(String selectedServer) {
		this.selectedServer = selectedServer;
	}

	/*public StringBuffer getDetails() {
		return details;
	}

	public void setDetails(StringBuffer details) {
		this.details = details;
	}*/

	public String toString(){
		
		StringBuffer sb = new StringBuffer();
		sb.append("\nid=").append(id).
		append("\ntime=").append(time).
		append("\nrequestedURL=").append(requestedURL).
		append("\npolicyName=").append(policyName).
		append("\nselectedServer=").append(selectedServer).
		append("\ndataReceived=").append(dataReceived).
		append("\nelapsedTime=").append(elapsedTime).
		append("\nrequestStatus=").append(requestStatus).
		append("\nfirstConnectionTime=").append(firstConnectionTime).
		append("\nfirstReadTime=").append(firstReadTime);
		//append("\ndetails:\n").append(details);
		
		return sb.toString();
	}
}
