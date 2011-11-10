package br.unifor.wssf.core.replicas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.unifor.wssf.os.Constants;

public class TextFileReplicaDAO implements ReplicaDAO {

	public final static String REPLICA_FILE_NAME = "replicas.txt";
	
	private List<String> replicaListString;
	
	private static TextFileReplicaDAO textFileReplicaDAO;

	private TextFileReplicaDAO() throws IOException {
			
		replicaListString = new ArrayList<String>();

		
		File file = new File(Constants.FILES_PATH+REPLICA_FILE_NAME);
		BufferedReader in = new BufferedReader(new FileReader(file));
		while (in.ready()) {
			replicaListString.add(in.readLine());
		}
		in.close();

	}

	public static TextFileReplicaDAO getInstance() throws IOException{
		if (textFileReplicaDAO == null){
			textFileReplicaDAO = new TextFileReplicaDAO();
		}
		return textFileReplicaDAO;
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
