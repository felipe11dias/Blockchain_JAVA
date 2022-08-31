package br.unifor.blockchain;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {


		Blockchain blockchain = new Blockchain(new ArrayList<>(), 3);
		
		blockchain.setUpOrReset();
		
		blockchain.minarateBlockchain();
		

	}

}
