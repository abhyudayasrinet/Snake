package com.ggwp.snake;

public class Snake {
	
	int top;
	int bottom;
	int left;
	int right;
	int direction;
	
	Snake(int left , int top , int right , int bottom){
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
		this.direction = -1;
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
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public int getDirection() {
		return direction;
	}

}
