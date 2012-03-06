package br.unifor.wssf.core.replicas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.unifor.wssf.os.DataStorage;

public class TextFileReplicaDAO implements ReplicaDAO {

	public final static String REPLICA_FILE_NAME = "replicas.txt";
	
	private List<String> replicaListString;
	
	private static TextFileReplicaDAO textFileReplicaDAO;
	
	public static TextFileReplicaDAO getInstance() throws IOException{
		if (textFileReplicaDAO == null){
			textFileReplicaDAO = new TextFileReplicaDAO();
		}
		return textFileReplicaDAO;
	}

	private TextFileReplicaDAO() throws IOException {
			
		replicaListString = new ArrayList<String>();
		
		DataStorage dataStorage = new DataStorage();
		dataStorage.createFilesDir();
		
		File replicasFile = dataStorage.getFile(REPLICA_FILE_NAME);
		readOrCreate(replicasFile);
	}

	private void readOrCreate(File replicas) throws IOException {
		if (replicas.isFile()) {
			readReplicasFile(replicas);
		} else {
			createDefaultReplicasFile(replicas);
			readReplicasFile(replicas);
		}
	}

	private void readReplicasFile(File replicas) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(replicas));
		while (in.ready()) {
			replicaListString.add(in.readLine());
		}
		in.close();
	}
	

	private void createDefaultReplicasFile(File replicas) throws IOException {
		replicas.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(replicas));
		writer.append("http://mozilla.mirror.rafal.ca/addons/71/enigmail-0.95.1-tb+sm.xpi\n");
		writer.append("http://gd.tuwien.ac.at/infosys/browsers/mozilla.org/addons/71/enigmail-0.95.1-tb+sm.xpi\n");
		writer.append("http://mozilla.phphosts.org/addons/71/enigmail-0.95.1-tb+sm.xpi\n");
		writer.append("http://mozilla.saix.net/addons/71/enigmail-0.95.1-tb+sm.xpi\n");
		writer.append("http://pv-mirror02.mozilla.org/pub/mozilla.org/addons/71/enigmail-0.95.1-tb+sm.xpi\n");
		writer.flush();
		writer.close();
	}
	
	public List<String> getReplicaListString() {
		return replicaListString;
	}

	public List<URL> getReplicas(URL replica) {

		List<URL> l = new ArrayList<URL>();
		
		for (String s : replicaListString) {
			
			if (s.equals("")) {
				
				for (URL u:l) if (u.equals(replica)) return l;
				l = new ArrayList<URL>();
				
			} else {
				try {
					l.add(new URL(s));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
	
		}

		return l;

	}

}
