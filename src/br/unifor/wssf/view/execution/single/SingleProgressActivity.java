package br.unifor.wssf.view.execution.single;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import br.unifor.wssf.R;
import br.unifor.wssf.experiment.execution.Execution;
import br.unifor.wssf.experiment.executor.SingleExecutor;
import br.unifor.wssf.experiment.manager.ExperimentManager;
import br.unifor.wssf.view.execution.single.controller.SingleProgressController;
import br.unifor.wssf.view.widget.ReplicaProgressBar;

public class SingleProgressActivity extends Activity {
	
	private List<ReplicaProgressBar> listProgressBar = new ArrayList<ReplicaProgressBar>();
	private ExperimentManager experimentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_exec_progress);
		
		
//		SimpleExpandableListAdapter expListAdapter = new SimpleExpandableListAdapter(
//				this, createGroupList(), // Creating group List.
//				R.layout.group_row, // Group item layout XML.
//				new String[] { "Group Item" }, // the key of group item.
//				new int[] { R.id.row_name }, // ID of each group item.-Data
//												// under the key goes into this
//												// TextView.
//				createChildList(), // childData describes second-level entries.
//				R.layout.child_row, // Layout for sub-level entries(second
//									// level).
//				new String[] { "Sub Item" }, // Keys in childData maps to
//												// display.
//				new int[] { R.id.grp_child } // Data under the keys above go
//												// into these TextViews.
//		);
//		setListAdapter(expListAdapter);
		
		Intent intent = getIntent();
		if (intent != null) {
			Bundle params = intent.getExtras();
			if (params != null) {
				Execution execution = (Execution) params.getSerializable("execution");
				TextView tvDetalhes = (TextView) findViewById(R.id.detalhesPolitica);
				tvDetalhes.setText("Política: "+execution.getPolicyId());
						
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
			
			LinearLayout layout = (LinearLayout) findViewById(R.id.panelBarras);
	
			TextView textView = new TextView(this);
			textView.setText(replica);
			
			layout.addView(textView);
			layout.addView(progressBar);
		}
	}

	private void doExperiment(Bundle params) {

		final Execution execution = (Execution) params.getSerializable("execution");
		final SingleProgressController singleProgressController = new SingleProgressController(this);

		SingleExecutor singleExecutor = new SingleExecutor(execution, this, singleProgressController);
		singleExecutor.start();
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();

		if (experimentManager != null) {
			experimentManager.getjProxy().sendCloseMessage();
		}
		finish();
	}
	
	public ProgressBar getProgressBar(int id) {
		return listProgressBar.get(id);
	}
}
