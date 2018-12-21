package view;

public class ImageMove {
	private int dx;
	private int dy;
	private int rotation;

	public ImageMove(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
		this.rotation = getRotation(dx, dy);
	}
	

	private static int getRotation(int dx2, int dy2) {
		if (dx2==2) return 0;
		else if (dx2==1 && dy2==1) return 60;
		else if (dx2==-1 && dy2==1) return 120;
		else if (dx2==-2) return 180;
		else if (dx2==-1 && dy2==-1) return 240;
		else return 300;
	}


	public int getDx() {
		return dx;
	}

	public int getDy() {
		return dy;
	}

	public int getRotation() {
		return rotation;
	}
}
