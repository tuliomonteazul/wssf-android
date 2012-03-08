package br.unifor.wssf.view.execution.multiple;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import br.unifor.wssf.R;
import br.unifor.wssf.experiment.executor.MultipleExecutor;
import br.unifor.wssf.view.widget.LogTextView;

public class MultipleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multi_exec);

		addListenerBtIniciarExperimentos();

	}

	private void addListenerBtIniciarExperimentos() {
		final LogTextView logExecucao = (LogTextView) findViewById(R.id.etLogExecucao);
		final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollLog);
		logExecucao.setScrollView(scrollView);

		final Button btIniciarExperimentos = (Button) findViewById(R.id.btExperimento);

		final Context context = this;

		btIniciarExperimentos.setOnClickListener(new OnClickListener() {

			private boolean iniciou = false;
			private MultipleExecutor multipleExecutor;
			
			@Override
			public void onClick(View v) {
				if (iniciou) {
					multipleExecutor.askToStopExecution();
					btIniciarExperimentos.setEnabled(false);
					btIniciarExperimentos.setText("Cancelando...");
					iniciou = false;
				} else {
					multipleExecutor = new MultipleExecutor(context, logExecucao);
					multipleExecutor.start();
					multipleExecutor.addExecutionStoppedListener(new ChangeButtonIniciarListener(btIniciarExperimentos));
					btIniciarExperimentos.setText("Cancelar");
					iniciou = true;
				}
			}
		});
	}
}
