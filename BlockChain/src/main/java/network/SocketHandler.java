/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.BlockChainController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Block;
import model.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Andreas
 */
public class SocketHandler implements Runnable {

    public BlockChainController blockChainController;
    private Socket clientSocket;
    private PeerController peerController;
    private BufferedReader in;
    private Gson gson;

    public SocketHandler(BlockChainController blockChainController, Socket clientSocket, PeerController peerController) {
        this.blockChainController = blockChainController;
        this.clientSocket = clientSocket;
        this.peerController = peerController;
        this.gson = new GsonBuilder().create();
    }

    public void HandleClient() throws IOException, Exception {

        System.out.println("Connection successful");
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
                true);
        in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;

        out.println("WELCOME TO ANDREAS' & EMIL'S BLOCKCHAIN NODE");
        out.println("TYPE 'HELP' TO GET A LIST OF ALL COMMANDS");

        while ((inputLine = in.readLine()) != null) {

            // #################################################
            // ################## USER INPUTS ##################
            // #################################################
            if (inputLine.equals("help")) {
                out.println("------------------------ NODE SPECIFIC USER INPUTS ------------------------");
                out.println("'view' - See all Blocks in BlockChain");
                out.println("'show' - Show specific Transaction in BlockChain");
                out.println("'add' - Add new Transaction to BlockChain");
                out.println("'edit' - Edit a Transaction in BlockChain");
                out.println("'mine' - Mine specific Block");
                out.println("'validate' - Validate BlockChain");
                out.println("'valimine' - Mine and Validate BlockChain");
                out.println("'close' - Close your connection to the node");
                out.println("'consult' - Consult other peers");
                out.println("'update_peers' - Updates the BlockChain of all nodes to the one of this node");
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

            if (inputLine.equals("show")) {
                out.println("To see a specific Transaction, please use format: blockID:transactionID");
                String transactionString = in.readLine();
                String[] parts = transactionString.split(":");
                try {
                    Transaction t = blockChainController.GetTransaction(Integer.parseInt(parts[0]), Integer.parseInt(parts[0]));
                    out.println("Value: " + t.getValue() + " To: " + t.getTo() + " From: " + t.getFrom());
                } catch (NumberFormatException nfe) {
                    out.println("Value was not a number, silly");
                }
            }

            if (inputLine.equals("add")) {
                out.println("Adding new Transaction to BlockChain! Please use format: value:to:from");
                String transactionString = in.readLine();
                String[] parts = transactionString.split(":");
                try {
                    Transaction t = new Transaction(Integer.parseInt(parts[0]), parts[1], parts[2]);
                    blockChainController.AddTransactionToBlockChain(new Transaction(100, "Andreas", "Emil"));
                    out.println("New Transaction added to BlockChain!");
                } catch (NumberFormatException nfe) {
                    out.println("Value was not a number, silly");
                }
            }

            if (inputLine.equals("edit")) {
                out.println("Editing a Transaction! Please use format: blockID:transactionID:value");
                String editString = in.readLine();
                try {
                    String[] parts = editString.split(":");
                    blockChainController.EditTransaction(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), out);
                } catch (NumberFormatException nfe) {
                    out.println("Value was not a number, silly");
                }
            }

            if (inputLine.equals("validate")) {
                blockChainController.ValidateBlockChain(out);
            }

            if (inputLine.equals("mine")) {
                out.println("Mining specific Block! Please enter a valid Block ID!");
                String inputID = in.readLine();
                try {
                    blockChainController.MineBlock(Integer.parseInt(inputID), out);
                    out.println("Done");
                } catch (NumberFormatException nfe) {
                    out.println("ID was not a number, silly");
                }
            }
            if (inputLine.equals("valimine")) {
                blockChainController.MineAndValidateAllBlocks(out);
            }
            if (inputLine.equals("close")) {
                out.println("Closing your connection!");
                break;
            }

            if (inputLine.equals("consult")) {
                consultPeers(out);
            }

            if (inputLine.equals("update_peers")) {
                out.println("Updating all peers..");
                updatePeers(out);
                out.println("Done");
            }

            // #################################################
            // #############  PEER COMMUNICATION  ##############
            // #################################################
            if (inputLine.equals("_changes")) {
                out.println(blockChainController.getNoOfChanges());
            }

            if (inputLine.split(" ")[0].equals("_update")) {
                handleUpdate((inputLine.split(" ")[1]), in);
            }

        }
        closeClient(out, in, clientSocket);
    }

    private void consultPeers(PrintWriter clientWriter) throws IOException, InterruptedException, Exception {
        int myChanges = blockChainController.getNoOfChanges();
        List<Integer> changeData = new ArrayList<>();
        int highest = 0;

        for (Socket s : peerController.getPeerSockets()) {

            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println("_changes");

            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                try {
                    Integer.parseInt(currentLine);
                    break;
                } catch (Exception e) {
                    System.out.println("was not a number: " + currentLine);
                }
            }
            changeData.add(Integer.parseInt(currentLine));
        }
        for (int data : changeData) {
            clientWriter.println("Other node is at: " + data + " changes");
            if (data > highest) {
                highest = data;
            }
        }
        //int avgData = totalData / changeData.size();
        clientWriter.println("This node is at: " + myChanges + " changes");
        if (myChanges > highest) {
            clientWriter.println("This BlockChain is ahead");
        } else if (myChanges < highest) {
            clientWriter.println("This BlockChain is behind");
        } else {
            clientWriter.println("BlockChains are equal");
        }
    }

    private void updatePeers(PrintWriter clientWriter) throws IOException {
        String myJson = gson.toJson(blockChainController.getBlockChain());
        blockChainController.setNoOfChanges(0);
        for (Socket s : peerController.getPeerSockets()) {
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            out.println("_update " + myJson);
        }
    }

    private void handleUpdate(String message, BufferedReader in) throws IOException {
        String json = message;

        JSONArray jData = new JSONArray(json);
        System.out.println("recieved json data: " + jData);
        ArrayList<Block> newBlockChain = new ArrayList<>();
        ArrayList<Transaction> transactions;
        for (int i = 0; i < jData.length(); i++) {
            transactions = new ArrayList<>();
            JSONObject jObj = jData.getJSONObject(i);
            Block newBlock = new Block(
                    jObj.getInt("id"),
                    jObj.getInt("nonce"),
                    jObj.getString("previous"),
                    jObj.getString("hash")
            );
            JSONArray jTransactions = jObj.getJSONArray("transactions");
            for (int j = 0; j < jTransactions.length(); j++) {
                JSONObject transJObj = jTransactions.getJSONObject(j);
                System.out.println("Object: " + transJObj);
                Transaction newTransactions = new Transaction(
                        transJObj.getInt("value"),
                        transJObj.getString("from"),
                        transJObj.getString("to")
                );
                transactions.add(newTransactions);
            }
            newBlock.setTransactions(transactions);
            newBlockChain.add(newBlock);
        }
        blockChainController.setBlockChain(newBlockChain);
        blockChainController.setNoOfChanges(0);
    }

    private void closeClient(PrintWriter out, BufferedReader in, Socket clientSocket) throws IOException {
        out.close();
        in.close();
        clientSocket.close();
    }

    public void run() {
        try {
            HandleClient();
        } catch (IOException ex) {
            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
