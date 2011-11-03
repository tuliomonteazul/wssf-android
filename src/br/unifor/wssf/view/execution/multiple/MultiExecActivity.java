package br.unifor.wssf.view.execution.multiple;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import br.unifor.wssf.R;
import br.unifor.wssf.experiment.executor.MultipleExecutor;
import br.unifor.wssf.view.execution.battery.BatteryStatus;
import br.unifor.wssf.view.widget.LogTextView;

public class MultiExecActivity extends Activity {
	
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
		
		Button btIniciarExperimentos = (Button) findViewById(R.id.btExperimento);
		
		final BatteryStatus batteryStatus = new BatteryStatus(this);
		
		btIniciarExperimentos.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MultipleExecutor multipleExecutor = new MultipleExecutor(batteryStatus, logExecucao);
				multipleExecutor.start();
			}
		});
	}
}
