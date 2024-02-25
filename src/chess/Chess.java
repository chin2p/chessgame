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

	//var declarations
	private static ArrayList<ReturnPiece> initPieces = new ArrayList<>();

	private static Player currPlayer = Player.white;
	
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
		if (move.length() == 5) {
			System.out.println("move length 5 (standard move)");
		} else if (move.length() == 6) {
			System.out.println("move length 6 (resign)");
		} else {
			System.out.println("move length 7+ (draw request)");
		}

		/* FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY */
		/* WHEN YOU FILL IN THIS METHOD, YOU NEED TO RETURN A ReturnPlay OBJECT */

		ReturnPlay returnPlay = new ReturnPlay();
        returnPlay.piecesOnBoard = new ArrayList<>(initPieces);
		//default to illegal so we can check the move
        returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;

		String[] parts = move.split(" ");
        if (parts.length != 2){
			return returnPlay;
		} 

		ReturnPiece.PieceFile fromFile = ReturnPiece.PieceFile.valueOf(parts[0].substring(0, 1));
        int fromRank = Integer.parseInt(parts[0].substring(1));
        ReturnPiece.PieceFile toFile = ReturnPiece.PieceFile.valueOf(parts[1].substring(0, 1));
        int toRank = Integer.parseInt(parts[1].substring(1));

		for(int i = 0; i < initPieces.size(); i++) {
			ReturnPiece piece = initPieces.get(i);

			if (piece.pieceFile == fromFile && piece.pieceRank == fromRank) {
				if ((Pawn.isValidPawnMove(piece, fromFile, fromRank, toFile, toRank, initPieces)) || (Rook.isValidRookMove(piece, fromFile, fromRank, toFile, toRank, initPieces)) || (Knight.isValidKnightMove(piece, fromFile, fromRank, toFile, toRank, initPieces))
				 || (Bishop.isValidBishopMove(piece, fromFile, fromRank, toFile, toRank, initPieces)) || (Queen.isValidQueenMove(piece, fromFile, fromRank, toFile, toRank, initPieces)) 
				 || (King.isValidKingMove(piece, fromFile, fromRank, toFile, toRank, initPieces))) {
					piece.pieceFile = toFile;
					piece.pieceRank = toRank;

					//later implement capture mechanics
					//also promotion mechanics here

					switchPlayer();
					break;
				}

			}
		}

		return returnPlay;
	}
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		/* FILL IN THIS METHOD */
		
		initPieces.clear();

        // Initialize pieces here
		initializePieces();

        // Print the board
		PlayChess.printBoard(initPieces);

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

	private static void switchPlayer() {
        currPlayer = (currPlayer == Player.white) ? Player.black : Player.white;
    }

}