package com.ggwp.snake;

public class Obstructions {
	
	float top;
	float bottom;
	float left;
	float right;
	float radius;
	float circle_x;
	float circle_y;
	String shape;
	
	
	public Obstructions(float top, float bottom, float left, float right) {
			shape = "Rectangle";
			this.top = top;
			this.bottom = bottom;
			this.left = left;
			this.right = right;
	}

	public Obstructions(float radius,float x,float y) {
		shape = "Circle";
		this.circle_x = x;
		this.circle_y = y;
		this.radius = radius;
	}
	
	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getCircle_x() {
		return circle_x;
	}

	public void setCircle_x(float circle_x) {
		this.circle_x = circle_x;
	}

	public float getCircle_y() {
		return circle_y;
	}

	public void setCircle_y(float circle_y) {
		this.circle_y = circle_y;
	}

	public String getShape() {
		return shape;
	}
	public float getTop() {
		return top;
	}
	public void setTop(float top) {
		this.top = top;
	}
	public float getBottom() {
		return bottom;
	}
	public void setBottom(float bottom) {
		this.bottom = bottom;
	}
	public float getLeft() {
		return left;
	}
	public void setLeft(float left) {
		this.left = left;
	}
	public float getRight() {
		return right;
	}
	public void setRight(float right) {
		this.right = right;
	}
	

}
