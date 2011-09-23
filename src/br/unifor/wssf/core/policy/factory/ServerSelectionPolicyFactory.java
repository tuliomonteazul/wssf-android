package br.unifor.wssf.core.policy.factory;

import br.unifor.wssf.core.policy.ServerSelectionPolicy;
import br.unifor.wssf.core.policy.impl.BoxPlotPolicy;
import br.unifor.wssf.core.policy.impl.FirstConnectionPolicy;
import br.unifor.wssf.core.policy.impl.FirstReadPolicy;
import br.unifor.wssf.core.policy.impl.NoPolicy;
import br.unifor.wssf.core.policy.impl.ParallelInvocationPolicy;

public class ServerSelectionPolicyFactory {

	/**
	 * FactoryMethod provis�rio enquanto n�o se usa o Spring Framework.
	 * 
	 * Nomes esperados:
	 * . Parallel
	 * . FirstConnectionPolicy
	 * . FirstReadPolicy
	 * . BoxPlotPolicy[10,0.5]
	 * . BoxPlotPolicy[10,1]
	 * . BoxPlotPolicy[10,1.5]
	 * 
	 * @param policyName
	 * @return
	 */
	public static ServerSelectionPolicy createServerSelectionPolicy(String policyName){
		
		if (policyName == null ||policyName.equals("NoPolicy")) {
			return new NoPolicy();
		}else if (policyName.equals("Parallel")) {
			return new ParallelInvocationPolicy();
		} else if (policyName.equals("FirstConnectionPolicy")) {
			return new FirstConnectionPolicy();
		} else if (policyName.equals("FirstReadPolicy")) {
			return new FirstReadPolicy();
		} else if (policyName.startsWith("BoxPlotPolicy")) {
			String[] params = policyName.substring(policyName.indexOf('[')+1, policyName.indexOf(']')).split(",");
			return new BoxPlotPolicy(Integer.parseInt(params[0]),Double.parseDouble(params[1]));
		} else {
			return null;
		}
		
		
	}
	
	public static void main(String[] args) {
		
		String policy = "BoxPlotPolicy[10,0.5]";
		String[] params = null;
		
		if (policy.startsWith("BoxPlotPolicy")) {
			params = policy.substring(policy.indexOf('[')+1, policy.indexOf(']')).split(",");
		}
		
		System.out.println(params[0]+"-"+params[1]);
		
	}
	
}
