package chess;

import java.util.ArrayList;

public class Pawn {

    public static boolean isValidPawnMove(ReturnPiece piece, ReturnPiece.PieceFile fromFile, int fromRank, ReturnPiece.PieceFile toFile, int toRank, ArrayList<ReturnPiece> pieces) {
        // Check if the move is forward for white or black pawns
        int rankChange = toRank - fromRank;
        boolean isWhite = piece.pieceType == ReturnPiece.PieceType.WP;
        boolean isBlack = piece.pieceType == ReturnPiece.PieceType.BP;
        
        //calculate file change to check for diagonal captures
        int fileChange = toFile.ordinal() - fromFile.ordinal();
        
        if (isWhite) {
            if (rankChange == 1 && fileChange == 0 && !isPieceAt(toFile, toRank, pieces)) {
                return true;
            } else if (fromRank == 2 && rankChange == 2 && fileChange == 0 && !isPieceAt(toFile, toRank, pieces) && !isPieceAt(toFile, 3, pieces)) {
                return true;
            } else if (rankChange == 1 && Math.abs(fileChange) == 1 && isOpponentPieceAt(toFile, toRank, isWhite, pieces)) {
                return true;
            }
        } else if (isBlack) {
            if (rankChange == -1 && fileChange == 0 && !isPieceAt(toFile, toRank, pieces)) {
                return true;
            } else if (fromRank == 7 && rankChange == -2 && fileChange == 0 && !isPieceAt(toFile, toRank, pieces) && !isPieceAt(toFile, 6, pieces)) {
                return true;
            } else if (rankChange == -1 && Math.abs(fileChange) == 1 && isOpponentPieceAt(toFile, toRank, isWhite, pieces)) {
                return true;
            }
        }
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
