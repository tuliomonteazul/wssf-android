package br.unifor.wssf.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.unifor.wssf.R;
import br.unifor.wssf.view.execution.multiple.MultipleActivity;
import br.unifor.wssf.view.execution.single.SingleConfigActivity;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.execution_choose);
		
		addListenerBtSingleExec();
		addListenerBtMultipleExec();
	}

	private void addListenerBtSingleExec() {
		Button btSingleExec = (Button) findViewById(R.id.btSingleExec);
		btSingleExec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callSingleExecConfig();
			}
		});
	}
	
	private void callSingleExecConfig() {
		Intent intent = new Intent(this, SingleConfigActivity.class);
		startActivity(intent);
	}
	
	private void addListenerBtMultipleExec() {
		Button btMultipleExec = (Button) findViewById(R.id.btMultiExec);
		btMultipleExec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callMultipleExecConfig();
			}
		});
	}

	protected void callMultipleExecConfig() {
		Intent intent = new Intent(this, MultipleActivity.class);
		startActivity(intent);
	}
}
