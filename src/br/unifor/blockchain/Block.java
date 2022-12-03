package br.unifor.blockchain;
import java.util.ArrayList;
import java.util.Date;

import br.unifor.blockchain.util.StringUtil;


public class Block {
	
	public String hash;
	public String previousHash; 
	public String merkleRoot;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //Adição da transactions
	public long timeStamp;
	public int nonce;
	
	//Block Constructor.  
	public Block(String previousHash ) {
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		
		this.hash = calculateHash(); //Certificando-se de fazer isso depois de definir os outros valores.
	}
	
	//Calcular novo hash com base no conteúdo dos blocos
	public String calculateHash() {
		String calculatedhash = StringUtil.applySha256( 
				previousHash +
				Long.toString(timeStamp) +
				Integer.toString(nonce) + 
				merkleRoot
				);
		return calculatedhash;
	}
	
	//Increases nonce value until hash target is reached.
	public void mineBlock(int difficulty) {
		merkleRoot = StringUtil.getMerkleRoot(transactions);
		String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0" 
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = calculateHash();
		}
		System.out.println("Block Mined!!! : " + hash);
	}
	
	//Add transactions para os blocos
	public boolean addTransaction(Transaction transaction) {
		//processe a transação e verifique se é válida, a menos que o bloco seja o bloco de gênese, ignore.
		if(transaction == null) return false;		
		if((previousHash != "0")) {
			if((transaction.processTransaction() != true)) {
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction Successfully added to Block");
		return true;
	}

	public String getHash() {
		return hash;
	}
	
	public String getPreviousHash() {
		return previousHash;
	}

}