package br.unifor.wssf.view.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.EditText;
import br.unifor.wssf.experiment.executor.ExecutionLog;

public class LogEditText extends EditText implements ExecutionLog {

	private Handler handler;
	
	public LogEditText(Context context) {
		super(context);
		handler = new Handler();
	}
	
	public LogEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		handler = new Handler();
	}
	
	public LogEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		handler = new Handler();
	}
	

	public void log(final String text) {
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				append(text+"\n");
			}
		});
	}
	
	public void clearLog() {
		this.setText("");
	}
}
