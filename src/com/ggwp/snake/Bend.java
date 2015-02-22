package com.ggwp.snake;

import android.util.Log;

public class Bend {

	public Bend(int left, int right, int top, int bottom, int direction) {
		
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.direction = direction;
		
	}
	int left;
	int right;
	int top;
	int bottom;
	int direction;
	
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
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public boolean matchBody (Snake snake) {
		
			if(snake.getBottom()==bottom &&
					snake.getLeft()==left &&
					snake.getRight()==right &&
					snake.getTop()==top)
				return true;
		return false;
	}

}
