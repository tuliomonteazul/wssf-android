package br.unifor.wssf.view.execution.single;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import br.unifor.wssf.R;
import br.unifor.wssf.core.replicas.TextFileReplicaDAO;
import br.unifor.wssf.experiment.execution.Execution;

public class SingleConfigActivity extends Activity {
	
	private ArrayList<String> listReplicas;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_exec);
        
        createComboReplicas();
        
        createComboPolicys();
        
        Button btExperimento = (Button) findViewById(R.id.btExperimento);
        btExperimento.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callProgressActivity();
			}

		});
        
    }

	private void createComboReplicas() {
		
		listReplicas = getListReplicas();
		
		final Spinner comboReplicas = (Spinner) findViewById(R.id.comboReplicas);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listReplicas);
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
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listPolicys);
		comboPolicys.setAdapter(adapter);
		
		comboPolicys.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				TableLayout tbBestPartial = (TableLayout) findViewById(R.id.tbBestPartial);
				if (position == 4) {
					tbBestPartial.setVisibility(View.VISIBLE);
				} else {
					tbBestPartial.setVisibility(View.INVISIBLE);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	
	private List<String> getListPolicys() {
		List<String> listPolicys = new ArrayList<String>();
		listPolicys.add("NP - NoPolicy");
		listPolicys.add("P - Parallel");
		listPolicys.add("FC - FirstConnectionPolicy");
		listPolicys.add("FR - FirstReadPolicy");
		listPolicys.add("BP -  BestPartialPolicy");
		return listPolicys;
	}

	private void callProgressActivity() {
		Intent intent = new Intent(this, SingleProgressActivity.class);
		
		Bundle params = new Bundle();
		params.putStringArrayList("listReplicas", listReplicas);
		params.putSerializable("execution", generateExecution());
		intent.putExtras(params);
		
		startActivity(intent);
		
	}
	
	private Execution generateExecution() {
		final Spinner comboReplicas = (Spinner) findViewById(R.id.comboReplicas);
		final String replica = "R"+ (comboReplicas.getSelectedItemPosition()+1);
		
		final EditText editCalls = (EditText) findViewById(R.id.calls);
		final EditText editFator = (EditText) findViewById(R.id.fator);
		final String calls = editCalls.getText().toString();
		final String fator = editFator.getText().toString();
		
		final Spinner comboPolicys = (Spinner) findViewById(R.id.comboPolicys);
		String policy = comboPolicys.getSelectedItem().toString().substring(0, 2).trim();
		if (policy.equalsIgnoreCase("BP") && calls != null
				&& !"".equals(calls) && fator != null
				&& !"".equals(fator)) {
			policy += "["+calls+","+fator+"]";
		}
		
		return new Execution(replica, policy);
	}
	
}