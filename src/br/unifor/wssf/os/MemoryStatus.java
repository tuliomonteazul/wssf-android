package br.unifor.wssf.os;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

public class MemoryStatus {
	
	private Context context;
	private long totalMemory;

	public MemoryStatus(Context context) {
		this.context = context;
		this.totalMemory = calculateTotalMemory();
	}
	
	public long getAvailableMemory() {
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		
		// convert to kbs
		long availableMegs = mi.availMem / 1048576L;

		return availableMegs;
	}
	
	
	public long getTotalMemory() {
		return totalMemory;
	}
	
	private long calculateTotalMemory() {
		try {
	        RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
	        String load = reader.readLine();

	        String[] toks = trimBetween(load).split(" ");
	        
	        return Long.parseLong(toks[2]);

	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
		
		return 0;
	}

	public static String trimBetween(String s) {
		StringTokenizer st = new StringTokenizer(s, " ", false);
		String t = "";
		while (st.hasMoreElements())
			t += " " + st.nextElement();
		return t;
	}
}
