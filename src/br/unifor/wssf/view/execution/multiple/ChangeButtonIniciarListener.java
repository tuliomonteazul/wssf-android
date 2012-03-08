package br.unifor.wssf.view.execution.multiple;

import android.os.Handler;
import android.os.Message;
import android.widget.Button;

public class ChangeButtonIniciarListener extends Handler {

	private final Button btIniciarExperimentos;

	public ChangeButtonIniciarListener(Button btIniciarExperimentos) {
		this.btIniciarExperimentos = btIniciarExperimentos;
	}

	public void handleMessage(Message msg) {
		btIniciarExperimentos.setEnabled(true);
		btIniciarExperimentos.setText("Iniciar Experimentos");
	}

}
