package br.unifor.wssf.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.widget.ProgressBar;

public class ReplicaProgressBar extends ProgressBar {

	private String text;  
    private Paint textPaint; 
	
	private ShapeDrawable pgDrawable;
	private static final String START_TEXT = "conectando...";
	private static final int START_COLOR = Color.GRAY;
	public static final int CONECTED_COLOR = Color.rgb(24,116,205);
	public static final int DATA_REC_COLOR = Color.rgb(0,205,0);
	public static final int EXCEPTION_COLOR = Color.rgb(205,0,0);
	public static final int CANCELED_COLOR = Color.rgb(192, 192, 192);
	
	public ReplicaProgressBar(Context context) {
		super(context, null, android.R.attr.progressBarStyleHorizontal);
		configure();
	}
	
	private void configure() {
		final float[] roundedCorners = new float[] { 5, 5, 5, 5, 5, 5, 5, 5 };
		pgDrawable = new ShapeDrawable(new RoundRectShape(roundedCorners, null, null));
		pgDrawable.getPaint().setColor(START_COLOR);
		ClipDrawable progress = new ClipDrawable(pgDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
		this.setProgressDrawable(progress);
		this.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.progress_horizontal));
		this.setMax(100);
		
		text = START_TEXT;  
        textPaint = new Paint();  
        textPaint.setColor(Color.BLACK);  
        textPaint.setTextSize(18);  
	}
	
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		// First draw the regular progress bar, then custom draw our text
		super.onDraw(canvas);
		Rect bounds = new Rect();
		textPaint.getTextBounds(text, 0, text.length(), bounds);
		int x = getWidth() / 2 - bounds.centerX();
		int y = getHeight() / 2 - bounds.centerY();
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
		pgDrawable.getPaint().setColor(color);
	}
	
}
