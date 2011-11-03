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
	private final List<Execution> executions = new ArrayList<Execution>();
	private final List<Execution> execsToRepeat = new ArrayList<Execution>();
	private int repeat;
	
	public List<Execution> readSetupFile() throws IOException {
		final File file = new File(TextFileReplicaDAO.REPLICA_FILE_PATH + SETUP_FILE);
		final BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String line = reader.readLine();
		
		while (line != null) {
			boolean isRepeatLine = checkRepeat(line);
			
			if (!isCommentLine(line) && !isRepeatLine) {
				final Execution execution = new Execution(line);
				executions.add(execution);
				if (repeat > 0) {
					execsToRepeat.add(execution);
				}
			}
			
			line = reader.readLine();
		}
		
		for (int i = 1; i < repeat; i++) {
			executions.addAll(execsToRepeat);
		}
		
		return executions;
	}

	private boolean checkRepeat(String line) {
		boolean isRepeatLine = false;
		if (line.toLowerCase().startsWith("repeat".toLowerCase())) {
			repeat = Integer.parseInt(line.split(" ")[1]);
			isRepeatLine = true;
		}
		return isRepeatLine;
	}
	
	private boolean isCommentLine(String line) {
		return line.charAt(0) == '#';
	}
}
