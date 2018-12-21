package model;

public enum Step {
	E(0, 2, 0), SE(1, 1, 1), SW(2, -1, 1), W(3, -2, 0), NW(4, -1, -1), NE(5, 1, -1);
	private int id, dx, dy;

	Step(int id, int dx, int dy) {
		this.id = id;
		this.dx = dx;
		this.dy = dy;
	}

	public int id() {
		return id;
	}

	public int dx() {
		return dx;
	}

	public int dy() {
		return dy;
	}

	public static Step getFromID(int id) {
		switch (id) {
		case 0:
			return E;
		case 1:
			return SE;
		case 2:
			return SW;
		case 3:
			return W;
		case 4:
			return NW;
		case 5:
			return NE;
		default:
			return null;
		}
	}

	public static final Step getStep(int dx, int dy) {
		if (dx == dy && dx < 0)
			return NW;
		else if (dx == dy && dx > 0)
			return SE;
		else if (dx == -dy && dx > 0)
			return NE;
		else if (dx == -dy && dx < 0)
			return SW;
		else if (dy == 0 && dx > 0)
			return E;
		else if (dy == 0 && dx < 0)
			return W;
		else
			return null;
	}
};