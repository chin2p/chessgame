package chess;

import java.util.ArrayList;

public class Rook {
    private static boolean isValidRookMove(ReturnPiece piece, ReturnPiece.PieceFile fromFile, int fromRank, ReturnPiece.PieceFile toFile, int toRank, ArrayList<ReturnPiece> pieces) {
        boolean isRook = piece.pieceType == ReturnPiece.PieceType.WR || piece.pieceType == ReturnPiece.PieceType.BR;
        if (!isRook) {
            return false;
        }

        //know whether piece moves vertically or horizontally
		int rankChange = toRank - fromRank;
		int fileChange = toFile.ordinal() - fromFile.ordinal();
		
		if (rankChange != 0 && fileChange == 0) {
			//if piece moves vertically
			if (rankChange < 0) { //piece moves down
				for (int i = 1; i <= rankChange; i++) {
					if (isOpponentPieceAt(toFile, toRank - i, piece.pieceType.toString().startsWith("W"), pieces)) {
						return false;
					}
                    
				}
				return true;
			} else if (rankChange > 0) { //piece moves up
				for (int i = 1; i <= rankChange; i++) {
					if (isOpponentPieceAt(toFile, toRank - i, piece.pieceType.toString().startsWith("W"), pieces)) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		} else if (rankChange == 0 && fileChange != 0) {
			//if piece moves horizontally
			// still working on this
		} else if (rankChange !=0 && fileChange != 0) {
            //if piece moves, but not like a rook
			return false;
		} else if (rankChange == 0 && fileChange == 0) {
            //if piece doesn't move
            return false;
        }
		
		//if none then invalid move
		return false;
	}

    private static boolean isPieceAt(ReturnPiece.PieceFile file, int rank, ArrayList<ReturnPiece> pieces) {
        for (ReturnPiece piece : pieces) {
            if (piece.pieceFile == file && piece.pieceRank == rank) {
                return true;
            }
        }
        return false;
    }

    private static boolean isOpponentPieceAt(ReturnPiece.PieceFile file, int rank, boolean isWhite, ArrayList<ReturnPiece> pieces) {
        for (ReturnPiece piece : pieces) {
            if (piece.pieceFile == file && piece.pieceRank == rank) {
                return (isWhite && piece.pieceType.toString().startsWith("B")) || (!isWhite && piece.pieceType.toString().startsWith("W"));
            }
        }
        return false;
    }
}
