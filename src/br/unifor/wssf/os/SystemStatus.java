package br.unifor.wssf.os;

import android.content.Context;

public class SystemStatus {
	
	private final BatteryStatus batteryStatus;
	private final MemoryStatus memoryStatus;

	public SystemStatus(Context context) {
		super();
		batteryStatus = new BatteryStatus(context);
		memoryStatus = new MemoryStatus();
	}
	
	public BatteryStatus getBatteryStatus() {
		return batteryStatus;
	}

	public MemoryStatus getMemoryStatus() {
		return memoryStatus;
	}
}
