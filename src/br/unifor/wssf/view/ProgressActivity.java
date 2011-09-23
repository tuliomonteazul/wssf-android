package br.unifor.wssf.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import br.unifor.wssf.R;

public class ProgressActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress);
		
		Intent intent = getIntent();
		if (intent != null) {
			Bundle params = intent.getExtras();
			if (params != null) {
				ArrayList<String> listReplicas = params.getStringArrayList("listReplicas");
				createReplicasProgress(listReplicas);
			}
		}
		
	}

	private void createReplicasProgress(ArrayList<String> listReplicas) {
		int i = 1;
		for (String replica : listReplicas) {
			ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
			final float[] roundedCorners = new float[] { 5, 5, 5, 5, 5, 5, 5, 5 };
			ShapeDrawable pgDrawable = new ShapeDrawable(new RoundRectShape(roundedCorners, null, null));
			pgDrawable.getPaint().setColor(Color.RED);
			ClipDrawable progress = new ClipDrawable(pgDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
			progressBar.setProgressDrawable(progress);
			progressBar.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.progress_horizontal));
			progressBar.setMax(100);
			progressBar.setProgress(45);
			progressBar.setId(i + 100);
			
			LinearLayout layout = (LinearLayout) findViewById(R.id.layoutPrincipal);
	
			TextView textView = new TextView(this);
			textView.setText("R"+(i++) + " - " + replica);
			
			layout.addView(textView);
			layout.addView(progressBar);
		}
	}
}
