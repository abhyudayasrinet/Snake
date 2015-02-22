package com.ggwp.snake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class GameView extends View {

	SnakeDB DB;
	
	Context mcontext;
	int score;
	int SPEED;
	int add_body;
	int wrong_direction;
	int screen_width,screen_height;
	int margin;
	int difficulty;
	int level;
	float food_x,food_y;
	int food_radius;
	
	int snake_width;
	
	List<Snake> snake;
	List<Bend> bends;
	
	boolean game_over;
	boolean pause_game;
	
	ArrayList<Integer> vals;
	ArrayList<Obstructions> obstructions;
	
	Random randInt;
	Paint paint;
	
	TextView score_board;
	AlertDialog.Builder builder;
	
	public GameView(Context context,int width,int height,TextView scoreboard,int difficulty,
			int level) {
		
		super(context);
		
		mcontext = context;
		score_board = scoreboard;
		
		DB = new SnakeDB(mcontext);
		
		
		screen_width = width;
		screen_height = height;
		margin = 0;
		this.difficulty = difficulty;
		this.level = level;
		
		
		/*
		 *  0 - UP 
		 *  1 - RIGHT
		 *  2 - DOWN
		 *  3 - LEFT 
		*/
		snake = new LinkedList<Snake>();

		if(difficulty == 1) {
			
			snake_width = 25;
			
			snake.add(new Snake(100, 100, 100 + snake_width, 100 + snake_width));
			snake.add(new Snake(75, 100, 75 + snake_width, 100 + snake_width));
			snake.add(new Snake(50, 100, 50 + snake_width, 100 + snake_width));
			
		}
		else if(difficulty == 0) {
			
			snake_width = 27;
			snake.add(new Snake(102, 102, 102 + snake_width, 102 + snake_width));
			snake.add(new Snake(75, 102, 75 + snake_width, 102 + snake_width));
			snake.add(new Snake(48, 102, 48 + snake_width, 102 + snake_width));
			
		}
		else if(difficulty == 2) {
			
			snake_width = 30;
			snake.add(new Snake(100, 100, 100 + snake_width, 100 + snake_width));
			snake.add(new Snake(70, 100, 70 + snake_width, 100 + snake_width));
			snake.add(new Snake(40, 100, 40 + snake_width, 100 + snake_width));
			
		}
		
		snake.get(0).setDirection(1);
		snake.get(1).setDirection(1);
		snake.get(2).setDirection(1);
		
		food_radius = snake_width/2 +1;
		
		//Set_Obstructions();
		Load_obstructions();
		Log.d("load obstructions","done");
		
		wrong_direction = 3;
		
		bends = new LinkedList<Bend>();
		
		game_over = false;
		pause_game = false;
		
		score = 0;
		add_body = 0;
		
		vals = new ArrayList<Integer>();
		
		paint = new Paint();
		paint.setColor(Color.BLACK);
	    paint.setStrokeWidth(0);
	    
	    builder = new AlertDialog.Builder(mcontext);
	    
	    randInt = new Random();
	    food_y = randInt.nextInt(  (screen_height-food_radius) - (margin+food_radius)  ) + (margin+food_radius);
    	food_x = randInt.nextInt(  (screen_width-food_radius) - (margin+food_radius)  )+(margin+food_radius);
		while(!location_ok()) {
			food_y = randInt.nextInt(  (screen_height-food_radius) - (margin+food_radius)  ) + (margin+food_radius);
	    	food_x = randInt.nextInt(  (screen_width-food_radius) - (margin+food_radius)  )+(margin+food_radius);
		}
		Log.d("constructor","over");
	}
	
	


	/*
	 * Set screen dimensions
	 * ---called when users have a hardware back button---
	 */
	public void setScreenDim(int width,int height) {
		screen_width = width;
		screen_height = height;
		
	}
	
	/*
	 * Update the direction of head according to the swipe 
	 * check if swipe is not in the wrong direction
	 * eg. if swipe 1 was to right swipe 2 can't be left 
	 * direction - direction of swipe
	 * 0-Up
	 * 1-Right
	 * 2-Down
	 * 3-Left
	 */
	public void DirectHead(int direction){
		
		
		
		if(snake.get(0).getDirection() != direction && direction != wrong_direction) {
			
			
			snake.get(0).setDirection(direction);
			bends.add(new Bend( snake.get(0).getLeft(),
					snake.get(0).getRight(),
					snake.get(0).getTop(),
					snake.get(0).getBottom(),
					snake.get(0).getDirection()
					));
			
			if(direction == 0) {
				wrong_direction = 2;
			}
			else if(direction == 1) {
				wrong_direction = 3;
			}
			else if(direction == 2) {
				wrong_direction = 0;
			}
			else if(direction == 3) {
				wrong_direction = 1;
			}
			
			
		}
		
	}	
	
	/*
	 * Pauses the game - you don't say!
	 */
	public void Pause() {
		
		if(pause_game) {
			
			pause_game = false;
			
		}
		else {
			
			pause_game = true;
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		score_board.setText("Score : "+score);
		
		//drawing boundaries
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(5);
		canvas.drawLine(0, 0, screen_width, 0, paint);
		canvas.drawLine(0, screen_height , screen_width, screen_height , paint);
		canvas.drawLine(0, 0, 0, screen_height, paint);
		canvas.drawLine(screen_width, 0, screen_width, screen_height, paint);
		
		
		
		//drawing obstructions
		paint.setColor(Color.DKGRAY);
		for(int i = 0 ; i < obstructions.size() ; i++ ) {
			if(obstructions.get(i).getShape().equals("Rectangle")) {
				canvas.drawRect(obstructions.get(i).getLeft(),
									obstructions.get(i).getTop(), 
									obstructions.get(i).getRight(),
									obstructions.get(i).getBottom(), paint);
				
			}
			else if(obstructions.get(i).getShape().equals("Circle")) {
				canvas.drawCircle(obstructions.get(i).getCircle_x(), 
						obstructions.get(i).getCircle_y(), 
						obstructions.get(i).getRadius(), paint);
			}
		}
	
		paint.setColor(Color.BLACK);
		
		if(!pause_game){
		
			//update position of head
			update_head();
		
			//update the body of snake
			for(int i = 1 ; i < snake.size() ; i++)
				update_position(i);
	        
	        /*
	         * generate correct food coordinates if food is eaten
	         * --buggy
	         */
	        if(food_eaten()) {
	        	
	        	
	        	
	        	food_y = randInt.nextInt(  (screen_height -2*food_radius) );
	        	food_x = randInt.nextInt(  (screen_width - 2*food_radius) );
	        	while(!location_ok()) {
	        		
	        		food_y = randInt.nextInt(  (screen_height -2*food_radius) );
		        	food_x = randInt.nextInt(  (screen_width - 2*food_radius) );
	            	
	        	}
	        	
	        	
	        	
        	
	        }
        
		}
		
		/*
		 * animation for bends
		 */
        for(int i = 0; i < bends.size(); i++) {
        	
        	canvas.drawRect(bends.get(i).getLeft(), bends.get(i).getTop(), bends.get(i).getRight(),
        						bends.get(i).getBottom(), paint);
        	
        }
		
        /*
         * snake
         */
		for(int i = 0; i < snake.size() ; i++) {
        	
        	canvas.drawRect(snake.get(i).getLeft(),
        					snake.get(i).getTop(),
        					snake.get(i).getRight(),
        					snake.get(i).getBottom(),
        					paint);
        }
		
		/*
		 * Draw food
		 */
		paint.setColor(Color.parseColor("#A66829"));
        canvas.drawCircle(food_x, food_y, food_radius, paint);
        
        /*
         * As given
         */
        check_game_over();
        
        if(!game_over) {
        	invalidate();
        }
        else {
     
        	// create dialog and update high score if made 
        	if(DB.getHighScore(difficulty, level) < score) {
        		DB.updateHighScore(difficulty, level, score);
        		builder.setTitle("Congrats!!!").setMessage("New High Score!!!");
        		builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Activity activity = (Activity)getContext();
						activity.finish();
			               
			               
			               Intent intent = new Intent(mcontext,GameActivity.class);
//			               intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS 
//			            		   | Intent.FLAG_ACTIVITY_NO_HISTORY);
			               //overridePendingTransition(R.anim.animation,R.anim.animation2);
			               if(difficulty == 0) {

								
								intent.putExtra("speed", 3);
								intent.putExtra("difficulty", 0);
							}
							if(difficulty == 1) {

								
								intent.putExtra("speed", 5);
								intent.putExtra("difficulty", 1);
							}
							if(difficulty == 2) {

								
								intent.putExtra("speed", 10);
								intent.putExtra("difficulty", 2);
							}
							intent.putExtra("level", level);
			                mcontext.startActivity(intent);
						
					}
				});
        		
        		builder.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Activity activity = (Activity)getContext();
						activity.finish();
						
    					Intent intent = new Intent(mcontext, Levels.class);
    					intent.putExtra("speed", SPEED);
						intent.putExtra("difficulty", difficulty);
						mcontext.startActivity(intent);
						
						
					}
				});
        		AlertDialog dialog = builder.create();
    			dialog.setCancelable(false);
    			dialog.show();
        	}
        
        	
        }
        
		
	}
	
	
	
	/*
	 * load obstructions from the database for the given level
	 */
	private void Load_obstructions() {
		
		obstructions  = DB.getObstructions(level);
		Log.d("obstructions size",obstructions.size()+"");
		for(int i  =0 ; i < obstructions.size();i++) {
			if(obstructions.get(i).getShape().equals("Rectangle")) {
				
				obstructions.get(i).setTop((float) ( (obstructions.get(i).getTop()/100.0) * screen_height));
				obstructions.get(i).setBottom( screen_height - (float)((obstructions.get(i).getBottom()/100.0) * screen_height));
				obstructions.get(i).setLeft((float)((obstructions.get(i).getLeft()/100.0) * screen_width));
				obstructions.get(i).setRight( screen_width - (float)((obstructions.get(i).getRight()/100.0) * screen_width));
		
			}
			else if(obstructions.get(i).getShape().equals("Circle") ){
				obstructions.get(i).setRadius((float)((obstructions.get(i).getRadius()/100.0) * screen_width));
				obstructions.get(i).setCircle_x((float)((obstructions.get(i).getCircle_x()/100.0) * screen_width));
				obstructions.get(i).setCircle_y((float)((obstructions.get(i).getCircle_y()/100.0) * screen_height));
		
			}
		}
		
		
	}
	
	
	/*
	 * check if the location of the food is not covered by the body of the snake
	 */
	private boolean location_ok() {
		
		if(food_x > screen_width - food_radius || food_y > screen_height - food_radius ||food_x < food_radius || food_y < food_radius)
			return false;
		
		
		for(int i = 0 ; i < snake.size() ; i++ ) {
			
			if( food_x >= snake.get(i).getLeft()-food_radius && food_x <= snake.get(i).getRight()+food_radius
					&& food_y >= snake.get(i).getTop()-food_radius && food_y <= snake.get(i).getBottom()+food_radius
						) {
				return false;
			}
			
		}
		for(int i = 0 ; i < bends.size() ; i++ ) {
			
			if(food_x > bends.get(i).getLeft()-food_radius && food_x < bends.get(i).getRight()+food_radius
					&& food_y > bends.get(i).getTop()-food_radius && food_y < bends.get(i).getBottom()+food_radius
						) {
				return false;
			}
			
		}
		
		for( int i = 0 ; i < obstructions.size() ; i++) {
			if(obstructions.get(i).getShape().equals("Rectangle")) {
				
				if( food_x >= obstructions.get(i).getLeft()-food_radius && 
					food_x <= obstructions.get(i).getRight()+food_radius &&
					food_y <= obstructions.get(i).getBottom()+food_radius &&
					food_y >= obstructions.get(i).getTop()-food_radius ) {
					
					return false;
				}
			}
			else if(obstructions.get(i).getShape().equals("Circle")) {
				float radius = obstructions.get(i).getRadius() + food_radius;
				int circle_x = (int)food_x;
				int circle_y = (int)food_y;
				circle_x -= obstructions.get(i).getCircle_x();
				circle_y -= obstructions.get(i).getCircle_y();
				if( circle_y*circle_y + circle_x*circle_x <= radius*radius) {
					return false;
				}
			}
		}
		
		return true;
	}

	/*
	 * UPDATE POSITION OF HEAD
	 * move the head according to the direction
	 */
	private void update_head() {
		
		if(snake.get(0).getDirection() == 0){ //UP
			
			snake.get(0).setTop(snake.get(0).getTop()-SPEED);
			snake.get(0).setBottom(snake.get(0).getBottom()-SPEED);
			
		}
		else if(snake.get(0).getDirection() == 1){ //RIGHT
			
			snake.get(0).setRight(snake.get(0).getRight()+SPEED);
			snake.get(0).setLeft(snake.get(0).getLeft()+SPEED);
			
		}
		else if(snake.get(0).getDirection() == 2){ //DOWN
			
			snake.get(0).setTop(snake.get(0).getTop()+SPEED);
			snake.get(0).setBottom(snake.get(0).getBottom()+SPEED);
			
			
		}
		else { //LEFT
			
			snake.get(0).setRight(snake.get(0).getRight()-SPEED);
			snake.get(0).setLeft(snake.get(0).getLeft()-SPEED);
			
		}
			
		
	}
	
	
	/* UPDATE POSITION OF SNAKE BODY
	 * check if it needs to bend and update direction
	 * update coordinates according to the direction
	 */
	private void update_position(int index) {
		
		for(int i = 0 ; i < bends.size() ; i++){
			
			
			if(bends.get(i).matchBody(snake.get(index))) {
				snake.get(index).setDirection(bends.get(i).getDirection());
				
			}
			
			if(bends.get(i).matchBody(snake.get(index)) && index == snake.size()-1 ) {
				
				bends.remove(i);
			}
		}
		
		if(snake.get(index).getDirection() == 0){ //UP
			
			snake.get(index).setTop(snake.get(index).getTop()-SPEED);
			snake.get(index).setBottom(snake.get(index).getBottom()-SPEED);
			
		}
		else if(snake.get(index).getDirection() == 1){ //RIGHT
			
			snake.get(index).setRight(snake.get(index).getRight()+SPEED);
			snake.get(index).setLeft(snake.get(index).getLeft()+SPEED);
			
		}
		else if(snake.get(index).getDirection() == 2){ //DOWN
			
			snake.get(index).setTop(snake.get(index).getTop()+SPEED);
			snake.get(index).setBottom(snake.get(index).getBottom()+SPEED);
			
			
		}
		else { //LEFT
			
			snake.get(index).setRight(snake.get(index).getRight()-SPEED);
			snake.get(index).setLeft(snake.get(index).getLeft()-SPEED);
			
		}
		
		
		
	}

	
	/*
	 * CHECK IF GAME OVER
	 * collisions with wall or self or obstacles
	*/
	private void check_game_over() {
		
		
		//collision with walls
		if( (snake.get(0).getRight() >= screen_width-margin ||
				snake.get(0).getLeft() <= margin || 
						snake.get(0).getTop() <= margin || 
								snake.get(0).getBottom() >= screen_height - margin )) {
			
			game_over = true;
			
		}
		
		for(int i = 0 ;  i < obstructions.size(); i++) {
			
			int left = snake.get(0).getLeft();
			int top = snake.get(0).getTop();
			int right = snake.get(0).getRight();
			int bot = snake.get(0).getBottom();
			
			if(obstructions.get(i).getShape().equals("Rectangle")) {
				if(left > obstructions.get(i).getLeft() && left < obstructions.get(i).getRight()
					&& top > obstructions.get(i).getTop() && top < obstructions.get(i).getBottom()
						)
					game_over = true;
				if(left > obstructions.get(i).getLeft() && left < obstructions.get(i).getRight()
						&& bot > obstructions.get(i).getTop() && bot < obstructions.get(i).getBottom()
							)
					game_over = true;
					
				if(right > obstructions.get(i).getLeft() && right < obstructions.get(i).getRight()
						&& top > obstructions.get(i).getTop() && top < obstructions.get(i).getBottom()
							)
					game_over = true;
				if(right > obstructions.get(i).getLeft() && right < obstructions.get(i).getRight()
						&& bot > obstructions.get(i).getTop() && bot < obstructions.get(i).getBottom()
							)
					game_over = true;
			}
			else if(obstructions.get(i).getShape().equals("Circle")) {
				float radius = obstructions.get(i).getRadius();
				float circle_x = obstructions.get(i).getCircle_x();
				float circle_y = obstructions.get(i).getCircle_y();
				top -= circle_y;
				left -= circle_x;
				bot -= circle_y;
				right -=circle_x;
				if( top*top + left*left <= radius*radius ||	//top left
					top*top + right*right <= radius*radius ||	//top right
					bot*bot + right*right <= radius*radius ||	//bottom right
					bot*bot + left*left <= radius*radius 	//bottom left
						) {
					game_over = true;
				}
				
			}
		}
		
		
		//collision with self
		for(int i = 2 ; i < snake.size(); i++ ) {
			
			if(snake_collision(i)) {
				game_over = true;
				break;
			}
			
			
		}
		
	}
	
	
	/*
	 * Check if snake has collided with itself
	 * 
	 */
	private boolean snake_collision(int index) {
		
		int left = snake.get(0).getLeft();
		int top = snake.get(0).getTop();
		int right = snake.get(0).getRight();
		int bot = snake.get(0).getBottom();
		
		if(left > snake.get(index).getLeft() && left < snake.get(index).getRight()
			&& top > snake.get(index).getTop() && top < snake.get(index).getBottom()
				)
			return true;
		if(left > snake.get(index).getLeft() && left < snake.get(index).getRight()
				&& bot > snake.get(index).getTop() && bot < snake.get(index).getBottom()
					)
				return true;
			
		if(right > snake.get(index).getLeft() && right < snake.get(index).getRight()
				&& top > snake.get(index).getTop() && top < snake.get(index).getBottom()
					)
				return true;
		if(right > snake.get(index).getLeft() && right < snake.get(index).getRight()
				&& bot > snake.get(index).getTop() && bot < snake.get(index).getBottom()
					)
				return true;
			
		
		return false;
		
	}
	
	/*
	 * Check if the food has been eaten
	 */	
	private boolean food_eaten() {
		
		int left = snake.get(0).getLeft();
		int right = snake.get(0).getRight();
		int top = snake.get(0).getTop();
		int bot = snake.get(0).getBottom();
		
		top -= food_y;
		bot -= food_y;
		right -= food_x;
		left -= food_x;
		//Log.d("food",""+food_x+ " "+food_y);
		//Log.d("snake",""+left+" "+top+" "+right+" "+bot);
		
		if( top*top + left*left <= food_radius*food_radius ||//top left
			top*top + right*right <= food_radius*food_radius ||// top right	
			bot*bot + right*right <= food_radius*food_radius ||//bottom right
			bot*bot + left*left <= food_radius*food_radius
				) {
			score++;
			
			add_snake();
			
			return true;
			
		}
		
		return false;
		
	}
	
	/*
	 * Add new body part to the snake when it eats the food
	 */
	private void add_snake() {
		Snake temp;
		if(snake.get(snake.size()-1).getDirection() == 0) {
			temp = new Snake(snake.get(snake.size()-1).getLeft(),
							snake.get(snake.size()-1).getBottom(),
							snake.get(snake.size()-1).getRight(),
							snake.get(snake.size()-1).getBottom()+snake_width
					);
			temp.setDirection(0);
			snake.add(temp);
		}
		else if(snake.get(snake.size()-1).getDirection() == 1) {
			temp = new Snake(snake.get(snake.size()-1).getLeft()-snake_width,
							snake.get(snake.size()-1).getTop(),
							snake.get(snake.size()-1).getLeft(),
							snake.get(snake.size()-1).getBottom()
					);
			temp.setDirection(1);
			snake.add(temp);
		}
		else if(snake.get(snake.size()-1).getDirection() == 2) {
				temp = new Snake(snake.get(snake.size()-1).getLeft(),
						snake.get(snake.size()-1).getTop()-snake_width,
						snake.get(snake.size()-1).getRight(),
						snake.get(snake.size()-1).getTop()
				);
		temp.setDirection(2);
		snake.add(temp);
		}
		else if(snake.get(snake.size()-1).getDirection() == 3) {
			temp = new Snake(snake.get(snake.size()-1).getRight(),
							snake.get(snake.size()-1).getTop(),
							snake.get(snake.size()-1).getRight()+snake_width,
							snake.get(snake.size()-1).getBottom()
					);
			temp.setDirection(3);
			snake.add(temp);
		}
		
		
	}
}