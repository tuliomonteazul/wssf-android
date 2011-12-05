package br.unifor.wssf.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.widget.ProgressBar;
import br.unifor.wssf.R;

public class ReplicaProgressBar extends ProgressBar {

	private String text;  
	private Paint strokeTextPaint; 
    private Paint textPaint; 
	private int actualProgressBar;
    
	private static final String START_TEXT = "conectando...";
	
	public ReplicaProgressBar(Context context) {
		super(context, null, android.R.attr.progressBarStyleHorizontal);
		configure();
	}
	
	private void configure() {
		this.setProgressDrawable(getResources().getDrawable(R.drawable.blue_progress)); 
		
		this.setMax(100);
		
		text = START_TEXT;  
        
        strokeTextPaint = new Paint();
        strokeTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        strokeTextPaint.setARGB(255, 0, 0, 0);
        strokeTextPaint.setTextSize(21);
        strokeTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        strokeTextPaint.setStyle(Paint.Style.STROKE);
        strokeTextPaint.setStrokeWidth(2);

        textPaint = new Paint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setARGB(255, 255, 255, 255);
        textPaint.setTextSize(21);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

	}
	
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		// First draw the regular progress bar, then custom draw our text
		super.onDraw(canvas);
		Rect bounds = new Rect();
		strokeTextPaint.getTextBounds(text, 0, text.length(), bounds);
		int x = getWidth() / 2 - bounds.centerX();
		int y = getHeight() / 2 - bounds.centerY();
		canvas.drawText(text, x, y, strokeTextPaint);
		
		bounds = new Rect();
		textPaint.getTextBounds(text, 0, text.length(), bounds);
		x = getWidth() / 2 - bounds.centerX();
		y = getHeight() / 2 - bounds.centerY();
		canvas.drawText(text, x, y, textPaint);
	}
	
	public synchronized void setText(String text) {
		this.text = text;
		postInvalidate();
	}

	public void setTextColor(int color) {
		textPaint.setColor(color);
		drawableStateChanged();
	}
	
	public void setColor(int color) {
		
		if (color != actualProgressBar && actualProgressBar != R.drawable.gray_progress) {
			actualProgressBar = color;
			Rect bounds = this.getProgressDrawable().getBounds();
			this.setProgressDrawable(getResources().getDrawable(color));
			this.getProgressDrawable().setBounds(bounds);
			postInvalidate();
			drawableStateChanged();
			refreshDrawableState();
		}
	}
	
}
