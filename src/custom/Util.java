package custom;

import model.PeerInfo;

public class Util {

	public Util() {
		// TODO Auto-generated constructor stub
	}

	public static byte[] IntToByte(int num){
		byte[]bytes=new byte[4];
		bytes[0]=(byte) ((num>>24)&0xff);
		bytes[1]=(byte) ((num>>16)&0xff);
		bytes[2]=(byte) ((num>>8)&0xff);
		bytes[3]=(byte) (num&0xff);
		return bytes;
	}

	public static int Byte2Int(byte[] bytes) {
		return (bytes[0]&0xff)<<24
			| (bytes[1]&0xff)<<16
			| (bytes[2]&0xff)<<8
			| (bytes[3]&0xff);
	}
	// if a boolean array is all true
	public static boolean allTrue (boolean[] values) {
	    for (boolean value : values) {
	        if (!value)
	            return false;
	    }
	    return true;
	}
	//byte2boolean
	
	//boolean2byte
	
	public static void writeLog(PeerInfo peerinfo, String logContent){
		
	}
}
