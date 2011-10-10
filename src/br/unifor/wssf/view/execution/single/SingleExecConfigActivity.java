package br.unifor.wssf.view.execution.single;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import br.unifor.wssf.R;
import br.unifor.wssf.core.replicas.TextFileReplicaDAO;
import br.unifor.wssf.experiment.ExperimentManager;

public class SingleExecConfigActivity extends Activity {
	
	private ArrayList<String> listReplicas;
	private ExperimentManager experimentManager;
	
	  /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        createComboReplicas();
        
        createComboPolicys();
        
        final Activity activity = this;
        Button btExperimento = (Button) findViewById(R.id.btExperimento);
        btExperimento.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				CheckBox checkProgresso = (CheckBox) findViewById(R.id.checkProgresso);
				if (checkProgresso.isChecked()) {
					callProgressActivity();
				} else {
					Toast.makeText(activity, "Iniciando experimento", Toast.LENGTH_SHORT).show();
					doExperiment();
				}
			}

		});
        
    }

	private void createComboReplicas() {
		
		listReplicas = getListReplicas();
		
		
		final Spinner comboReplicas = (Spinner) findViewById(R.id.comboReplicas);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listReplicas);
		comboReplicas.setAdapter(adapter);
	}


	private ArrayList<String> getListReplicas() {
		ArrayList<String> listReplicasView = new ArrayList<String>();
		try {
			final List<String> replicaListString = TextFileReplicaDAO.getInstance().getReplicaListString();
			for (int i = 0; i<replicaListString.size(); i++) {
				String replica = "R" + (i + 1) + " - " + replicaListString.get(i);
				listReplicasView.add(replica);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listReplicasView;
	}


	private void createComboPolicys() {
		List<String> listPolicys = getListPolicys();
		
		final Spinner comboPolicys = (Spinner) findViewById(R.id.comboPolicys);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listPolicys);
		comboPolicys.setAdapter(adapter);
	}

	
	private List<String> getListPolicys() {
		List<String> listPolicys = new ArrayList<String>();
		listPolicys.add("NP - NoPolicy");
		listPolicys.add("P - Parallel");
		listPolicys.add("FC - FirstConnectionPolicy");
		listPolicys.add("FR - FirstReadPolicy");
		listPolicys.add("BP -  BoxPlotPolicy");
		return listPolicys;
	}

	protected void doExperiment() {
		
		new Thread() {
			@Override
			public void run() {
				
				final Spinner comboReplicas = (Spinner) findViewById(R.id.comboReplicas);
				final String replica = "R"+ (comboReplicas.getSelectedItemPosition()+1);
				
				final Spinner comboPolicys = (Spinner) findViewById(R.id.comboPolicys);
				String policy = comboPolicys.getSelectedItem().toString().substring(0, 2).trim();
				
				final EditText editTimeout = (EditText) findViewById(R.id.clientTimeout);
				final String clientTimeout = editTimeout.getText().toString();
				
				final EditText editCalls = (EditText) findViewById(R.id.calls);
				final EditText editFator = (EditText) findViewById(R.id.fator);
				final String calls = editCalls.getText().toString();
				final String fator = editFator.getText().toString();
				if (policy.equalsIgnoreCase("BP") && calls != null
						&& !"".equals(calls) && fator != null
						&& !"".equals(fator)) {
					policy += "["+calls+","+fator+"]";
				}
				
				
				try {
					experimentManager = new ExperimentManager(replica, policy,  Integer.parseInt(clientTimeout));
					experimentManager.execute();
					
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
	

	private void callProgressActivity() {
		Intent intent = new Intent(this, SingleExecProgressActivity.class);
		
		final Spinner comboReplicas = (Spinner) findViewById(R.id.comboReplicas);
		String replica = "R"+ (comboReplicas.getSelectedItemPosition()+1);
		
		final Spinner comboPolicys = (Spinner) findViewById(R.id.comboPolicys);
		String policy = comboPolicys.getSelectedItem().toString().substring(0, 2).trim();
		
		final EditText editTimeout = (EditText) findViewById(R.id.clientTimeout);
		String clientTimeout = editTimeout.getText().toString();
		
		final EditText editCalls = (EditText) findViewById(R.id.calls);
		final EditText editFator = (EditText) findViewById(R.id.fator);
		final String calls = editCalls.getText().toString();
		final String fator = editFator.getText().toString();
		if (policy.equalsIgnoreCase("BP") && calls != null
				&& !"".equals(calls) && fator != null
				&& !"".equals(fator)) {
			policy += "["+calls+","+fator+"]";
		}
		
		Bundle params = new Bundle();
		params.putStringArrayList("listReplicas", listReplicas);
		params.putString("replica", replica);
		params.putString("policy", policy);
		params.putInt("clientTimenout", Integer.parseInt(clientTimeout));
		intent.putExtras(params);
		
		startActivity(intent);
		
	}
	
}