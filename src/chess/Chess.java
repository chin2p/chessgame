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

	//castling var
	public static boolean whiteKingMoved = false;
	public static boolean blackKingMoved = false;
	public static boolean whiteRookAMoved = false;
	public static boolean whiteRookHMoved = false;
	public static boolean blackRookAMoved = false;
	public static boolean blackRookHMoved = false;

	
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

		ReturnPlay returnPlay = new ReturnPlay();
        returnPlay.piecesOnBoard = new ArrayList<>(initPieces);
		

		String[] parts = move.split(" ");
        if (parts.length != 2){
			if (parts.length == 1 && parts[0].equals("resign")) {
				if (currPlayer == Player.white) {
					returnPlay.message = ReturnPlay.Message.RESIGN_BLACK_WINS;
					start();
					return returnPlay;
				} else {
					returnPlay.message = ReturnPlay.Message.RESIGN_WHITE_WINS;
					start();
					return returnPlay;
				}
			} else if (parts.length == 1 && parts[0].equals("start")) {
				// need to implement "start" (aka restart) here
				// i dont think i did this right:
				//Chess.initializePieces();
				//returnPlay.piecesOnBoard = new ArrayList<>(initPieces);
				return returnPlay;
			} else if (parts.length >= 4) {
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
		
		// Check for castling attempt before other moves
		if (isCastlingMove(move)) {
			if (canCastle(currPlayer, move)) {
				executeCastling(currPlayer, move);
				switchPlayer();
				// Reset any relevant flags or tracking variables here, if necessary
				lastPawnFile = null;
				lastPawnRank = -1;
				lastMoveWasDoublePawnStep = false;
				returnPlay.message = null; // Optionally, provide a specific message for successful castling
				return returnPlay;
			} else {
				returnPlay.message = ReturnPlay.Message.ILLEGAL_MOVE;
				return returnPlay;
			}
		}

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

						//castling king/rook moved check
						// Example for updating king and rook movement flags
						if (piece.pieceType == ReturnPiece.PieceType.WK) {
							whiteKingMoved = true;
						} else if (piece.pieceType == ReturnPiece.PieceType.BK) {
							blackKingMoved = true;
						} else if (piece.pieceType == ReturnPiece.PieceType.WR && fromRank == 1) {
							if (fromFile == ReturnPiece.PieceFile.a) whiteRookAMoved = true;
							else if (fromFile == ReturnPiece.PieceFile.h) whiteRookHMoved = true;
						} else if (piece.pieceType == ReturnPiece.PieceType.BR && fromRank == 8) {
							if (fromFile == ReturnPiece.PieceFile.a) blackRookAMoved = true;
							else if (fromFile == ReturnPiece.PieceFile.h) blackRookHMoved = true;
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

		if (parts.length == 3 && parts[2].equals("draw?")) {
			returnPlay.message = ReturnPlay.Message.DRAW;
		}

		return returnPlay;
		
	}
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		/* FILL IN THIS METHOD */
		
		initPieces.clear();
		initializePieces();
		currPlayer = Player.white; // Ensure the starting player is set correctly
		lastPawnFile = null;
		lastPawnRank = -1;
		lastMoveWasDoublePawnStep = false;
		// print board
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


	//castling check
	private static boolean canCastle(Player currPlayer, String move) {
		String normalizedMove = move.replaceAll("\\s+", "");
		char kingFile = normalizedMove.charAt(0);
		char rookFile = normalizedMove.charAt(3);
		int rank = currPlayer == Player.white ? 1 : 8; // Assuming 1 for white and 8 for black

		char kingPos = 'e';

		// Check if the king's current position is under attack
		if (isSquareUnderAttack(currPlayer, ReturnPiece.PieceFile.valueOf(String.valueOf(kingPos)), rank)) {
			return false; // Cannot castle if the king is in check
		}
	
		// In canCastle method, add checks for king and rook movement
		if (currPlayer == Player.white) {
			if (kingFile == 'e' && rookFile == 'a') {
				return (!whiteKingMoved && !whiteRookAMoved && isPathClear(kingFile, rookFile, rank));
			} else if (kingFile == 'e' && rookFile == 'h') {
				return (!whiteKingMoved && !whiteRookHMoved && isPathClear(kingFile, rookFile, rank));
			}
		} else { // Assuming black player
			if (kingFile == 'e' && rookFile == 'a') {
				return (!blackKingMoved && !blackRookAMoved && isPathClear(kingFile, rookFile, rank));
			} else if (kingFile == 'e' && rookFile == 'h') {
				return (!blackKingMoved && !blackRookHMoved && isPathClear(kingFile, rookFile, rank));
			}
		}
		// In canCastle method, add checks for threats to the king's path
		if (currPlayer == Player.white) {
			if (kingFile == 'e' && (rookFile == 'a' || rookFile == 'h')) {
				// Check squares king moves through and ends on for white
				if (isSquareUnderAttack(Player.white, ReturnPiece.PieceFile.e, 1) || 
					isSquareUnderAttack(Player.white, ReturnPiece.PieceFile.d, 1) || 
					isSquareUnderAttack(Player.white, ReturnPiece.PieceFile.c, 1) || // Only for queen side
					isSquareUnderAttack(Player.white, ReturnPiece.PieceFile.g, 1)) { // Only for king side
					return false;
				}
			}
		} else {
			if (kingFile == 'e' && (rookFile == 'a' || rookFile == 'h')) {
				// Check squares king moves through and ends on for black
				if (isSquareUnderAttack(Player.black, ReturnPiece.PieceFile.e, 8) || 
					isSquareUnderAttack(Player.black, ReturnPiece.PieceFile.d, 8) || 
					isSquareUnderAttack(Player.black, ReturnPiece.PieceFile.c, 8) || // Only for queen side
					isSquareUnderAttack(Player.black, ReturnPiece.PieceFile.g, 8)) { // Only for king side
					return false;
				}
			}
		}
		//no conditions are met
		return false;
	}
	
	//Castling helper functions
	// Helper method to check if the path between the king and rook is clear for castling
	private static boolean isPathClear(char fromFile, char toFile, int rank) {
		int startFile = Math.min(fromFile, toFile) + 1; // Start checking from the next file after the king
		int endFile = Math.max(fromFile, toFile); // Stop checking at the rook's file

		for (int file = startFile; file < endFile; file++) {
			if (findPieceAtPosition(Character.toString((char)file) + rank) != null) {
				return false; // Found a piece in the way
			}
		}
		return true; // No pieces between the king and rook
	}

	// Helper method to find a piece at a given position
	private static ReturnPiece findPieceAtPosition(String position) {
		for (ReturnPiece piece : initPieces) {
			if ((piece.pieceFile.name() + piece.pieceRank).equalsIgnoreCase(position)) {
				return piece; // Return the piece at the given position
			}
		}
		return null; // No piece found at the given position
	}

	// check square under attack
	private static boolean isSquareUnderAttack(Player player, ReturnPiece.PieceFile file, int rank) {
		for (ReturnPiece piece : initPieces) {
			// Skip the current player's pieces
			if ((player == Player.white && piece.pieceType.name().startsWith("W")) ||
				(player == Player.black && piece.pieceType.name().startsWith("B"))) {
				continue;
			}
			
			// For each type of piece, check if it can move to the target square
			if (piece.pieceType == ReturnPiece.PieceType.BN || piece.pieceType == ReturnPiece.PieceType.WN) {
				if (Knight.isValidKnightMove(piece, piece.pieceFile, piece.pieceRank, file, rank, initPieces)) {
					return true;
				}
			}
			if (piece.pieceType == ReturnPiece.PieceType.BP || piece.pieceType == ReturnPiece.PieceType.WP) {
				if (Pawn.isValidPawnMove(piece, piece.pieceFile, piece.pieceRank, file, rank, initPieces)) {
					return true;
				}
			}
			if (piece.pieceType == ReturnPiece.PieceType.BB || piece.pieceType == ReturnPiece.PieceType.WB) {
				if (Bishop.isValidBishopMove(piece, piece.pieceFile, piece.pieceRank, file, rank, initPieces)) {
					return true; // Square is under attack by an opponent's knight
				}
			}
			if (piece.pieceType == ReturnPiece.PieceType.BR || piece.pieceType == ReturnPiece.PieceType.WR) {
				if (Rook.isValidRookMove(piece, piece.pieceFile, piece.pieceRank, file, rank, initPieces)) {
					return true; // Square is under attack by an opponent's knight
				}
			}
			if (piece.pieceType == ReturnPiece.PieceType.BQ || piece.pieceType == ReturnPiece.PieceType.WQ) {
				if (Queen.isValidQueenMove(piece, piece.pieceFile, piece.pieceRank, file, rank, initPieces)) {
					return true; // Square is under attack by an opponent's knight
				}
			}
			if (piece.pieceType == ReturnPiece.PieceType.BK || piece.pieceType == ReturnPiece.PieceType.WK) {
				if (King.isValidKingMove(piece, piece.pieceFile, piece.pieceRank, file, rank, initPieces)) {
					return true; // Square is under attack by an opponent's knight
				}
			}
		}
		return false; // No opponent's piece can attack the square
	}
	
	private static boolean isCastlingMove(String move) {
		return move.matches("e1 g1|e1 c1|e8 g8|e8 c8");
	}

	//executing castling
	private static void executeCastling(Player currPlayer, String move) {
		// Determine the castling type based on the move's ending file for the king
		boolean kingSide = move.endsWith("g1") || move.endsWith("g8");
	
		int rank = currPlayer == Player.white ? 1 : 8;
		ReturnPiece.PieceFile kingNewFile = kingSide ? ReturnPiece.PieceFile.g : ReturnPiece.PieceFile.c;
		ReturnPiece.PieceFile rookNewFile = kingSide ? ReturnPiece.PieceFile.f : ReturnPiece.PieceFile.d;
		ReturnPiece.PieceFile kingOriginalFile = ReturnPiece.PieceFile.e;
		ReturnPiece.PieceFile rookOriginalFile = kingSide ? ReturnPiece.PieceFile.h : ReturnPiece.PieceFile.a;
	
		// Move the king to its new file
		for (ReturnPiece piece : initPieces) {
			if (piece.pieceRank == rank && piece.pieceFile == kingOriginalFile &&
				((currPlayer == Player.white && piece.pieceType == ReturnPiece.PieceType.WK) ||
				 (currPlayer == Player.black && piece.pieceType == ReturnPiece.PieceType.BK))) {
				piece.pieceFile = kingNewFile;
				break;
			}
		}
	
		// Move the rook to its new file
		for (ReturnPiece piece : initPieces) {
			if (piece.pieceRank == rank && piece.pieceFile == rookOriginalFile &&
				((currPlayer == Player.white && piece.pieceType == ReturnPiece.PieceType.WR) ||
				 (currPlayer == Player.black && piece.pieceType == ReturnPiece.PieceType.BR))) {
				piece.pieceFile = rookNewFile;
				break;
			}
		}
	}


}