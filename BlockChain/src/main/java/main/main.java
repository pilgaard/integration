/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.BlockChainController;
import java.io.IOException;
import model.Block;
import model.Transaction;
import network.SocketHandler;

/**
 *
 * @author Andreas
 */
public class main {

    public static void main(String[] args) throws IOException {
        BlockChainController bcc = new BlockChainController();
        SocketHandler sh = new SocketHandler();
        sh.blockChainController = bcc;
        GenerateBlockChain(bcc);
        //manipulateBlockChain(bcc);
        sh.asd();
    }

    private static void GenerateBlockChain(BlockChainController bcc) {
        //add genesis block
        bcc.AddGenesisBlock();

        //add testing data
        bcc.AddTransactionToBlockChain(new Transaction(100, "Andreas", "Emil"));
        bcc.AddTransactionToBlockChain(new Transaction(50, "Emil", "Bo"));
        bcc.AddTransactionToBlockChain(new Transaction(25, "Emil", "Jonas"));
        bcc.AddTransactionToBlockChain(new Transaction(5, "Bo", "Andreas"));
        bcc.AddTransactionToBlockChain(new Transaction(10, "Andreas", "Emil"));
        bcc.AddTransactionToBlockChain(new Transaction(50, "Bo", "Josefine"));
        bcc.AddTransactionToBlockChain(new Transaction(15, "Emil", "Kasper"));
        bcc.AddTransactionToBlockChain(new Transaction(30, "Kasper", "Emil"));
        bcc.AddTransactionToBlockChain(new Transaction(100, "Andreas", "Emil"));
        bcc.AddTransactionToBlockChain(new Transaction(50, "Emil", "Bo"));
        bcc.AddTransactionToBlockChain(new Transaction(25, "Emil", "Jonas"));
        bcc.AddTransactionToBlockChain(new Transaction(5, "Bo", "Andreas"));
        bcc.AddTransactionToBlockChain(new Transaction(10, "Andreas", "Emil"));
        bcc.AddTransactionToBlockChain(new Transaction(50, "Bo", "Josefine"));
        bcc.AddTransactionToBlockChain(new Transaction(15, "Emil", "Kasper"));
        bcc.AddTransactionToBlockChain(new Transaction(30, "Kasper", "Emil"));
        bcc.AddTransactionToBlockChain(new Transaction(100, "Andreas", "Emil"));
        bcc.AddTransactionToBlockChain(new Transaction(50, "Emil", "Bo"));
        bcc.AddTransactionToBlockChain(new Transaction(25, "Emil", "Jonas"));
        bcc.AddTransactionToBlockChain(new Transaction(5, "Bo", "Andreas"));
        bcc.AddTransactionToBlockChain(new Transaction(10, "Andreas", "Emil"));
        bcc.AddTransactionToBlockChain(new Transaction(50, "Bo", "Josefine"));
        bcc.AddTransactionToBlockChain(new Transaction(15, "Emil", "Kasper"));
        bcc.AddTransactionToBlockChain(new Transaction(30, "Kasper", "Emil"));
        bcc.AddTransactionToBlockChain(new Transaction(100, "Andreas", "Emil"));
        bcc.AddTransactionToBlockChain(new Transaction(50, "Emil", "Bo"));
        bcc.AddTransactionToBlockChain(new Transaction(25, "Emil", "Jonas"));
        bcc.AddTransactionToBlockChain(new Transaction(5, "Bo", "Andreas"));
        bcc.AddTransactionToBlockChain(new Transaction(10, "Andreas", "Emil"));
        bcc.AddTransactionToBlockChain(new Transaction(50, "Bo", "Josefine"));
        bcc.AddTransactionToBlockChain(new Transaction(15, "Emil", "Kasper"));
        bcc.AddTransactionToBlockChain(new Transaction(30, "Kasper", "Emil"));
        bcc.AddTransactionToBlockChain(new Transaction(100, "Andreas", "Emil"));
        bcc.AddTransactionToBlockChain(new Transaction(50, "Emil", "Bo"));
        bcc.AddTransactionToBlockChain(new Transaction(25, "Emil", "Jonas"));
        bcc.AddTransactionToBlockChain(new Transaction(5, "Bo", "Andreas"));
        bcc.AddTransactionToBlockChain(new Transaction(10, "Andreas", "Emil"));
        bcc.AddTransactionToBlockChain(new Transaction(50, "Bo", "Josefine"));
        bcc.AddTransactionToBlockChain(new Transaction(15, "Emil", "Kasper"));
        bcc.AddTransactionToBlockChain(new Transaction(30, "Kasper", "Emil"));
    }
    
    private static void manipulateBlockChain(BlockChainController bcc){
        /*
        //See if blockchain is valid
        bcc.ValidateBlockChain();
        
        //Mine entire blockchain to get Sha256 hashes starting with 0000
        bcc.MineAndValidateAllBlocks();
        
        //Edit block 5 transaction 4's value to 1000
        bcc.EditTransaction(5, 4, 1000);
        
        //Mine and validate blockchain to fix hashes after edit in blockchain
        bcc.MineAndValidateAllBlocks();
        
        //edit block 3 transaction 2's values to 100
        bcc.EditTransaction(3, 2, 100);
        
        //manually mine block 3
        bcc.MineBlock(3);
        
        //validate to see that the manual mine was succesful
        bcc.ValidateBlockChain();
        
        //manually mine rest of blocks
        bcc.MineBlock(4);
        bcc.MineBlock(5);
        bcc.MineBlock(6);
        bcc.MineBlock(7);
        bcc.MineBlock(8);
        
        //validate to see if manual mines were succesful
        bcc.ValidateBlockChain();
                */
    }

    private static void printChain(BlockChainController bcc) {
        for (int i = 0; i < bcc.getBlockChain().size(); i++) {
            Block currentBlock = bcc.getBlockChain().get(i);
            System.out.println("Block " + currentBlock.getId() + " hash: " + currentBlock.getHash());
            System.out.println("Block " + currentBlock.getId() + " previous: " + currentBlock.getPrevious());
        }
    }
    
    private static void openSocket(SocketHandler sh) throws IOException{
        sh.asd();
    }

}
