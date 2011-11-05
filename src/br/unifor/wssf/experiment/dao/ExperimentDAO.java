package br.unifor.wssf.experiment.dao;

import java.util.List;

import br.unifor.wssf.experiment.Experiment;

public interface ExperimentDAO {

	void insertExperiment(Experiment e);
	
	Experiment getExperiment(String id);
	
	List<Experiment> getAllExperiments();
	
	void commit() throws Exception;
	
}
