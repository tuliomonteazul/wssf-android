package br.unifor.wssf.experiment.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.unifor.wssf.core.replicas.TextFileReplicaDAO;

public class ExecutionSetup {
	private static final String SETUP_FILE = "execution-setup";
	
	public List<Execution> readSetupFile() throws IOException {
		final List<Execution> executions = new ArrayList<Execution>();
		final File file = new File(TextFileReplicaDAO.REPLICA_FILE_PATH + SETUP_FILE);
		final BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String line = reader.readLine();
		while (line != null) {
			if (!isCommentLine(line)) {
				executions.add(new Execution(line));
			}
			
			line = reader.readLine();
		}
		
		return executions;
	}

	private boolean isCommentLine(String line) {
		return line.charAt(0) == '#';
	}
	
}
