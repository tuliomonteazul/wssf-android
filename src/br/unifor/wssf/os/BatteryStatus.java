package br.unifor.wssf.os;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class BatteryStatus {

	private Context context;

	public BatteryStatus(final Context context) {
		super();
		this.context = context;
	}

	public float getLevel() {
		
		Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		
		final int intLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		final int intScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		
		return (float) ((intLevel * 100) / intScale);
	}
	
	public Context getContext() {
		return context;
	}
}
