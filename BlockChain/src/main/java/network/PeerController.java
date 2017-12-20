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
    //Socket socket;
    ArrayList<Socket> peerSockets;
    PeerController self;
    ArrayList<String> peers;
    ArrayList<String> connectedPeers;

    //Temporary -- Run main - comment in other information.. run another instance
    int port = 10008;
    String otherPeer = "localhost:10007";

    //int port = 10007;
    //String otherPeer = "localhost:10008";

    public PeerController(BlockChainController blockChainController) {
        this.self = this;
        this.blockChainController = blockChainController;
        this.activeSockets = new ArrayList();
        this.peers = new ArrayList();
        this.peerSockets = new ArrayList();
        this.connectedPeers = new ArrayList();
    }

    public int StartPeerServer() throws IOException {
        while (true) {
            try {
                serverSocket = new ServerSocket(port);
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
            return port;
            //System.out.println("Server on: " + InetAddress.getLocalHost().getHostAddress() + " port: " + port);
        }
    }

    public void ConnectToPeers() {
        for ( String peer : peers ) {
            boolean connected = false;
            String[] parts = peer.split(":");
            while (true) {
                while (connected != true) {
                    try {
                        Socket socket = new Socket(parts[0], Integer.parseInt(parts[1]));
                        peerSockets.add(socket);
                        connected = true;
                    } catch (Exception e) {
                        System.out.println("Not available..");
                        connected = false;
                    }
                }
            }
        }
    }

    public void Start() throws IOException {
        this.port = 10006;
        if (System.getenv("PEER_PORT") != null) {
            this.port = Integer.parseInt(System.getenv("PEER_PORT"));
        }
        System.out.println("Port: " + this.port);
        StartPeerServer();
        if (System.getenv("PEERS") != null & System.getenv("PEERS") != null & System.getenv("PEERS") != null) {
            peers.add(System.getenv("PEER1"));
            peers.add(System.getenv("PEER2"));
            peers.add(System.getenv("PEER3"));
        }
    }

    public ArrayList<Socket> getPeerSockets() {
        return peerSockets;
    }

    

}
