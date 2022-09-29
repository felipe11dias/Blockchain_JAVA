package br.unifor.blockchain;
import java.util.ArrayList;
import java.util.HashMap;

public class Blockchain {
	
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	public static float minimumTransaction = 0.1f;
	public static Wallet walletA;
	public static Wallet walletB;
	public static Transaction genesisTransaction;
	
	private ArrayList<Block> blockchain = new ArrayList<>();
	public Integer difficulty = 3;
	private final String prefixString = new String(new char[difficulty]).replace('\0', '0');
	
	public Blockchain(ArrayList<Block> blockchain, Integer difficulty) {
		this.blockchain = blockchain;
		this.difficulty = difficulty;
	}
	
	public void setUpOrReset() {
		/*
		 * logger.log(Level.INFO, "Limpando blockchain."); blockchain.clear();
		 * logger.log(Level.INFO, "Criando bloco genesis do blockchain."); Block
		 * genesisBlock = new Block("Bloco Genesis.", "0"); logger.log(Level.INFO,
		 * "Minerando bloco genesis do blockchain."); logger.log(Level.INFO,
		 * "Adicionando bloco genesis na blockchain."); blockchain.add(genesisBlock);
		 * 
		 * System.out.println("");
		 * 
		 * logger.log(Level.INFO,
		 * "Criando primeiro bloco com previusHas do blockchain."); Block firstBlock =
		 * new Block("Primeiro bloco ap�s o genesis.", genesisBlock.getHash(), new
		 * Date().getTime()); logger.log(Level.INFO,
		 * "Minerando primeiro bloco com previusHas do blockchain.");
		 * logger.log(Level.INFO,
		 * "Adicionando primeiro bloco com previusHas na blockchain.");
		 * blockchain.add(firstBlock);
		 * 
		 * System.out.println("");
		 */
        
        //add our blocks to the blockchain ArrayList:
  		
  		//Create wallets:
  		walletA = new Wallet();
  		walletB = new Wallet();		
  		Wallet coinbase = new Wallet();
  		
  		//create genesis transaction, which sends 100 Coins to walletA: 
  		genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
  		genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction	
  		genesisTransaction.transactionId = "0"; //manually set the transaction id
  		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
  		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
  		
  		System.out.println("Creating and Mining Genesis block... ");
  		Block genesis = new Block("0");
  		genesis.addTransaction(genesisTransaction);
  		blockchain.add(genesis);
  		
  		//Testing
  		Block block1 = new Block(genesis.hash);
  		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
  		System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
  		block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
  		blockchain.add(block1);
  		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
  		System.out.println("WalletB's balance is: " + walletB.getBalance());
  		
  		Block block2 = new Block(block1.hash);
  		System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
  		block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
  		blockchain.add(block2);
  		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
  		System.out.println("WalletB's balance is: " + walletB.getBalance());
  		
  		Block block3 = new Block(block2.hash);
  		System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
  		block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));
  		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
  		System.out.println("WalletB's balance is: " + walletB.getBalance());
  		
    }
	
	public void minarateBlockchain() {
		for (int i = 0; i < blockchain.size(); i++) {
			System.out.println("");
			System.out.println("HASH VÁLIDO ENCONTRADO: " + blockchain.get(i).mineBlock(difficulty));
			System.out.println(blockchain.get(i).toString());
			System.out.println("NONCE RESULTANTE: " + blockchain.get(i).toString() 
					+ "BLOCK INDEX BLOCKCHAIN: " + i);
			System.out.println("");
		}
		
		this.verifyBlockchainValidate();
	}
	
	public void verifyBlockchainValidate() {
        boolean flag = true;
        System.out.println("VERIFICANDO BLOCKCHAIN.");
        for (int i = 0; i < blockchain.size(); i++) {
            String previousHash = i == 0 ? "0" : blockchain.get(i - 1).getHash();
            flag = blockchain.get(i)
                .getHash()
                .equals(blockchain.get(i)
                    .calculateBlockHash())
                && previousHash.equals(blockchain.get(i)
                    .getPreviousHash())
                && blockchain.get(i)
                    .getHash()
                    .substring(0, difficulty)
                    .equals(prefixString);
            if (!flag)
            	System.out.println("BLOCKCHAIN INV�LIDA.");
                break;
        }
        System.out.println("BLOCKCHAIN V�LIDA.");
    }
	
	public Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[3]).replace('\0', '0');
		HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
		tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
		
		//loop through blockchain to check hashes:
		for(int i=1; i < blockchain.size(); i++) {
			
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			//compare registered hash and calculated hash:
			if(!currentBlock.hash.equals(currentBlock.calculateBlockHash()) ){
				System.out.println("#Current Hashes not equal");
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.hash.equals(currentBlock.getPreviousHash()) ) {
				System.out.println("#Previous Hashes not equal");
				return false;
			}
			//check if hash is solved
			if(!currentBlock.hash.substring( 0, 3).equals(hashTarget)) {
				System.out.println("#This block hasn't been mined");
				return false;
			}
			
			//loop thru blockchains transactions:
			TransactionOutput tempOutput;
			for(int t=0; t <currentBlock.transactions.size(); t++) {
				Transaction currentTransaction = currentBlock.transactions.get(t);
				
				if(!currentTransaction.verifiySignature()) {
					System.out.println("#Signature on Transaction(" + t + ") is Invalid");
					return false; 
				}
				if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
					return false; 
				}
				
				for(TransactionInput input: currentTransaction.inputs) {	
					tempOutput = tempUTXOs.get(input.transactionOutputId);
					
					if(tempOutput == null) {
						System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
						return false;
					}
					
					if(input.UTXO.value != tempOutput.value) {
						System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
						return false;
					}
					
					tempUTXOs.remove(input.transactionOutputId);
				}
				
				for(TransactionOutput output: currentTransaction.outputs) {
					tempUTXOs.put(output.id, output);
				}
				
				if( currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient) {
					System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
					return false;
				}
				if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
					System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
					return false;
				}
				
			}
			
		}
		System.out.println("Blockchain is valid");
		return true;
	}
}
