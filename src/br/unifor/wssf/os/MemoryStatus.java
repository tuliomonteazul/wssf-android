package br.unifor.wssf.os;

import java.util.StringTokenizer;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

public class MemoryStatus {
	
	private Context context;

	public MemoryStatus(Context context) {
		this.context = context;
	}
	
	public long getAvailable() {
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		
		// convert to kbs
		long availableMegs = mi.availMem / 1048576L;

		return availableMegs;
	}
	
	
	public static String trimBetween(String s) {
		StringTokenizer st = new StringTokenizer(s, " ", false);
		String t = "";
		while (st.hasMoreElements())
			t += " " + st.nextElement();
		return t;
	}
}
