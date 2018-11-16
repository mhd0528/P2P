package controller;

import java.io.*;
import controller.ArgReader;
import model.PeerInfo;

public class FileManager {
	private static class SingletonHolder {
		public final static FileManager instance = new FileManager();
	}
	private String filePath;
	private File file;
	private FileManager() {
		
	}
	public static FileManager getInstance() {
		return SingletonHolder.instance;
	}
	public void setFile(PeerInfo peerInfo) {
		this.filePath = "peer_" + peerInfo.getId() + "/" + ArgReader.getInstance().getfileName();		//file address
		this.file = new File(filePath);
		if(peerInfo.getHasFile()) {				//When we are supposed to have the file, we do
			if(!file.exists()) {
				System.out.println("Error: File doesn't exist.");
				System.exit(0);
			}
		}
		else {						//When we are not supposed to have the file, we do
			File dir = new File("peer_" + peerInfo.getId());
			dir.mkdir();
			file.delete();			//Delete and start writing to .temp file to keep intention clean
			file = new File(filePath + ".temp");
			//file.delete();
			
		}
		
	}
	
	public byte[] read(int piece) {
		try {
			RandomAccessFile input = new RandomAccessFile(file, "r");		//Open RandomAccessFile to read at desired position
			input.seek(piece * ArgReader.getInstance().getpieceSize());					//Seek to desired position
			int piecesNum = ArgReader.getInstance().getfileSize() / ArgReader.getInstance().getpieceSize();		//The number of pieces
			if(ArgReader.getInstance().getfileSize() % ArgReader.getInstance().getpieceSize() != 0) {			//Last piece is not as big as before
				piecesNum ++;									
			}
			byte[] pieceBuffer;
			if(piece == (piecesNum - 1)) {
				pieceBuffer = new byte[ArgReader.getInstance().getfileSize() - (piecesNum - 1) * ArgReader.getInstance().getpieceSize()];	//Create a buffer of piece size and read data
			}
			else {
				pieceBuffer = new byte[ArgReader.getInstance().getpieceSize()];
			}
			input.read(pieceBuffer);
			input.close();
			System.out.println("Succeed to read piece " + piece);
			return pieceBuffer;
		} catch (Exception e) {
			System.out.println("Error: could not read piece " + piece);
			e.printStackTrace();
			return null;
		}
	}
	
	synchronized public void write(int piece, byte[] data) {
		try {
			RandomAccessFile output = new RandomAccessFile(file, "rw");			//Open RandomAccessFile to write at desired position
			output.seek(piece * ArgReader.getInstance().getpieceSize());						//Seek to desired position
			output.write(data);
			output.close();
			System.out.println("Succeed to write piece " + piece);
		} catch (Exception e) {
			System.out.println("Error: could not write piece " + piece);
		}
	}
	
	public void renameTemp() {
		File completedFile = new File(filePath);				//Since we've been writing to <filePath>.temp rename to <filePath>
		file.renameTo(completedFile);
		file = completedFile;
	}
}
