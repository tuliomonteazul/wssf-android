package br.unifor.wssf.view.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import br.unifor.wssf.experiment.execution.log.ExecutionLog;

public class LogTextView extends TextView implements ExecutionLog {

	private Handler handler;
	private ScrollView scrollView;
	
	public LogTextView(Context context) {
		super(context);
		init();
	}
	
	public LogTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public LogTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		handler = new Handler();
	}
	

	public void log(final String text) {
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				append(text+"\n");
				
				post(new Runnable() {
			        public void run() {
			            scrollView.fullScroll(View.FOCUS_DOWN);
			        }
			    });
			}
		});
	}
	
	public void clearLog() {
		setText("");
	}

	public void setScrollView(ScrollView scrollView) {
		this.scrollView = scrollView;
	}
}
