package br.unifor.blockchain;

import java.security.Security;


public class Main {
	
	public static void main(String[] args) {
		//	Security Esta classe centraliza todas as propriedades de segurança e métodos de segurança comuns.
		//	O pacote Bouncy Castle Crypto é uma implementação Java de algoritmos criptográficos.
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
		
		Blockchain blockchain = new Blockchain();
		
		blockchain.setUpOrReset();
		
		blockchain.isChainValid();
	}

}
