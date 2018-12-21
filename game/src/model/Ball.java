package model;

import view.Color;

public class Ball {
	private Color color;
	private int x;
	private int y;

	public Ball(Color color, int x, int y) {
		this.color = color;
		this.x = x;
		this.y = y;
	}

	private static boolean isOnBoard(int x, int y) {
		if (y == 0 || y == 10 || x + y < 7 || x + y > 23 || x - y > 14 || x - y < -3) {
			return false;
		}
		else
			return true;
	}

	public boolean move(int dx, int dy) {
		x += dx;
		y += dy;
		if (isOnBoard(x, y))
			return true;
		else
			return false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Color getColor() {
		return color;
	}

}
