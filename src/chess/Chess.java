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

	// Fields to track the last pawn move for en passant
    public static ReturnPiece.PieceFile lastPawnFile = null;
    public static int lastPawnRank = -1;
    public static boolean lastMoveWasDoublePawnStep = false;

	
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

		// if (move.length() == 5) {
		// 	System.out.println("move length 5 (standard move)");
		// } else if (move.length() == 6) {
		// 	System.out.println("move length 6 (resign)");
		// } else {
		// 	System.out.println("move length 7+ (draw request)");
		// }

		ReturnPlay returnPlay = new ReturnPlay();
        returnPlay.piecesOnBoard = new ArrayList<>(initPieces);
		

		String[] parts = move.split(" ");
        if (parts.length != 2){
			if (parts.length == 1 && parts[0].equals("resign")) {
				if (currPlayer == Player.white) {
					returnPlay.message = ReturnPlay.Message.RESIGN_BLACK_WINS;
					return returnPlay;
				} else {
					returnPlay.message = ReturnPlay.Message.RESIGN_WHITE_WINS;
					return returnPlay;
				}
			} else {
				//illegal move
				returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
				return returnPlay;
			}
		} else {

			ReturnPiece.PieceFile fromFile = ReturnPiece.PieceFile.valueOf(parts[0].substring(0, 1));
			int fromRank = Integer.parseInt(parts[0].substring(1));
			ReturnPiece.PieceFile toFile = ReturnPiece.PieceFile.valueOf(parts[1].substring(0, 1));
			int toRank = Integer.parseInt(parts[1].substring(1));

			//illegal move var
			boolean legalMove = false;

			for(int i = 0; i < initPieces.size(); i++) {
				ReturnPiece piece = initPieces.get(i);

				if ((currPlayer == Player.white && piece.pieceType.name().startsWith("W")) ||
					(currPlayer == Player.black && piece.pieceType.name().startsWith("B"))) {
					if (piece.pieceFile == fromFile && piece.pieceRank == fromRank) {
						if ((Pawn.isValidPawnMove(piece, fromFile, fromRank, toFile, toRank, initPieces)) || (Rook.isValidRookMove(piece, fromFile, fromRank, toFile, toRank, initPieces)) || (Knight.isValidKnightMove(piece, fromFile, fromRank, toFile, toRank, initPieces))
							|| (Bishop.isValidBishopMove(piece, fromFile, fromRank, toFile, toRank, initPieces)) || (Queen.isValidQueenMove(piece, fromFile, fromRank, toFile, toRank, initPieces)) 
							|| (King.isValidKingMove(piece, fromFile, fromRank, toFile, toRank, initPieces))) {
							//legal move
							legalMove = true;

							// En Passant capture check
							if (Math.abs(fromFile.ordinal() - toFile.ordinal()) == 1 && 
								((piece.pieceType == ReturnPiece.PieceType.WP && toRank == 6) || 
								(piece.pieceType == ReturnPiece.PieceType.BP && toRank == 3))) {
								// Check if last move was a double pawn step and the move is en passant
								if (lastMoveWasDoublePawnStep && lastPawnFile == toFile && 
									((piece.pieceType == ReturnPiece.PieceType.WP && lastPawnRank == 5) || 
									(piece.pieceType == ReturnPiece.PieceType.BP && lastPawnRank == 4))) {
									int capturedPawnIndex = findPieceIndexAtPosition(initPieces, toFile, lastPawnRank);
									if (capturedPawnIndex != -1) {
										initPieces.remove(capturedPawnIndex);
									}
								}
							}

							// Check and handle regular captures
							for (int j = 0; j < initPieces.size(); j++) {
								ReturnPiece targetPiece = initPieces.get(j);
								if (targetPiece.pieceFile == toFile && targetPiece.pieceRank == toRank && !targetPiece.equals(piece)) {
									// Piece is captured
									initPieces.remove(j);
									break;
								}
							}

							// move the piece
							piece.pieceFile = toFile;
							piece.pieceRank = toRank;

							// Update lastMoveWasDoublePawnStep for the next move
							if (piece.pieceType == ReturnPiece.PieceType.WP && fromRank == 2 && toRank == 4) {
								lastPawnFile = fromFile;
								lastPawnRank = toRank;
								lastMoveWasDoublePawnStep = true;
							} else if (piece.pieceType == ReturnPiece.PieceType.BP && fromRank == 7 && toRank == 5) {
								lastPawnFile = fromFile;
								lastPawnRank = toRank;
								lastMoveWasDoublePawnStep = true;
							} else {
								lastMoveWasDoublePawnStep = false;
							}

							
							//also promotion mechanics here
							if ((piece.pieceType == ReturnPiece.PieceType.BP || piece.pieceType == ReturnPiece.PieceType.WP) && (toRank == 8 || toRank == 1)) {
								if (move.length() < 7 || move.substring(6, 1) == "Q") {
									//promote to queen
									if (piece.pieceType == ReturnPiece.PieceType.BP) {
										piece.pieceType = ReturnPiece.PieceType.BQ;
									} else if (piece.pieceType == ReturnPiece.PieceType.WP) {
										piece.pieceType = ReturnPiece.PieceType.WQ;
									}
								} else if (move.substring(6, 1) == "N") {
									//promote to knight
									if (piece.pieceType == ReturnPiece.PieceType.BP) {
										piece.pieceType = ReturnPiece.PieceType.BN;
									} else if (piece.pieceType == ReturnPiece.PieceType.WP) {
										piece.pieceType = ReturnPiece.PieceType.WN;
									}
								} else if (move.substring(6, 1) == "B") {
									//promote to bishop
									if (piece.pieceType == ReturnPiece.PieceType.BP) {
										piece.pieceType = ReturnPiece.PieceType.BB;
									} else if (piece.pieceType == ReturnPiece.PieceType.WP) {
										piece.pieceType = ReturnPiece.PieceType.WB;
									}
								} else if (move.substring(6, 1) == "R") {
									//promote to rook
									if (piece.pieceType == ReturnPiece.PieceType.BP) {
										piece.pieceType = ReturnPiece.PieceType.BR;
									} else if (piece.pieceType == ReturnPiece.PieceType.WP) {
										piece.pieceType = ReturnPiece.PieceType.WR;
									}
								}
								//else, it's an illegal move
							}

							switchPlayer();

							returnPlay.message = null;
							break;
						}

					}
				} else {
					// If the piece does not belong to the current player, it's an illegal move
					returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
					return returnPlay;
				}
			}
		

			// If no legal move is found, set the message to illegal move
			if (!legalMove) {
				returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
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

	//for en passant capture
	private static int findPieceIndexAtPosition(ArrayList<ReturnPiece> pieces, ReturnPiece.PieceFile file, int rank) {
		for (int i = 0; i < pieces.size(); i++) {
			ReturnPiece piece = pieces.get(i);
			if (piece.pieceFile == file && piece.pieceRank == rank) {
				return i;
			}
		}
		return -1;
	}
	

}