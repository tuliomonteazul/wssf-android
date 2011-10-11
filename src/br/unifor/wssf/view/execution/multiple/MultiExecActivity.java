package br.unifor.wssf.view.execution.multiple;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.unifor.wssf.R;
import br.unifor.wssf.experiment.executor.MultipleExecutor;
import br.unifor.wssf.view.widget.LogEditText;

public class MultiExecActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multi_exec);
		
		
		
		addListenerBtIniciarExperimentos();
	}
	
	private void addListenerBtIniciarExperimentos() {
		final MultiExecActivity multiExecActivity = this;
		final LogEditText logExecucao = (LogEditText) findViewById(R.id.etLogExecucao);
		Button btIniciarExperimentos = (Button) findViewById(R.id.btExperimento);
		btIniciarExperimentos.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MultipleExecutor multipleExecutor = new MultipleExecutor(multiExecActivity, logExecucao);
				multipleExecutor.start();
			}
		});
	}

}
