package br.unifor.blockchain;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Blockchain {
	
	private static Logger logger = Logger.getLogger(Blockchain.class.getName());
	
	private List<Block> blockchain = new ArrayList<>();
	private int prefix = 4;
	private final String prefixString = new String(new char[prefix]).replace('\0', '0');
	
	public Blockchain(List<Block> blockchain, int prefix) {
		this.blockchain = blockchain;
		this.prefix = prefix;
	}
	
	public void setUpOrReset() {
		logger.log(Level.INFO, "Limpando blockchain.");
		blockchain.clear();
		logger.log(Level.INFO, "Criando bloco genesis do blockchain.");
        Block genesisBlock = new Block("Bloco Genesis.", "0", new Date().getTime());
        logger.log(Level.INFO, "Minerando bloco genesis do blockchain.");
        logger.log(Level.INFO, "Adicionando bloco genesis na blockchain.");
        blockchain.add(genesisBlock);
        
        System.out.println("");
        
        logger.log(Level.INFO, "Criando primeiro bloco com previusHas do blockchain.");
        Block firstBlock = new Block("Primeiro bloco após o genesis.", genesisBlock.getHash(), new Date().getTime());
        logger.log(Level.INFO, "Minerando primeiro bloco com previusHas do blockchain.");
        logger.log(Level.INFO, "Adicionando primeiro bloco com previusHas na blockchain.");
        blockchain.add(firstBlock);
        
        System.out.println("");
    }
	
	public void addNewBlock(String textContentBlock) {
		logger.log(Level.INFO, "Criando novo bloco na blockchain.");
        Block newBlock = new Block(textContentBlock != null && textContentBlock.equals("") ? textContentBlock : "Novo bloco.", blockchain.get(blockchain.size() - 1).getHash(), new Date().getTime());
        logger.log(Level.INFO, "Minerando novo bloco na blockchain.");
        logger.log(Level.INFO, "Adicionando novo bloco na blockchain.");
        blockchain.add(newBlock);
    }
	
	
	public void minarateBlockchain() {
		for (int i = 0; i < blockchain.size(); i++) {
			System.out.println("");
			logger.log(Level.INFO, "HASH VÁLIDO ENCONTRADO: " + blockchain.get(i).mineBlock(prefix));
			logger.log(Level.INFO, blockchain.get(i).toString());
			logger.log(Level.INFO, "NONCE RESULTANTE: " + blockchain.get(i).toString() 
					+ "BLOCK INDEX BLOCKCHAIN: " + i);
			System.out.println("");
		}
		
		this.verifyBlockchainValidate();
	}
	
	public void verifyBlockchainValidate() {
        boolean flag = true;
        logger.log(Level.INFO, "VERIFICANDO BLOCKCHAIN.");
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
                    .substring(0, prefix)
                    .equals(prefixString);
            if (!flag)
            	logger.log(Level.INFO, "BLOCKCHAIN INVÁLIDA.");
                break;
        }
        logger.log(Level.INFO, "BLOCKCHAIN VÁLIDA.");
    }
}
