package model;

import static model.Ball.Color.*;
import static model.Move.*;
import static model.Orientation.*;
import static model.State.*;

import model.Ball.Color;

public class BoardModel {

	public static final Color MY_COLOR = BLACK; // second player: WHITE
	public static final Move[] tableOfMoves = { E, SE, SW, W, NW, NE };

	private int myCounter;
	private int opponentCounter;
	private State state;

	public State getState() {
		return state;
	}
	
	/**
	 * Default constructor for the Controller
	 */
	public BoardModel() {
		this.myCounter = 0;
		this.opponentCounter = 0;
		this.state = MY_COLOR == WHITE ? CHOOSE_FIRST_BALL : null;
		this.chosenBalls = new ListOfBalls();
		this.opponentToMoveBalls = new ListOfBalls();
	}

	/**
	 * Method is called, when user chose their first ball
	 * 
	 * @param ball - first chosen Ball
	 */
	public void chooseFirstBall(Ball ball) {

		chosenBalls.add(ball);
		state = CHOOSE_SECOND_BALL;
	}

	public void startGame() {
		state = CHOOSE_FIRST_BALL;
	}

	/**
	 * Method is called, when user chose their second ball Method calls for creating
	 * list of chosen balls
	 * 
	 * @param ball - second chosen Ball
	 * @return orientation of the line made by chosen balls
	 */
	public Orientation chooseSecondBall(Ball ball) {
		int x1 = chosenBalls.get(0).getPosX();
		int x2 = ball.getPosX();
		int y1 = chosenBalls.get(0).getPosY();
		int y2 = ball.getPosY();
		int dx = x2 - x1;
		int dy = y2 - y1;

		if (!chosenBalls.get(0).equals(ball)) {
			state = CHOOSE_MOVE;
			return null;
		} else {
			state = CHOOSE_MOVE;
			return HORIZONTAL;
		}
	}

	public int numberOfChosenBalls() {
		return chosenBalls.size();
	}

	/**
	 * Method creates a table of moves (table of integers, each one is a number of
	 * free fields in every direction
	 * 
	 * @return a table of moves
	 */
	public int[] makeMovesTable(ListOfBalls myBallsList, ListOfBalls opponentBallsList) {
		int[] movesTable = { 0, 0, 0, 0, 0, 0 };

		for (Ball tmpBall : chosenBalls) {
			for (Move move : tableOfMoves) {
				if (myBallsList.getBall(tmpBall.getPosX() + move.dx(), tmpBall.getPosY() + move.dy()) == null
						&& opponentBallsList.getBall(tmpBall.getPosX() + move.dx(),
								tmpBall.getPosY() + move.dy()) == null)
					movesTable[move.id()]++;
			}
		}
		return movesTable;
	}

	public Orientation chooseBalls(int dx, int dy, int x1, int y1, ListOfBalls myBallsList) {
		for (int i = 1;; i += 1) {
			Ball tmpBall = myBallsList.getBall(x1 + i * (int) Math.signum(dx), y1 + i * (int) Math.signum(dy));
			if (tmpBall != null) {
				chosenBalls.add(tmpBall);
			}
			if (i == Math.abs(dx))
				break;
			if (i > 10)
				break;
		}
		return Orientation.getOrientation(dx, dy);
	}

	/**
	 * Method moves balls on the board. Method calls Board class method to make a
	 * move.
	 * 
	 * @param move        - the direction of a move
	 * @param orientation - the orientation of a line of chosen balls
	 */
	public void removeOpponentToMoveBallsIfNeeded(Move move, Orientation orientation) {

		if (orientation != null) {
			Move[] movesAlongLine = orientation.possibleMoves();
			if (!((movesAlongLine[0].equals(move) || movesAlongLine[1].equals(move))
					&& tableOfMoves[move.id()].isPossible())) {
				opponentToMoveBalls.removeAll(opponentToMoveBalls);
			}
		}

	}

	private ListOfBalls chosenBalls;
	private ListOfBalls opponentToMoveBalls;

	public ListOfBalls getChosenBalls() {
		// TODO Auto-generated method stub
		return chosenBalls;
	}

	public ListOfBalls getOpponentToMoveBalls() {
		// TODO Auto-generated method stub
		return opponentToMoveBalls;
	}

	public String getAlertText() {
		if (myCounter >= 4) {
			return "WYGRA£EŒ";
		} else if (opponentCounter >= 4) {
			return "PRZEGRA£EŒ";
		} else
			return null;
	}

	public void setPointCounters(ListOfBalls newBalls, ListOfBalls newOpponentBalls) {
		opponentCounter += chosenBalls.size() - newBalls.size();
		myCounter += opponentToMoveBalls.size() - newOpponentBalls.size();
	}

	public int getOpponentCounter() {
		return opponentCounter;
	}

	public int getMyCounter() {
		return myCounter;
	}

	public void endGame() {
		state = GAME_ENDED;

	}

	public void endMove() {
		removeToMoveBalls();
	}

	/**
	 * Method adding a possibility of moving along the line of chosen balls. Method
	 * checks whether these moves are possible and creates new buttons for these
	 * moves.
	 * 
	 * @param x2   - x position of the last ball in the line
	 * @param y2   - y position of the last ball in the line
	 * @param move - direction of a chosen move
	 * @return whether these move is possible or not
	 */
	public Integer addMoveAlongLine(int x2, int y2, Move move, ListOfBalls myBallsList,
			ListOfBalls opponentBallsList) {
		int dx = move.dx();
		int dy = move.dy();
		Integer p = null;

		for (int i = 1; i <= chosenBalls.size(); i++) {
			Ball tmpBall = myBallsList.getBall(x2 + i * dx, y2 + i * dy);
			if (tmpBall == null) {
				Ball tmpOpponentBall = opponentBallsList.getBall(x2 + i * dx, y2 + i * dy);
				if (tmpOpponentBall != null)
					return i;
				else {
					opponentToMoveBalls.add(tmpOpponentBall);
					if (i == chosenBalls.size())
						opponentToMoveBalls.removeAll(opponentToMoveBalls);
				}
			} else {
				if (chosenBalls.contains(tmpBall))
					continue;
				else {
					opponentToMoveBalls.removeAll(opponentToMoveBalls);
					break;
				}
			}
		}
		return null;
	}

	public int firstBallX() {
		return chosenBalls.get(0).getPosX();
	}
	
	public int firstBallY() {
		return chosenBalls.get(0).getPosY();
	}

	public void removeToMoveBalls() {
		chosenBalls.removeAll(chosenBalls);
		opponentToMoveBalls.removeAll(opponentToMoveBalls);		
	}
}
