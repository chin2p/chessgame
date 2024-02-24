package chess;

import java.util.ArrayList;

public class Bishop {

    public static boolean isValidBishopMove(ReturnPiece piece, ReturnPiece.PieceFile fromFile, int fromRank, ReturnPiece.PieceFile toFile, int toRank, ArrayList<ReturnPiece> pieces) {
        // Ensure the piece is a bishop
        boolean isBishop = piece.pieceType == ReturnPiece.PieceType.WB || piece.pieceType == ReturnPiece.PieceType.BB;
        if (!isBishop) {
            return false;
        }

        // Calculate the file and rank changes to determine if the move is diagonal
        int fileChange = Math.abs(toFile.ordinal() - fromFile.ordinal());
        int rankChange = Math.abs(toRank - fromRank);

        // Check for diagonal movement
        if (fileChange != rankChange) {
            return false;
        }

        // Check path for blocking pieces
        int fileStep = toFile.ordinal() > fromFile.ordinal() ? 1 : -1;
        int rankStep = toRank > fromRank ? 1 : -1;
        for (int step = 1; step < fileChange; step++) {
            ReturnPiece.PieceFile intermediateFile = ReturnPiece.PieceFile.values()[fromFile.ordinal() + step * fileStep];
            int intermediateRank = fromRank + step * rankStep;
            if (isPieceAt(intermediateFile, intermediateRank, pieces)) {
                return false; // Path is blocked
            }
        }

        // Ensure destination is either empty or contains an opponent's piece
        return !isPieceAt(toFile, toRank, pieces) || isOpponentPieceAt(toFile, toRank, piece.pieceType.toString().startsWith("W"), pieces);
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
