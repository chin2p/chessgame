package chess;

import java.util.ArrayList;

public class Rook {

    public static boolean isValidRookMove(ReturnPiece piece, ReturnPiece.PieceFile fromFile, int fromRank, ReturnPiece.PieceFile toFile, int toRank, ArrayList<ReturnPiece> pieces) {
        boolean isRook = piece.pieceType == ReturnPiece.PieceType.WR || piece.pieceType == ReturnPiece.PieceType.BR;
        if (!isRook) {
            return false;
        }

        //know whether piece moves vertically or horizontally
		int rankChange = toRank - fromRank;
		int fileChange = toFile.ordinal() - fromFile.ordinal();
		
		if (rankChange != 0 && fileChange == 0) {
			//if piece moves vertically
			int step;
            if (rankChange > 0) {
                step = 1; //moving upwards
            } else {
                step = -1; //moving downwards
            }
            for (int i = 1; i < Math.abs(rankChange); i++) {
                if (isPieceAt(fromFile, fromRank + i * step, pieces)) {
                    return false; // path blocked
                }
            }
		} else if (rankChange == 0 && fileChange != 0) {
			//if piece moves horizontally
            int step;
            if (fileChange > 0) {
                step = 1; // moving right
            } else {
                step = -1; // moving left
            }
            for (int i = 1; i < Math.abs(fileChange); i++) {
                if (isPieceAt(ReturnPiece.PieceFile.values()[fromFile.ordinal() + i * step], fromRank, pieces)) {
                    return false; // path blocked
                }
            }
        } else {
            //if none then invalid move
		    return false;
        }
        
        // destination either empty or contain opponent piece
        boolean isWhite = piece.pieceType == ReturnPiece.PieceType.WK;
        return !isPieceAt(toFile, toRank, pieces) || isOpponentPieceAt(toFile, toRank, isWhite, pieces);
		
		
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
