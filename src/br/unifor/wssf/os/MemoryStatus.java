package br.unifor.wssf.os;

import java.util.StringTokenizer;

import android.os.Debug;
import android.util.Log;

public class MemoryStatus {
	
	public float getAllocated() {
		Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
		Debug.getMemoryInfo(memoryInfo);
		
		final float totalPrivate = memoryInfo.getTotalPrivateDirty() / 1024f;
		final float totalPss = memoryInfo.getTotalPss() / 1024f;
		final float totalShared = memoryInfo.getTotalSharedDirty() / 1024f;
		
		Log.d("memory", "--------------------");
		Log.d("memory", "getTotalPrivateDirty "+totalPrivate);
		Log.d("memory", "getTotalPss "+totalPss);
		Log.d("memory", "getTotalSharedDirty "+totalShared);
		return totalPss;
	}
	
	
	public static String trimBetween(String s) {
		StringTokenizer st = new StringTokenizer(s, " ", false);
		String t = "";
		while (st.hasMoreElements())
			t += " " + st.nextElement();
		return t;
	}
}
