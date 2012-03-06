package br.unifor.wssf.os;

import java.io.File;

import android.os.Environment;


public class DataStorage {
	private final static String FILES_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WSSF/";
//	public final static String FILES_PATH = "/data/data/br.unifor.wssf/files/";
	
	public void createFilesDir() {
		File filesDir = new File(DataStorage.FILES_PATH);
		if (!filesDir.isDirectory()) {
			filesDir.mkdir();
		}
	}
	
	public File getFile(String name) {
		return new File(DataStorage.FILES_PATH + name);
	}
}
