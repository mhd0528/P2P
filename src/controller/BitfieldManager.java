package controller;
import java.io.*;
import java.util.*;
import model.*;
import custom.*;
import java.math.*;

public class BitfieldManager {
	private static class SingletonHolder {
		public final static BitfieldManager instance = new BitfieldManager();
	}
	
	
	private int pieceNum = (int)Math.ceil((double)ArgReader.getInstance().getfileSize()/(double)ArgReader.getInstance().getpieceSize());
	//record required bits
	boolean requiredPieces[] = new boolean[pieceNum];
	HashMap<PeerInfo, boolean[]> bitFields = new HashMap<>();
	private BitfieldManager(){
		//initiate
		Arrays.fill(requiredPieces, false);
		//initiate bit fields of every peer
		for (PeerInfo peer : PeerInfoManager.getInstance().getPeers()){
			boolean singlePeer[] = new boolean[pieceNum];
			if(peer.getHasFile()){
				Arrays.fill(singlePeer, true);
				bitFields.put(peer, singlePeer);
			}
			else{
				Arrays.fill(singlePeer, false);
				bitFields.put(peer, singlePeer);
			}
		}
	}
	public static BitfieldManager getInstance(){

		return SingletonHolder.instance;
	}

	//for receiving have message
	synchronized public void updateBitfield(PeerInfo peerInfo, int pieceNum){
		//othersBitfield[peerID][pieceNum] = true;
		boolean[] tmp = bitFields.get(peerInfo);
		tmp[pieceNum] = true;
	}
	
	synchronized public int compareAndchoose(PeerInfo peerInfo){
		//compare if there is interested pieces
		boolean[] myBitField = bitFields.get(PeerInfoManager.getInstance().getMyInfo());
		boolean[] destBitField = bitFields.get(peerInfo);
		List<Integer> diff = new ArrayList<>();
		boolean flag = false;
		for (int i = 0; i < pieceNum; i++){
			//have not received && have not been required 
			if(myBitField[i] == false && destBitField[i] == true && requiredPieces[i] == false){
				diff.add(i);
				flag = true;
			}
		}
		//no interested pieces
		if (flag == false){
			return -1;
		}
		else {
			//random choose a file piece
			Collections.shuffle(diff);
			int want = diff.get(0);
			//record that this pieces has been required
			requiredPieces[want] = true;
			return want;
		}
		
	}
	
	//just compare if there is interested piece
	public boolean comparePeerInfo(PeerInfo peerInfo){
		//compare if there is interested pieces
		boolean[] myBitField = bitFields.get(PeerInfoManager.getInstance().getMyInfo());
		boolean[] destBitField = bitFields.get(peerInfo);
		boolean flag = false;
		for (int i = 0; i < pieceNum; i++){
			//have not received && have not been required 
			if(myBitField[i] == false && destBitField[i] == true && requiredPieces[i] == false){
				flag = true;
			}
		}
		//no interested pieces
		if (flag == false){
			return false;
		}
		else {
			return true;
		}
	}
	
	public boolean[] getBitField(PeerInfo peerInfo){
		boolean[] tmp = bitFields.get(peerInfo);
		return tmp;
	}
	
	public int getpieceNum(){
		return pieceNum;
	}
	
	public boolean isAllReceived(PeerInfo peerInfo){
		boolean[] tmp = bitFields.get(peerInfo);
		//check if tmp is all 1
		//if true--->is all received
		if (Util.allTrue(tmp)){
			return true;
		}
		else {
			return false;
		}
	}
}

