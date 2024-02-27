package chess;

import java.util.ArrayList;

public class King {

    public static boolean isValidKingMove(ReturnPiece piece, ReturnPiece.PieceFile fromFile, int fromRank, ReturnPiece.PieceFile toFile, int toRank, ArrayList<ReturnPiece> pieces) {
        // Ensure the piece is a king
        boolean isKing = piece.pieceType == ReturnPiece.PieceType.WK || piece.pieceType == ReturnPiece.PieceType.BK;
        if (!isKing) {
            return false;
        }

        // Calculate the file and rank changes to determine the move's range
        int fileChange = Math.abs(toFile.ordinal() - fromFile.ordinal());
        int rankChange = Math.abs(toRank - fromRank);

        // Check if the move is within one square in any direction
        if (fileChange > 1 || rankChange > 1) {
            return false;
        }

        // Ensure the destination is either empty or contains an opponent's piece
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
                boolean isOpponentPiece = (isWhite && piece.pieceType.toString().startsWith("B")) || (!isWhite && piece.pieceType.toString().startsWith("W"));
                // Debug output to check if an opponent piece is detected correctly
                //System.out.println("Checking opponent piece at " + file + rank + ": " + isOpponentPiece);
                return isOpponentPiece;
            }
        }
        return false;
    }
}
