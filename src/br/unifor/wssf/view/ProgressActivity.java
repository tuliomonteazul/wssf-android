package br.unifor.wssf.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import br.unifor.wssf.R;
import br.unifor.wssf.experiment.ExperimentManager;
import br.unifor.wssf.view.controller.ProgressController;
import br.unifor.wssf.view.widget.ReplicaProgressBar;

public class ProgressActivity extends Activity {
	
	private List<ReplicaProgressBar> listProgressBar = new ArrayList<ReplicaProgressBar>();
	private ExperimentManager experimentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress);
		
		Intent intent = getIntent();
		if (intent != null) {
			Bundle params = intent.getExtras();
			if (params != null) {
				ArrayList<String> listReplicas = params.getStringArrayList("listReplicas");
				createReplicasProgress(listReplicas);
				
				Toast.makeText(this, "Iniciando experimento", Toast.LENGTH_SHORT).show();
				doExperiment(params);
			}
		}
		
	}
	

	private void createReplicasProgress(ArrayList<String> listReplicas) {
		for (String replica : listReplicas) {
			ReplicaProgressBar progressBar = new ReplicaProgressBar(this);
			listProgressBar.add(progressBar);
			
			LinearLayout layout = (LinearLayout) findViewById(R.id.layoutPrincipal);
	
			TextView textView = new TextView(this);
			textView.setText(replica);
			
			layout.addView(textView);
			layout.addView(progressBar);
		}
	}

	private void doExperiment(Bundle params) {

		final String replica = params.getString("replica");
		final String policy = params.getString("policy");
		final Integer clientTimeout = params.getInt("clientTimeout");
		final ProgressController progressController = new ProgressController(this);
		
		new Thread() {
			@Override
			public void run() {
		
				try {
					experimentManager = new ExperimentManager(replica, policy, clientTimeout);
					experimentManager.execute(progressController);
					
				} catch (Exception e1) {
					e1.printStackTrace();
					try {
					  Log.e("testeProxy", "Ocorreu um erro: " + e1.getMessage());
					  Log.e("testeProxy", "Fim do Experimento: "+ExperimentManager.experiment);
//					  ExperimentDAO dao = new ExcelExperimentDAO();
//					  dao.insertExperiment(ExperimentManager.experiment);
//					  dao.commit();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}.start();
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();

		if (experimentManager != null) {
			experimentManager.getjProxy().setCanRun(false);
			experimentManager.getjProxy().closeSocket();
		}
		finish();
	}
	
	public ProgressBar getProgressBar(int id) {
		return listProgressBar.get(id);
	}
}
