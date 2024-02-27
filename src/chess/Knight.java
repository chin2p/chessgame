package chess;

import java.util.ArrayList;

public class Knight {

    public static boolean isValidKnightMove(ReturnPiece piece, ReturnPiece.PieceFile fromFile, int fromRank, ReturnPiece.PieceFile toFile, int toRank, ArrayList<ReturnPiece> pieces) {
        // Determine if the piece is a white or black knight
        boolean isWhiteKnight = piece.pieceType == ReturnPiece.PieceType.WN;
        boolean isBlackKnight = piece.pieceType == ReturnPiece.PieceType.BN;
        
        // If the piece is neither a white nor black knight, the move is invalid
        if (!isWhiteKnight && !isBlackKnight) {
            return false;
        }

        // Calculate the file and rank changes to determine if the move is L-shaped
        int fileChange = Math.abs(toFile.ordinal() - fromFile.ordinal());
        int rankChange = Math.abs(toRank - fromRank);

        // Check for L-shaped movement: 2 squares one direction, 1 square the other
        boolean isValidLShape = (fileChange == 2 && rankChange == 1) || (fileChange == 1 && rankChange == 2);

        // Check if the move is to an empty square or captures an opponent's piece
        boolean isMoveLegal = isValidLShape && (!isPieceAt(toFile, toRank, pieces) || isOpponentPieceAt(toFile, toRank, isWhiteKnight, pieces));
        
        return isMoveLegal;
    }

    private static boolean isPieceAt(ReturnPiece.PieceFile file, int rank, ArrayList<ReturnPiece> pieces) {
        for (ReturnPiece piece : pieces) {
            if (piece.pieceFile == file && piece.pieceRank == rank) {
                return true;
            }
        }
        return false;
    }

    private static boolean isOpponentPieceAt(ReturnPiece.PieceFile file, int rank, boolean isWhiteKnight, ArrayList<ReturnPiece> pieces) {
        for (ReturnPiece piece : pieces) {
            if (piece.pieceFile == file && piece.pieceRank == rank) {
                boolean isOpponentPiece = (isWhiteKnight && piece.pieceType.toString().startsWith("B")) || (!isWhiteKnight && piece.pieceType.toString().startsWith("W"));
                return isOpponentPiece;
            }
        }
        return false;
    }
}
