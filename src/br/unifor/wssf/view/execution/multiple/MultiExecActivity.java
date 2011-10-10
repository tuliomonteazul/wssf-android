package br.unifor.wssf.view.execution.multiple;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import br.unifor.wssf.R;

public class MultiExecActivity extends Activity {
	
	private ArrayList<String> listReplicas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multi_exec);
		
		createComboFileExecutions();
		// TODO listener exibe detalhes
		// TODO listener iniciar experimento
	}
	
	private void createComboFileExecutions() {
		
		listReplicas = getListReplicas();
		
		final Spinner comboReplicas = (Spinner) findViewById(R.id.comboFileExecs);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listReplicas);
		comboReplicas.setAdapter(adapter);
	}
	
	private ArrayList<String> getListReplicas() {
		ArrayList<String> listReplicasView = new ArrayList<String>();
//		try {
//			final List<String> replicaListString = TextFileReplicaDAO.getInstance().getReplicaListString();
//			for (int i = 0; i<replicaListString.size(); i++) {
//				String replica = "R" + (i + 1) + " - " + replicaListString.get(i);
//				listReplicasView.add(replica);
//			}
			listReplicasView.add("execution1");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return listReplicasView;
	}
}
