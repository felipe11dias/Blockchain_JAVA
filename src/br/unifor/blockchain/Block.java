package br.unifor.blockchain;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Block {
	
	private static Logger logger = Logger.getLogger(Block.class.getName());
	
    private String hash;
    private String previousHash;
    private String textContent;
    private long timeStamp;
    private int nonce;
 
    public Block(String textContent, String previousHash, long timeStamp) {
        this.textContent = textContent;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.hash = calculateBlockHash();
    }
    
    public String mineBlock(int prefix) {
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        while (!hash.substring(0, prefix)
            .equals(prefixString)) {
            nonce++;
            hash = calculateBlockHash();
        }
        return hash;
    }
    
    public String calculateBlockHash() {
        String dataToHash = previousHash 
          + Long.toString(timeStamp) 
          + Integer.toString(nonce) 
          + textContent;
        MessageDigest digest = null;
        byte[] bytes = null;
        
        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
        	logger.log(Level.SEVERE, ex.getMessage());
        }
        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes) {
            buffer.append(String.format("%02x", b));
        }
        return buffer.toString();
    }

	@Override
	public String toString() {
		return "Block [hash=" + hash + ", previousHash=" + previousHash + ", textContent=" + textContent
				+ ", timeStamp=" + timeStamp + ", nonce=" + nonce + "]";
	}
    
	public String getHash() {
		return hash;
	}
	
	public String getPreviousHash() {
		return previousHash;
	}
}