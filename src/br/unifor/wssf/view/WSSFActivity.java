package br.unifor.wssf.view;

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
import android.widget.EditText;
import android.widget.Spinner;
import br.unifor.wssf.R;
import br.unifor.wssf.core.replicas.TextFileReplicaDAO;
import br.unifor.wssf.proxy.experiment.ExcelExperimentDAO;
import br.unifor.wssf.proxy.experiment.ExperimentDAO;
import br.unifor.wssf.proxy.experiment.ExperimentManager;

public class WSSFActivity extends Activity {
	
	private ArrayList<String> listReplicas;
	
	  /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        createComboReplicas();
        
        createComboPolicys();
        
        Button btExperimento = (Button) findViewById(R.id.btExperimento);
        btExperimento.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callProgressActivity();
				// TODO trocar para Service
				new Thread() {
					@Override
					public void run() {
						super.run();
						doExperiment();
					}
				}.start();
			}

		});
        
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

	private void createComboReplicas() {
		
		listReplicas = getListReplicas();
		
		
		final Spinner comboReplicas = (Spinner) findViewById(R.id.comboReplicas);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listReplicas);
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

		try {
			
			final Spinner comboReplicas = (Spinner) findViewById(R.id.comboReplicas);
			String replica = "R"+ (comboReplicas.getSelectedItemPosition()+1);
			
			final Spinner comboPolicys = (Spinner) findViewById(R.id.comboPolicys);
			String policy = comboPolicys.getSelectedItem().toString().substring(0, 2).trim();
			
			final EditText editTimeout = (EditText) findViewById(R.id.clientTimeout);
			String clientTimeout = editTimeout.getText().toString();
			
			new ExperimentManager(replica, policy, Integer.parseInt(clientTimeout)).execute();
//			new ExperimentManager("R1", "FC", 180).execute();
			
//			System.exit(0);
		} catch (Exception e1) {
//			e1.printStackTrace(new PrintStream("err1.txt"));
			e1.printStackTrace();
			try {
			  Log.e("testeProxy", "Ocorreu um erro: " + e1.getMessage());
			  Log.e("testeProxy", "Fim do Experimento: "+ExperimentManager.experiment);
			  ExperimentDAO dao = new ExcelExperimentDAO();
			  dao.insertExperiment(ExperimentManager.experiment);
			  dao.commit();
			} catch (Exception e2) {
//				e2.printStackTrace(new PrintStream("err2.txt"));
				e2.printStackTrace();
			}
//			System.exit(1);
		}
	}
	

	private void callProgressActivity() {
		Intent intent = new Intent(this, ProgressActivity.class);
		
		Bundle params = new Bundle();
		params.putStringArrayList("listReplicas", listReplicas);
		intent.putExtras(params);
		
		startActivity(intent);
	}
}