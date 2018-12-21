package model;

import static model.Step.*;

public enum Orientation {
	DIAGONAL_NE_WS(NE, SW), DIAGONAL_NW_SE(NW, SE), HORIZONTAL(E, W);
	private Step[] possibleMoves;

	Orientation(Step move1, Step move2) {
		this.possibleMoves = new Step[2];
		this.possibleMoves[0] = move1;
		this.possibleMoves[1] = move2;
	}

	public static Orientation getOrientation(int dx, int dy) {
		if (dy == 0)
			return HORIZONTAL;
		else if (dx == dy)
			return DIAGONAL_NW_SE;
		else
			return DIAGONAL_NE_WS;
	}

	public Step[] movesAlongLine() {
		return this.possibleMoves;
	}
};