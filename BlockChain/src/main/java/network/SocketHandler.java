/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import controller.BlockChainController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Block;
import model.Transaction;

/**
 *
 * @author Andreas
 */
public class SocketHandler {

    Socket socket;
    ServerSocket serverSocket;
    List<Socket> clientSockets;
    public BlockChainController blockChainController;
    int port;

    public void asd() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(10007);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 10007.");
            System.exit(1);
        }
        while (true) {
            Socket clientSocket = null;
            System.out.println("Waiting for connection.....");

            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            System.out.println("Connection successful");
            System.out.println("Waiting for input.....");
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            
            out.println("WELCOME TO ANDREAS' & EMIL'S BLOCKCHAIN NODE");
            out.println("TYPE 'HELP' TO GET A LIST OF ALL COMMANDS");
            
            while ((inputLine = in.readLine()) != null) {
                
                if(inputLine.equals("help")) {
                    out.println("'view' - See all Blocks in BlockChain");
                    out.println("'show' - Show specific Transaction in BlockChain");
                    out.println("'add' - Add new Transaction to BlockChain");
                    out.println("'edit' - Edit a Transaction in BlockChain");
                    out.println("'mine' - Mine specific Block");
                    out.println("'validate' - Validate BlockChain");
                    out.println("'valimine' - Mine and Validate BlockChain");
                    out.println("'close' - Close your connection to the node");
                }
                
                if (inputLine.equals("view")) {
                    out.println("Printing all Blocks:");
                    out.println("###################################################");
                    for (int i = 0; i < blockChainController.getBlockChain().size(); i++) {
                        Block currentBlock = blockChainController.getBlockChain().get(i);
                        out.println("Block ID: " + currentBlock.getId());
                        out.println("Hash: " + currentBlock.getHash());
                        out.println("Previous: " + currentBlock.getPrevious());
                        out.println("Nonce: " + currentBlock.getNonce());
                    }
                    out.println("###################################################");
                }
                
                if(inputLine.equals("show")){
                    out.println("To see a specific Transaction, please use format: blockID:transactionID");
                    String transactionString = in.readLine();
                    String[] parts = transactionString.split(":");
                    try{
                        Transaction t = blockChainController.GetTransaction(Integer.parseInt(parts[0]), Integer.parseInt(parts[0]));
                        out.println("Value: " + t.getValue() + " To: " + t.getTo()+ " From: " + t.getFrom());
                    }catch(NumberFormatException nfe){
                        out.println("Value was not a number, silly");
                    }
                }
                
                if(inputLine.equals("add")){
                    out.println("Adding new Transaction to BlockChain! Please use format: value:to:from");
                    String transactionString = in.readLine();
                    String[] parts = transactionString.split(":");
                    try {
                        Transaction t = new Transaction(Integer.parseInt(parts[0]), parts[1], parts[2]);
                        blockChainController.AddTransactionToBlockChain(new Transaction(100, "Andreas", "Emil"));
                        out.println("New Transaction added to BlockChain!");
                    } catch(NumberFormatException nfe){
                        out.println("Value was not a number, silly");
                    }
                }
                
                if(inputLine.equals("edit")){
                    out.println("Editing a Transaction! Please use format: blockID:transactionID:value");
                    String editString = in.readLine();
                    try{
                        String[] parts = editString.split(":");
                        blockChainController.EditTransaction(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), out);
                    }catch(NumberFormatException nfe){
                        out.println("Value was not a number, silly");
                    }
                }
                
                if(inputLine.equals("validate")){
                    blockChainController.ValidateBlockChain(out);
                }
                
                if(inputLine.equals("mine")){
                    out.println("Mining specific Block! Please enter a valid Block ID!");
                    String inputID = in.readLine();
                    try{
                        blockChainController.MineBlock(Integer.parseInt(inputID), out);
                        out.println("Done");
                    }catch(NumberFormatException nfe){
                        out.println("ID was not a number, silly");
                    }
                    
                }
                
                if(inputLine.equals("valimine")){
                    blockChainController.MineAndValidateAllBlocks(out);
                }

                if (inputLine.equals("close")) {
                    out.println("Closing your connection!");
                    break;
                }
            }
            closeClient(out, in, clientSocket);
        }
    }
    
    private void closeClient(PrintWriter out, BufferedReader in, Socket clientSocket) throws IOException{
        out.close();
        in.close();
        clientSocket.close();
    }

}
