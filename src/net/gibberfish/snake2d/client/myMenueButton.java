package net.gibberfish.snake2d.client;

public class myMenueButton {
	
	private int x;
	private int y;
	private int width;
	private int height;

	public myMenueButton(int ix, int iy, int iwidth, int iheight) {
		
		setX(ix);
		setY(iy);
		setWidth(iwidth);
		setHeight(iheight);
		
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
