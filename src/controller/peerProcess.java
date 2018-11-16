package controller;
import java.net.*;
import controller.*;
import java.util.*;
import model.*;

public class peerProcess {
	private ServerSocket serverSocket;
	private List<Connection> peerConnectionList;
	private PeerInfo serverInfo;
	
	public peerProcess(int id) throws Exception {
		
		peerConnectionList = new ArrayList<>();
		
		serverInfo = PeerInfoManager.getInstance().getPeerInfoById(id);
		PeerInfoManager.getInstance().setMyInfo(serverInfo);
		FileManager.getInstance().setFile(serverInfo);
		
		//connect to peers before this server
		try {
			for (PeerInfo peer : PeerInfoManager.getInstance().getPeersBefore(serverInfo)) {
				System.out.println("connect to " + peer.getHost() + " " + peer.getPort());
				Socket socket = new Socket(peer.getHost(), peer.getPort());
				peerConnectionList.add(new Connection(socket, peer, this));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		//wait for peers connect later
		try {
			serverSocket = new ServerSocket(serverInfo.getPort());
			for (int i = 0; i < PeerInfoManager.getInstance().getPeersAfter(serverInfo).size(); i++) {
				
				Socket socket = serverSocket.accept();
				
				peerConnectionList.add(new Connection(socket, null, this));
			}

			//start timer. Temporarily begin timer after all peers connecting
			MyTimer mytimer = new MyTimer(peerConnectionList);
			mytimer.startTimer();
		
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			serverSocket.close();
		}
		

	}
	public List<Connection> getConnectionList() {
		return peerConnectionList;
	}
	
	public static void main(String[] args) {
		System.out.println("begin start server");
		try {
			new peerProcess(Integer.parseInt(args[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
