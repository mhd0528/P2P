package model;
import java.io.*;
import java.text.*;
import java.util.*;

import controller.ArgReader;

public class PeerInfo {
	private int peerID;
	private String host;
	private int port;
	private Boolean hasFile;
	private File file;
	private String filePath;
	BufferedWriter writer;
	
	public PeerInfo(int peerID, String host, int port, Boolean hasFile) {
		this.peerID = peerID;
		this.host = host;
		this.port = port;
		this.hasFile = hasFile;
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return peerID;
	}
	public String getHost() {
		return host;
	}
	public int getPort() {
		return port;
	}
	public Boolean getHasFile() {
		return hasFile;
	}

}
