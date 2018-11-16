package custom;

public class Config {
	public static enum MessageType {
		CHOKE, //0
		UNCHOKE, //1
		INTERESTED, //2
		NOT_INTERESTED, //3
		HAVE, //4
		BITFIELD, //5
		REQUEST, //6
		PIECE, //7
		HANDSHAKE //8
	};
	public static enum LogType {
		TCPConnection, //0
		ChangeOfPreferredNeighbor, //1
		ChangeOfOptUnchokedNeighbor, //2
		Unchoking, //3
		Choking, //4
		ReceivingHaveMessage, //5
		ReceivingInterestedMessage, //6
		ReceivingNotInterestedMessage, //7
		DownloadingAPiece, //8
		CompletionOfDownload, //9
	}
}
