package chess;

import java.util.ArrayList;

public class Queen {

    public static boolean isValidQueenMove(ReturnPiece piece, ReturnPiece.PieceFile fromFile, int fromRank, ReturnPiece.PieceFile toFile, int toRank, ArrayList<ReturnPiece> pieces) {
        // Ensure the piece is a queen
        boolean isQueen = piece.pieceType == ReturnPiece.PieceType.WQ || piece.pieceType == ReturnPiece.PieceType.BQ;
        if (!isQueen) {
            return false;
        }

        // Calculate the file and rank changes to determine the move's direction
        int fileChange = Math.abs(toFile.ordinal() - fromFile.ordinal());
        int rankChange = Math.abs(toRank - fromRank);

        // Determine if the move is straight or diagonal
        boolean isStraight = fileChange == 0 || rankChange == 0;
        boolean isDiagonal = fileChange == rankChange;

        if (!isStraight && !isDiagonal) {
            return false;
        }

        // Calculate step direction for file and rank
        int fileStep = Integer.compare(toFile.ordinal(), fromFile.ordinal());
        int rankStep = Integer.compare(toRank, fromRank);

        // Check path for blocking pieces
        int steps = isStraight ? Math.max(fileChange, rankChange) : fileChange; // For diagonal, fileChange == rankChange
        for (int step = 1; step < steps; step++) {
            ReturnPiece.PieceFile intermediateFile = ReturnPiece.PieceFile.values()[fromFile.ordinal() + step * fileStep];
            int intermediateRank = fromRank + step * rankStep;
            if (isPieceAt(intermediateFile, intermediateRank, pieces)) {
                return false; // Path is blocked
            }
        }

        // Ensure destination is either empty or contains an opponent's piece
        boolean isWhite = piece.pieceType == ReturnPiece.PieceType.WQ;
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
