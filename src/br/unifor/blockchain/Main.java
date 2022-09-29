package br.unifor.blockchain;

import java.security.Security;
import java.util.ArrayList;


public class Main {
	
	public static void main(String[] args) {
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
		
		Blockchain blockchain = new Blockchain(new ArrayList<>(), 3);
		
		blockchain.setUpOrReset();
		
		blockchain.minarateBlockchain();
		
		blockchain.isChainValid();
	}

}
