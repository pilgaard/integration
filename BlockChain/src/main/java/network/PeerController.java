/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import controller.BlockChainController;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andreas
 */
public class PeerController {

    ServerSocket serverSocket;
    BlockChainController blockChainController;
    List<SocketHandler> activeSockets;
    Socket socket;
    PeerController self;

    //Temporary -- Run main - comment in other information.. run another instance
    int serverPort = 10008;
    String otherPeer = "localhost:10007";
    //int serverPort = 10007;
    //String otherPeer = "localhost:10008";
    public PeerController(BlockChainController blockChainController) {
        this.self = this;
        this.blockChainController = blockChainController;
        this.activeSockets = new ArrayList();
    }

    public int StartPeerServer() throws IOException {
        while (true) {
            try {
                serverSocket = new ServerSocket(serverPort);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                System.out.println("Accepting connections");
                                Socket clientSocket = serverSocket.accept();
                                SocketHandler socketHandler = new SocketHandler(blockChainController, clientSocket, self);
                                activeSockets.add(socketHandler);
                                new Thread(socketHandler).start();
                            } catch (Exception e) {
                                //exception handle
                            }
                        }
                    }
                }).start();
            } catch (Exception e) {
                //exception handle
            }
            return serverPort;
            //System.out.println("Server on: " + InetAddress.getLocalHost().getHostAddress() + " port: " + serverPort);
        }
    }

    public void ConnectToPeers() {
        String[] parts = otherPeer.split(":");
        boolean connected = false;
        while (true) {
            while (connected != true) {
                try {
                    socket = new Socket(parts[0], Integer.parseInt(parts[1]));
                    //DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    connected = true;
                } catch (Exception e) {
                    System.out.println("Not available..");
                    connected = false;
                }
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
    
    
}
