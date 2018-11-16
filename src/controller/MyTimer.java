package controller;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import custom.Config.MessageType;
import model.Connection;
import model.Message;

public class MyTimer extends Thread{
	private List<Connection> connectionList;
	private List<Connection> preferedList;
	private Connection optimisticPeer;
	private int unchokingInterval;
	private int optimisticUnchokingInterval;
	private long lastUnchokingTime;
	private long lastOptimisticUnchokingTime;
	private boolean startFlag;
	
	public MyTimer(List<Connection> connectionList) {
		this.connectionList = connectionList;
		unchokingInterval = ArgReader.getInstance().getUnchokingInterval() * 1000;
		optimisticUnchokingInterval = ArgReader.getInstance().getOptimisticUnchokingInterval() * 1000;
		preferedList = new ArrayList<>();
		lastUnchokingTime = 0;
		lastOptimisticUnchokingTime = 0;
		startFlag = true;
	}
	public void startTimer() {
		startFlag = true;
		super.start();
	}
	public void stopTimer() {
		startFlag = false;
	}
	public List<Connection> getPreferedList() {
		return preferedList;
	}
	
	@Override
	public void run() {
		while(startFlag) {
			long currentMillis = System.currentTimeMillis();
			//select preferred neighbor
			if (currentMillis - lastUnchokingTime > unchokingInterval) {
				lastUnchokingTime = currentMillis;
				List<Connection> interestedList = new ArrayList<>();
				List<Connection> newPreferedList = new ArrayList<>();
				//get interest and have speed peers
				for (Connection connection : connectionList) {
					if (connection.getInterestedFlag() && connection.getDownloadingNumOfPeriod() > 0) {
						interestedList.add(connection);
					}
				}
				//select new prefer list
				if (interestedList.size() > 0) {
					if (interestedList.size() <= ArgReader.getInstance().getNumberOfPreferredNeighbors()) {
						newPreferedList = interestedList;
					} else {
						//sort by downloadingNum
						Collections.sort(interestedList, new Comparator<Connection>() {
							@Override
							public int compare(Connection c1, Connection c2) {
								return c2.getDownloadingNumOfPeriod() - c1.getDownloadingNumOfPeriod();
							}
						});
						//add k fast
						for (int i = 0; i < ArgReader.getInstance().getNumberOfPreferredNeighbors(); i++) {
							newPreferedList.add(interestedList.get(i));
						}
					}
				}
				for (Connection connection : newPreferedList) {
					if (!preferedList.contains(connection)) {
						//not in previous preferredList, send unchokeMessage
						connection.sendMessage(new Message(MessageType.UNCHOKE, null));
					}
				}
				for (Connection connection : connectionList) {
					if (!newPreferedList.contains(connection) && connection.getSendedHandShake()) {
						//not in new preferredList, send chokeMessage
						connection.sendMessage(new Message(MessageType.CHOKE, null));
					}
				}
				//reset the all connection downloadingNum to zero
				for (Connection connection : connectionList) {
					connection.resetDownloadingNum();
				}
				//set the newPreferedList as preferedList
				preferedList = newPreferedList;
			}
			
			//select optimistic neighbor
			if (currentMillis - lastOptimisticUnchokingTime > optimisticUnchokingInterval) {
				List<Connection> interestedButNotInPreferredList = new ArrayList<>();
				lastOptimisticUnchokingTime = currentMillis;
				for (Connection connection : connectionList) {
					//confirm the connection already finished handShake
					if (connection.getInterestedFlag() && !preferedList.contains(connection)) {
						interestedButNotInPreferredList.add(connection);
					}
				}
				//randomly select one
				if (interestedButNotInPreferredList.size() > 0) {
					Collections.shuffle(interestedButNotInPreferredList);
					optimisticPeer = interestedButNotInPreferredList.get(0);
					//send unchokeMessage
					optimisticPeer.sendMessage(new Message(MessageType.UNCHOKE, null));
				}
				
			}
		}
	}
}
