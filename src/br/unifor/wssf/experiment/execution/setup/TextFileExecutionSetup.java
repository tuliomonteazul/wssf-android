package br.unifor.wssf.experiment.execution.setup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import br.unifor.wssf.experiment.execution.Execution;
import br.unifor.wssf.os.Constants;

public class TextFileExecutionSetup implements ExecutionSetup {
	private static final String SETUP_FILE = "execution-setup";
	private final List<Execution> executions = new ArrayList<Execution>();
	private final List<Execution> execsToRepeat = new ArrayList<Execution>();
	private int repeat;
	
	private static TextFileExecutionSetup instance;
	
	private TextFileExecutionSetup() {
		super();
	}
	
	public static synchronized TextFileExecutionSetup getInstance() {
		if (instance == null) {
			instance = new TextFileExecutionSetup();
		}
		return instance;
	}
	
	@Override
	public List<Execution> getExecutions() {
		List<Execution> executions = null;
		try {
			executions = readSetupFile();
		} catch (IOException e) {
			Log.e("execution", e.toString());
			e.printStackTrace();
		}
		return executions;
	}
	
	public List<Execution> readSetupFile() throws IOException {
		final File file = new File(Constants.FILES_PATH + SETUP_FILE);
		final BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String line = reader.readLine();
		
		while (line != null) {
			boolean isRepeatLine = checkRepeat(line);
			
			if (!isCommentLine(line) && !isRepeatLine) {
				final Execution execution = Execution.createFromLineFile(line);
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
