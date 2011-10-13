package br.unifor.wssf.experiment.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import br.unifor.wssf.core.replicas.TextFileReplicaDAO;
import br.unifor.wssf.experiment.model.Experiment;

public class TxtExperimentDAO implements ExperimentDAO {
	
	private static final char SEPARATOR = ';';
	private BufferedWriter writer;
	
	
	public TxtExperimentDAO() throws FileNotFoundException, IOException {
		File file = new File(TextFileReplicaDAO.REPLICA_FILE_PATH + "/experiments.txt");
		boolean fileAlreadyExist = file.isFile();
		if (!fileAlreadyExist) {
			file.createNewFile();
		}
		
		writer = new BufferedWriter(new FileWriter(file, true));
		
		if (!fileAlreadyExist) {
			createHeaderColumns();
		}
	}
	
    private void createHeaderColumns() {
    	StringBuffer buffer = new StringBuffer(80);
    	buffer.append("id");
    	buffer.append(SEPARATOR);
    	buffer.append("time");
    	buffer.append(SEPARATOR);
    	buffer.append("requestedURL");
    	buffer.append(SEPARATOR);
    	buffer.append("policyName");
    	buffer.append(SEPARATOR);
    	buffer.append("dataReceived");
    	buffer.append(SEPARATOR);
    	buffer.append("elapsedTime");
    	buffer.append(SEPARATOR);
    	buffer.append("RequestStatus");
    	buffer.append(SEPARATOR);
    	buffer.append("selectedServer");
    	buffer.append(SEPARATOR);
    	buffer.append("FirstConnectionTime");
    	buffer.append(SEPARATOR);
    	buffer.append("FirstReadTime");
    	
    	write(buffer.toString());
	}

	public List<Experiment> getAllExperiments() {
		return null;
	}

	public Experiment getExperiment(String id) {
		return null;
	}

	public void insertExperiment(Experiment e) {

		final StringBuffer buffer = new StringBuffer(200);
		buffer.append(e.getId() == null ? "-" : e.getId());
		buffer.append(SEPARATOR);
		buffer.append(e.getTime() == null ? new Date(0) : e.getTime());
		buffer.append(SEPARATOR);
		buffer.append(e.getRequestedURL() == null ? "-" : e.getRequestedURL());
		buffer.append(SEPARATOR);
		buffer.append(e.getPolicyName() == null ? "-" : e.getPolicyName());
		buffer.append(SEPARATOR);
		buffer.append(e.getDataReceived() == null ? 0 : e.getDataReceived());
		buffer.append(SEPARATOR);
		buffer.append(e.getElapsedTime() == null ? 0 : e.getElapsedTime());
		buffer.append(SEPARATOR);
		buffer.append(e.getRequestStatus() == null ? "-" : e.getRequestStatus());
		buffer.append(SEPARATOR);
		buffer.append(e.getSelectedServer() == null ? "-" : e.getSelectedServer());
		buffer.append(SEPARATOR);
		buffer.append(e.getFirstConnectionTime() == null ? 0 : e.getFirstConnectionTime());
		buffer.append(SEPARATOR);
		buffer.append(e.getFirstReadTime() == null ? 0 : e.getFirstReadTime());
		
		write(buffer.toString());
	}

	public void commit() throws IOException {
        writer.flush();
	}
	
	private void write(String string) {
		try {
			writer.append(string);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
