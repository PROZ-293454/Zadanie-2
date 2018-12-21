package model;

import view.Color;
import model.State;

import java.util.ArrayList;
import java.util.List;

import static view.Color.*;
import static model.State.*;
import static model.Step.*;
import static model.Orientation.*;

public class BoardModel {

	ListOfBalls myBalls;
	ListOfBalls opponentBalls;
	ListOfBalls myBallsToMove;
	ListOfBalls opponentBallsToMove;

	private Orientation ballsToMoveOrientation;
	public static final Color MY_COLOR = WHITE; // player 1 - BLACK, player 2 - WHITE
	public static final Step[] tableOfMoves = { E, SE, SW, W, NW, NE };

	private State state;
	private int myCounter;
	private int opponentCounter;

	public BoardModel() {
		this.myBalls = new ListOfBalls();
		this.opponentBalls = new ListOfBalls();
		this.myBallsToMove = new ListOfBalls();
		this.ballsToMoveOrientation = HORIZONTAL;
		this.opponentBallsToMove = new ListOfBalls();
		this.state = MY_COLOR == WHITE ? CHOOSE_FIRST_BALL : null;
		this.myCounter = 0;
		this.opponentCounter = 0;
		setBoardModel();
	}

	private ListOfBalls getList(Color color) {
		if (color == MY_COLOR)
			return myBalls;
		else
			return opponentBalls;
	}

	private boolean moveBall(Ball ball, Step move) {
		boolean isOnBoard = ball.move(move.dx(), move.dy());

		if (isOnBoard)
			return true;
		else {
			getList(ball.getColor()).remove(ball);
			return false;
		}
	}

	public boolean chooseFirstBall(int x, int y) {
		Ball ball = myBalls.getBall(x, y);
		myBallsToMove.add(ball);

		state = CHOOSE_SECOND_BALL;
		return true;
	}

	public Step chooseBallLine(int x2, int y2) {
		int x1 = myBallsToMove.get(0).getX();
		int y1 = myBallsToMove.get(0).getY();
		int dx2_1 = x2 - x1;
		int dy2_1 = y2 - y1;
		if (dx2_1 == 0 && dy2_1 == 0) {
			state = CHOOSE_MOVE;
			ballsToMoveOrientation = HORIZONTAL;
			return E;
		}
		Step step = Step.getStep(dx2_1, dy2_1);
		if (step != null) {
			int dx = step.dx();
			int dy = step.dy();
			int lineLength = (Math.abs(dx2_1) + Math.abs(dy2_1)) / 2;

			for (int i = 1; i <= lineLength; i++) {
				Ball ball = myBalls.getBall(x1 + dx * i, y1 + dy * i);
				myBallsToMove.add(ball);
			}
			ballsToMoveOrientation = Orientation.getOrientation(dx, dy);
			state = CHOOSE_MOVE;
			return step;
		} else
			return null;
	}

	public void moveBallLine(Step move) {
		for (Ball ball : myBallsToMove) {
			boolean isMoved = moveBall(ball, move);
			if (!isMoved)
				if (ball.getColor() == MY_COLOR)
					opponentCounter++;
				else
					myCounter++;
		}
		for (Ball ball : opponentBallsToMove) {
			boolean isMoved = moveBall(ball, move);
			if (!isMoved)
				if (ball.getColor() == MY_COLOR)
					opponentCounter++;
				else
					myCounter++;
		}
	}

	public List<Step> createListOfMoves() {

		List<Step> possibleMoves = new ArrayList<>();
		int[] freeFields = makeFreeFieldsTable();
		int lineLength = myBallsToMove.size();

		for (Step move : tableOfMoves) {
			if (freeFields[move.id()] == lineLength) {
				possibleMoves.add(move);
			}
		}

		Step[] movesAlongLine = ballsToMoveOrientation.movesAlongLine();
		for (Step move : movesAlongLine) {
			boolean isPossible = isMoveAlongLinePossible(move);
			if (isPossible) {
				possibleMoves.add(move);

			}
		}

		state = CHOOSE_MOVE;
		return possibleMoves;
	}

	public void endGame() {
		state = GAME_ENDED;
	}

	public void startTurn() {
		state = CHOOSE_FIRST_BALL;
	}

	private int[] makeFreeFieldsTable() {
		int[] movesTable = { 0, 0, 0, 0, 0, 0 };

		for (Ball ball : myBallsToMove) {
			int x = ball.getX();
			int y = ball.getY();
			for (Step move : tableOfMoves) {
				int dx = move.dx();
				int dy = move.dy();
				if (myBalls.getBall(x + dx, y + dy) == null && opponentBalls.getBall(x + dx, y + dy) == null)
					movesTable[move.id()]++;
			}
		}
		System.out.println(
				"" + movesTable[0] + movesTable[1] + movesTable[2] + movesTable[3] + movesTable[4] + movesTable[5]);
		return movesTable;
	}

	private boolean isMoveAlongLinePossible(Step move) {
		int lineLength = myBallsToMove.size();
		Ball lastBall = myBallsToMove.get(lineLength - 1);
		int x2 = lastBall.getX();
		int y2 = lastBall.getY();
		int dx = move.dx();
		int dy = move.dy();

		for (int i = 1; i <= lineLength; i++) {
			Ball ball = myBalls.getBall(x2 + i * dx, y2 + i * dy);
			if (ball == null) {
				Ball tmpOpponentBall = opponentBalls.getBall(x2 + i * dx, y2 + i * dy);
				if (tmpOpponentBall == null) {
					return true;
				} else {
					opponentBallsToMove.add(tmpOpponentBall);
					if (i == lineLength) {
						opponentBallsToMove.clear();
						return false;
					}
				}
			} else {
				opponentBallsToMove.clear();
				return false;
			}
		}
		return false;

	}

	private void setBoardModel() {

		ListOfBalls ballsList1 = new ListOfBalls();
		ListOfBalls ballsList2 = new ListOfBalls();
		if (MY_COLOR == BLACK) {
			myBalls = ballsList1;
			opponentBalls = ballsList2;
		} else {
			myBalls = ballsList2;
			opponentBalls = ballsList1;
		}
		for (int i = 6; i <= 14; i += 2) {
			Ball ball = new Ball(BLACK, i, 1);
			ballsList1.add(ball);
		}
		for (int i = 5; i <= 15; i += 2) {
			Ball ball = new Ball(BLACK, i, 2);
			ballsList1.add(ball);
		}
		for (int i = 8; i <= 12; i += 2) {
			Ball ball = new Ball(BLACK, i, 3);
			ballsList1.add(ball);
		}

		// adding opponent's balls
		for (int i = 6; i <= 14; i += 2) {
			Ball ball = new Ball(WHITE, i, 9);
			ballsList2.add(ball);
		}
		for (int i = 5; i <= 15; i += 2) {
			Ball ball = new Ball(WHITE, i, 8);
			ballsList2.add(ball);
		}
		for (int i = 8; i <= 12; i += 2) {
			Ball ball = new Ball(WHITE, i, 7);
			ballsList2.add(ball);
		}
	}

	public State getState() {
		return state;
	}

	public int getMyCounter() {
		return myCounter;
	}

	public int getOpponentCounter() {
		return opponentCounter;
	}

	public int[] getBallPos(int i) {
		if (i >= myBallsToMove.size())
			return null;
		Ball ball = myBallsToMove.get(i);
		if (ball == null)
			return null;
		int[] pos = { ball.getX(), ball.getY() };
		return pos;
	}

	public int getLineLength() {
		return myBallsToMove.size() + opponentBallsToMove.size();
	}

	public Step getLineStep() {
		if (myBallsToMove.size() > 1) {
			Ball firstBall = myBallsToMove.get(0);
			Ball secondBall = myBallsToMove.get(1);
			return Step.getStep(secondBall.getX() - firstBall.getX(), secondBall.getY() - firstBall.getY());
		} else if (opponentBallsToMove.size() > 1) {
			Ball firstBall = opponentBallsToMove.get(0);
			Ball secondBall = opponentBallsToMove.get(1);
			return Step.getStep(secondBall.getX() - firstBall.getX(), secondBall.getY() - firstBall.getY());
		} else if (getLineLength() == 1)
			return E;
		else
			return null;
	}

	public String endTurn() {
		myBallsToMove.clear();
		opponentBallsToMove.clear();
		ballsToMoveOrientation = null;
		if (opponentCounter > 3)
			return "Przegra�e�!";
		else if (myCounter > 3)
			return "Wygra�e�!";
		else
			return null;
	}

}
