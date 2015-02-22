package com.ggwp.snake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.ggwp.snake.R;

public class HighScores extends Activity {
	
	
	ExpandableListView scoresList;
	ScoreExpandableListAdapter scoreListAdapter;
	SnakeDB DB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.high_scores);
		
		DB = new SnakeDB(this);
		scoresList = (ExpandableListView) findViewById(R.id.scoreListView);
		
		List <String> headers = new ArrayList<String>();
		headers.add("Easy");
		headers.add("Normal");
		headers.add("Hard");
		
		HashMap <String,List<String>> children = new HashMap<String, List<String>>(); 
		
		//List of easy high scores
		List <String> scoresEasy = new ArrayList<String>();
		for(int i=1;i<=5;i++)
		{
			int val = DB.getHighScore(0, i);
			scoresEasy.add(String.valueOf(val));
		}
		children.put("Easy",scoresEasy);
		
		//List of normal high scores
		List <String> scoresNormal = new ArrayList<String>();
		for(int i=1;i<=5;i++)
		{
			int val = DB.getHighScore(1, i);
			scoresNormal.add(String.valueOf(val));
		}
		children.put("Normal",scoresNormal);
		
		//List of easy high scores
		List <String> scoresHard = new ArrayList<String>();
		for(int i=1;i<=5;i++)
		{
			int val = DB.getHighScore(2, i);
			scoresHard.add(String.valueOf(val));
		}
		children.put("Hard",scoresHard);	
		
		scoreListAdapter = new ScoreExpandableListAdapter(this, headers, children);
		
		scoresList.setAdapter(scoreListAdapter);
		scoresList.setGroupIndicator(null);
	}
	
	public void onBackPressed() {    
		HighScores.this.finish();
	    Intent intent = new Intent(this,MainMenu.class);
	    overridePendingTransition(R.anim.animation,R.anim.animation2);
	    startActivity(intent);
	}

}
