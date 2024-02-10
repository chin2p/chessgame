/*
 * Names: Chirantan Patel (csp126), Catherine Zhou (czz2)
 */
package chess;

import java.util.ArrayList;

class ReturnPiece {
	static enum PieceType {WP, WR, WN, WB, WQ, WK, 
		            BP, BR, BN, BB, BK, BQ};
	static enum PieceFile {a, b, c, d, e, f, g, h};
	
	PieceType pieceType;
	PieceFile pieceFile;
	int pieceRank;  // 1..8
	public String toString() {
		return ""+pieceFile+pieceRank+":"+pieceType;
	}
	public boolean equals(Object other) {
		if (other == null || !(other instanceof ReturnPiece)) {
			return false;
		}
		ReturnPiece otherPiece = (ReturnPiece)other;
		return pieceType == otherPiece.pieceType &&
				pieceFile == otherPiece.pieceFile &&
				pieceRank == otherPiece.pieceRank;
	}
}

class ReturnPlay {
	enum Message {ILLEGAL_MOVE, DRAW, 
				  RESIGN_BLACK_WINS, RESIGN_WHITE_WINS, 
				  CHECK, CHECKMATE_BLACK_WINS,	CHECKMATE_WHITE_WINS, 
				  STALEMATE};
	
	ArrayList<ReturnPiece> piecesOnBoard;
	Message message;
}

public class Chess {
	
	enum Player { white, black }

	private static ArrayList<ReturnPiece> initPieces = new ArrayList<>();
	
	
	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for details of
	 *         the contents of the returned ReturnPlay instance.
	 */
	public static ReturnPlay play(String move) {

		/* FILL IN THIS METHOD */
		
		/* FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY */
		/* WHEN YOU FILL IN THIS METHOD, YOU NEED TO RETURN A ReturnPlay OBJECT */
		return null;
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		/* FILL IN THIS METHOD */
		
		initPieces.clear();
        
        // Initialize pieces here
		initializePieces();

	}

	/*
	 * Helper functions
	 */
	//Setup board and initialize all pieces
	private static void initializePieces() {
        // Initialize White pieces
        for (ReturnPiece.PieceFile file : ReturnPiece.PieceFile.values()) {
			//Initialize pawns first
            initPieces.add(createPiece(ReturnPiece.PieceType.WP, file, 2));
        }
        initPieces.add(createPiece(ReturnPiece.PieceType.WR, ReturnPiece.PieceFile.a, 1));
        initPieces.add(createPiece(ReturnPiece.PieceType.WR, ReturnPiece.PieceFile.h, 1));
        initPieces.add(createPiece(ReturnPiece.PieceType.WN, ReturnPiece.PieceFile.b, 1));
        initPieces.add(createPiece(ReturnPiece.PieceType.WN, ReturnPiece.PieceFile.g, 1));
        initPieces.add(createPiece(ReturnPiece.PieceType.WB, ReturnPiece.PieceFile.c, 1));
        initPieces.add(createPiece(ReturnPiece.PieceType.WB, ReturnPiece.PieceFile.f, 1));
        initPieces.add(createPiece(ReturnPiece.PieceType.WQ, ReturnPiece.PieceFile.d, 1));
        initPieces.add(createPiece(ReturnPiece.PieceType.WK, ReturnPiece.PieceFile.e, 1));
        
        // Initialize Black pieces
        for (ReturnPiece.PieceFile file : ReturnPiece.PieceFile.values()) {
            initPieces.add(createPiece(ReturnPiece.PieceType.BP, file, 7));
        }
        initPieces.add(createPiece(ReturnPiece.PieceType.BR, ReturnPiece.PieceFile.a, 8));
        initPieces.add(createPiece(ReturnPiece.PieceType.BR, ReturnPiece.PieceFile.h, 8));
        initPieces.add(createPiece(ReturnPiece.PieceType.BN, ReturnPiece.PieceFile.b, 8));
        initPieces.add(createPiece(ReturnPiece.PieceType.BN, ReturnPiece.PieceFile.g, 8));
        initPieces.add(createPiece(ReturnPiece.PieceType.BB, ReturnPiece.PieceFile.c, 8));
        initPieces.add(createPiece(ReturnPiece.PieceType.BB, ReturnPiece.PieceFile.f, 8));
        initPieces.add(createPiece(ReturnPiece.PieceType.BQ, ReturnPiece.PieceFile.d, 8));
        initPieces.add(createPiece(ReturnPiece.PieceType.BK, ReturnPiece.PieceFile.e, 8));
    }
	
    private static ReturnPiece createPiece(ReturnPiece.PieceType type, ReturnPiece.PieceFile file, int rank) {
        ReturnPiece piece = new ReturnPiece();
        piece.pieceType = type;
        piece.pieceFile = file;
        piece.pieceRank = rank;
        return piece;
    }


}