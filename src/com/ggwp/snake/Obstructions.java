package com.ggwp.snake;

public class Obstructions {
	
	int top;
	int bottom;
	int left;
	int right;
	int radius;
	int circle_x;
	int circle_y;
	String shape;
	
	
	public Obstructions(int top, int bottom, int left, int right) {
			shape = "Rectangle";
			this.top = top;
			this.bottom = bottom;
			this.left = left;
			this.right = right;
	}

	public Obstructions(int radius,int x,int y) {
		shape = "Circle";
		this.circle_x = x;
		this.circle_y = y;
		this.radius = radius;
	}
	
	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getCircle_x() {
		return circle_x;
	}

	public void setCircle_x(int circle_x) {
		this.circle_x = circle_x;
	}

	public int getCircle_y() {
		return circle_y;
	}

	public void setCircle_y(int circle_y) {
		this.circle_y = circle_y;
	}

	public String getShape() {
		return shape;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getBottom() {
		return bottom;
	}
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}
	

}
