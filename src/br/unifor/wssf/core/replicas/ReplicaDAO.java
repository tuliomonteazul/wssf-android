package br.unifor.wssf.core.replicas;

import java.net.URL;
import java.util.List;

public interface ReplicaDAO {

	List<URL> getReplicas(URL replica);
	
}
