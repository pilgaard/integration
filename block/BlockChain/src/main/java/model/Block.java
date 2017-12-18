/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.common.hash.Hashing;
import com.sun.xml.internal.ws.util.StringUtils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 *
 * @author Andreas
 */
public class Block {
    
    int id;
    int nonce;
    String previous;
    String hash;
    ArrayList<Transaction> transactions;

    //Genesis block
    public Block() {
        this.id = 1;
        this.transactions = new ArrayList<>();
        this.previous = "0000000000000000000000000000000000000000000000000000000000000000";
        this.nonce = 0;
        this.hash = GenerateHash(nonce, previous, transactions);
        while(!hash.substring(0, 4).equals("0000")){
            nonce = nonce + 1;
            this.hash = GenerateHash(nonce, previous, transactions);
        }      
    }

    //Normal block
    public Block(int id, String previous) {
        this.id = id;
        this.previous = previous;
        this.transactions = new ArrayList<>();
        this.nonce = 0;
        this.hash = GenerateHash(nonce, previous, transactions);
        while(!hash.substring(0, 4).equals("0000")){
            nonce = nonce + 1;
            this.hash = GenerateHash(nonce, previous, transactions);
        }    
    }
    
    public String GenerateHash(int nonce, String previous, ArrayList<Transaction> transactions){  
        String nonceString = Integer.toString(nonce); 
        String transactionString = "";
        for(Transaction s : transactions){
            transactionString = transactionString + Integer.toString(s.getValue()) + s.from + s.to;
        }
        return Hashing.sha256()
                .hashString(nonceString + previous + transactionString, StandardCharsets.UTF_8)
                .toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }
    
    
    
    
    
    
    
}
