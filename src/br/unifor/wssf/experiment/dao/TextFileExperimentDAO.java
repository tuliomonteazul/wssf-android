package br.unifor.wssf.experiment.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import br.unifor.wssf.experiment.Experiment;
import br.unifor.wssf.os.Constants;

public class TextFileExperimentDAO implements ExperimentDAO {
	
	private static final char SEPARATOR = ';';
	private BufferedWriter writer;
	private DecimalFormat decimalFormat = new DecimalFormat("###.##");
	
	public TextFileExperimentDAO(Context context) throws FileNotFoundException, IOException {
		File file = new File(Constants.FILES_PATH + "experiments.txt");
		boolean fileAlreadyExist = file.isFile();
		if (!fileAlreadyExist) {
			file.createNewFile();
		}
//		FileOutputStream output = context.openFileOutput("experiments.txt", Context.MODE_WORLD_READABLE);
		
		
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
    	buffer.append(SEPARATOR);
    	buffer.append("batteryUsage");
    	buffer.append(SEPARATOR);
    	buffer.append("memoryAllocated");
    	
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
		buffer.append(SEPARATOR);
		buffer.append(e.getBatteryLevel());
		buffer.append(SEPARATOR);
		buffer.append(decimalFormat.format(e.getAllocatedMemory()));
		
		write(buffer.toString());
	}

	public void commit() throws IOException {
        writer.flush();
        writer.close();
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
