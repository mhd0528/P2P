package controller;
import java.io.*;
import java.util.*;
import model.*;

public class ArgReader {
	private int NumberOfPreferredNeighbors;
	private int UnchokingInterval;
	private int OptimisticUnchokingInterval;
	private String fileName;
	private int fileSize;
	private int pieceSize;
	private static class SingletonHolder {
		public final static ArgReader instance = new ArgReader();
	}
	private ArgReader() {
		try {
			FileReader reader = new FileReader("./Common.cfg");
            BufferedReader bufferedReader = new BufferedReader(reader);
            //peerInfoList = new ArrayList<>();
            String str = null;
            while((str = bufferedReader.readLine()) != null) {
            	String[] netArgs = str.split(" ");
            	if (netArgs[0].equals("NumberOfPreferredNeighbors")){
            		NumberOfPreferredNeighbors = Integer.parseInt(netArgs[1]);
            	}
            	else if(netArgs[0].equals("UnchokingInterval")){
            		UnchokingInterval = Integer.parseInt(netArgs[1]);
            	}
            	else if(netArgs[0].equals("OptimisticUnchokingInterval")){
            		OptimisticUnchokingInterval = Integer.parseInt(netArgs[1]);
            	}
            	else if(netArgs[0].equals("FileName")){
            		fileName = netArgs[1];
            	}
            	else if(netArgs[0].equals("FileSize")){
            		fileSize = Integer.parseInt(netArgs[1]);
            	}
            	else{
            		pieceSize = Integer.parseInt(netArgs[1]);
            	}
            	
            }
            
            bufferedReader.close();
            reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static ArgReader getInstance() {
		return SingletonHolder.instance;
	}
	
	public int getNumberOfPreferredNeighbors(){
		return NumberOfPreferredNeighbors;
	}
	
	public int getUnchokingInterval(){
		return UnchokingInterval;
	}
	
	public int getOptimisticUnchokingInterval(){
		return OptimisticUnchokingInterval;
	}

	public String getfileName(){
		return fileName;
	}
	
	public int getfileSize(){
		return fileSize;
	}
	
	public int getpieceSize(){
		return pieceSize;
	}

}
