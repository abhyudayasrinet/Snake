package com.ggwp.snake;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class SnakeDB extends SQLiteOpenHelper {

	static String DB_NAME = "snakeDB";
	
	String ObstructionsTable = "OBSTRUCTIONS";
	String Level = "LEVEL";
	String Shape = "SHAPE";
	String Top = "TOP";
	String Bottom = "BOTTOM";
	String Left = "LEFT";
	String Right = "RIGHT";
	String Radius = "RADIUS";
	String X = "X";
	String Y = "Y";
	
	String HighScoreTable = "HIGHSCORES";
	//String Level = "LEVEL";
	String Mode = "MODE";
	String Difficulty = "DIFFICULTY";
	String Score = "SCORE";
	
	public SnakeDB(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String query = "CREATE TABLE "+ObstructionsTable+" ( "+Level+" integer, "+Shape+" text, "
						+Top+" integer, "+Bottom+" integer, "+Left+" integer, "+Right+" integer, "
						+Radius+" integer, "+X+" integer, "+Y+" integer)";
		
		db.execSQL(query);
		
		query = "CREATE TABLE "+HighScoreTable+" ( "+Level+" integer, "+Difficulty+" integer, "
				+Score+" integer, "+Mode+" integer)";
		
		db.execSQL(query);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("ON UPGRADE DROP TABLE "+ObstructionsTable);
		db.execSQL("ON UPGRADE DROP TABLE "+HighScoreTable);
	}

	public void updateHighScore(int difficulty, int level, int score,int mode) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values=new ContentValues();
		
	    values.put(Score,score );
	    
	    int i = db.update(HighScoreTable , values, Level+" = ? AND "+Difficulty+" = ? AND "+Mode+" = ?", new String []{String.valueOf(level),String.valueOf(difficulty),String.valueOf(mode)});
	    
	    if(i == 0) {
	    	
	    	values.put(Level,level);
	    	values.put(Mode,mode);
	    	values.put(Difficulty,difficulty);
	    	
	    	db.insert(HighScoreTable, null, values);
	    	
	    }
	    
	    db.close();
	    
	}
	
	public int getHighScore(int difficulty, int level,int mode) {
		
		SQLiteDatabase db = this.getWritableDatabase();

		String query = "SELECT * FROM "+HighScoreTable+" WHERE "+Difficulty+"="+difficulty+" AND "
						+Level+"="+level+" AND "+Mode+"="+mode;
		
		Cursor cursor = db.rawQuery(query, null);
		
		while(cursor.moveToFirst()) {
			
			db.close();
			return cursor.getInt(2);
			
		}
		db.close();
		return 0;
	}


	public ArrayList<Obstructions> getObstructions(int level) {

		ArrayList<Obstructions> obstructions = new ArrayList<Obstructions>();
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		String query = "SELECT * FROM "+ObstructionsTable+" WHERE "+Level+"="+level;
		
		Cursor cursor = db.rawQuery(query, null);
		
		Obstructions temp;
		
		int radius,x,y,top,left,bottom,right;
		
		if(cursor.moveToFirst()) {
			
			do {
			
				String shape = cursor.getString(1);
				
				if(shape.equals("Circle")) {
					
					radius = cursor.getInt(6);
					x = cursor.getInt(7);
					y = cursor.getInt(8);
					
					temp = new Obstructions(radius, x, y);
					
					obstructions.add(temp);
				}
				else {
					
					top = cursor.getInt(2);
					bottom = cursor.getInt(3);
					left = cursor.getInt(4);
					right = cursor.getInt(5);
					
					temp = new Obstructions(top, bottom, left, right);
					
					obstructions.add(temp);
				}
				
			} while(cursor.moveToNext());
			
			
		}
		
		db.close();
		return obstructions;
	}

	public void addObstruction(int level, Obstructions temp) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values=new ContentValues();
		
		
		values.put("Level", level);
		if(temp.getShape().equals("Circle")) {
			
			values.put("Shape", "Circle");
			values.put("Radius", temp.getRadius());
			values.put("X", temp.getCircle_x());
			values.put("Y", temp.getCircle_y());
			
		} else {
			
			values.put("Shape","Rectangle");
			values.put("Top", temp.getTop());
			values.put("Bottom",temp.getBottom());
			values.put("Left",temp.getLeft());
			values.put("Right",temp.getRight());
			
		}
		
		db.insert(ObstructionsTable, null, values);
		db.close();
	}
	
	
	public void deleteAll() {
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from "+ObstructionsTable);
		
	}
	
	
	
}