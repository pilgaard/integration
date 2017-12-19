/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import model.Block;
import model.Transaction;

/**
 *
 * @author Andreas
 */
public class BlockChainController {

    ArrayList<Block> blockChain;

    public BlockChainController() {
        this.blockChain = new ArrayList<>();
    }

    public void AddGenesisBlock() {
        this.blockChain.add(new Block());
        System.out.println("Added GenesisBlock to BlockChain:");
        System.out.println("GenesisBlock id: " + this.blockChain.get(0).getId());
        System.out.println("GenesisBlock hash: " + this.blockChain.get(0).getHash());
    }

    public void AddBlock() {
        Block lastBlock = this.blockChain.get(this.blockChain.size() - 1);
        int newBlockId = blockChain.size() + 1;
        Block blockToAdd = new Block(newBlockId, lastBlock.getHash());
        this.blockChain.add(blockToAdd);
        System.out.println("Added new Block to BlockChain:");
        System.out.println("Block id: " + blockToAdd.getId());
        System.out.println("Block hash: " + blockToAdd.getHash());
    }

    public boolean ValidateBlockChain(PrintWriter out) {
        out.println("Validating BlockChain..");
        int totalFails = 0;
        for (int i = 0; i < this.blockChain.size(); i++) {
            if (!this.blockChain.get(i).getHash().substring(0, 4).equals("0000")) {
                out.println("Block with ID: " + this.blockChain.get(i).getId() + " failed.");
                totalFails++;
            }
        }
        if (totalFails == 0) {
            out.println("Validation result: " + totalFails + " out of " + this.blockChain.size() + " failed.");
            return true;
        } else {
            out.println("Validation result: " + totalFails + " out of " + this.blockChain.size() + " failed.. - Please Mine Blocks and try again.");
            return false;
        }
    }

    public void AddTransactionToBlock(int id, Transaction transactionToAdd) {
        Block blockToUpdate = this.blockChain.get(id - 1);
        blockToUpdate.getTransactions().add(transactionToAdd);
        System.out.println(transactionToAdd.getFrom() + " transfered " + transactionToAdd.getValue() + " to " + transactionToAdd.getTo());
        UpdateBlocks(id);
    }

    public void AddTransactionToBlockChain(Transaction transactionToAdd) {
        Block latestBlock = this.blockChain.get(this.blockChain.size() - 1);
        if (latestBlock.getTransactions().size() == 5) {
            AddBlock();
            latestBlock = this.blockChain.get(this.blockChain.size() - 1);
        }
        latestBlock.getTransactions().add(transactionToAdd);
        System.out.println(transactionToAdd.getFrom() + " transfered " + transactionToAdd.getValue() + " to " + transactionToAdd.getTo());
        UpdateBlocks(latestBlock.getId());
    }

    public void UpdateBlocks(int id) {
        //System.out.println("Updating blocks..");
        int index = id - 1;
        for (int i = index; i < this.blockChain.size(); i++) {
            Block b = this.blockChain.get(i);
            if (i == index) {
                String newHash = b.GenerateHash(b.getNonce(), b.getPrevious(), b.getTransactions());
                b.setHash(newHash);
            } else {
                b.setPrevious(this.blockChain.get(i - 1).getHash());
                b.setHash(b.GenerateHash(b.getNonce(), b.getPrevious(), b.getTransactions()));
            }
        }
    }

    public void MineBlock(int blockID, PrintWriter out) {
        out.println("Mining Block with ID: " + blockID);
        Block blockToMine = this.blockChain.get(blockID - 1);
        while (!blockToMine.getHash().substring(0, 4).equals("0000")) {
            blockToMine.setNonce(blockToMine.getNonce() + 1);
            blockToMine.setHash(blockToMine.GenerateHash(blockToMine.getNonce(), blockToMine.getPrevious(), blockToMine.getTransactions()));
            if (blockToMine.getHash().substring(0, 4).equals("0000")) {
                UpdateBlocks(blockID);
            }
        }
    }

    public void MineAndValidateAllBlocks(PrintWriter out) {
        out.println("Mining all Blocks..");
        for (int i = 0; i < this.blockChain.size(); i++) {
            MineBlock(i + 1, out);
        }
        ValidateBlockChain(out);
    }

    public void EditTransaction(int blockID, int transactionID, int newValue, PrintWriter out) {
        try {
            Block blockToEdit = this.blockChain.get(blockID - 1);
            Transaction transactionToEdit = blockToEdit.getTransactions().get(transactionID - 1);
            transactionToEdit.setValue(newValue);
            UpdateBlocks(blockID);
            //ValidateBlockChain(out);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("The specified Block or Transaction does not exist.. Please Try again");
        }
    }

    public Transaction GetTransaction(int blockID, int transactionID) {
        try {
            Transaction t = this.blockChain.get(blockID).getTransactions().get(transactionID);
            return t;
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("The specified Block or Transaction does not exist.. Please Try again");
        }
        return null;
    }

    public ArrayList<Block> getBlockChain() {
        return blockChain;
    }

    public void setBlockChain(ArrayList<Block> blockChain) {
        this.blockChain = blockChain;
    }

}
