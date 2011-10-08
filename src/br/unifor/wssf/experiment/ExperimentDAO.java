package br.unifor.wssf.experiment;

import java.util.List;

public interface ExperimentDAO {

	void insertExperiment(Experiment e);
	
	Experiment getExperiment(String id);
	
	List<Experiment> getAllExperiments();
	
	void commit() throws Exception;
	
}
