package model;

import java.util.ArrayList;

public class ListOfBalls extends ArrayList<Ball>{
	
	private static final long serialVersionUID = 1L;

	public ListOfBalls() {
		super();
	}
	
	public Ball getBall (int x, int y) {
		for (int i=0; i<this.size(); i++) {
			Ball ball = this.get(i);
			if (ball.getX() == x && ball.getY() == y ) return ball;			
		}
		return null;
	}
}
