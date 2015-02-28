package com.ggwp.snake;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class HighScoreTimeGameFragment extends Fragment {
	
	static final int CLASSIC_MODE = 0;
	static final int TIME_MODE = 1;
	
	ExpandableListView scoresList;
	HighScoreExpandableListAdapter scoreListAdapter;
	SnakeDB DB;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.highscores_game_tab, container, false);
		
		DB = new SnakeDB(view.getContext());
		scoresList = (ExpandableListView) view.findViewById(R.id.scoreListView);
		
		List <String> headers = new ArrayList<String>();
		headers.add("Easy");
		headers.add("Normal");
		headers.add("Hard");
		
		HashMap <String,List<String>> children = new HashMap<String, List<String>>(); 
		
		//List of easy high scores
		List <String> scoresEasy = new ArrayList<String>();
		for(int i=1;i<=10;i++)
		{
			int val = DB.getHighScore(0, i,TIME_MODE);
			scoresEasy.add(String.valueOf(val));
		}
		children.put("Easy",scoresEasy);
		
		//List of normal high scores
		List <String> scoresNormal = new ArrayList<String>();
		for(int i=1;i<=10;i++)
		{
			int val = DB.getHighScore(1, i,TIME_MODE);
			scoresNormal.add(String.valueOf(val));
		}
		children.put("Normal",scoresNormal);
		
		//List of easy high scores
		List <String> scoresHard = new ArrayList<String>();
		for(int i=1;i<=10;i++)
		{
			int val = DB.getHighScore(2, i,TIME_MODE);
			scoresHard.add(String.valueOf(val));
		}
		children.put("Hard",scoresHard);	
		
		scoreListAdapter = new HighScoreExpandableListAdapter(view.getContext(), headers, children);
		
		scoresList.setAdapter(scoreListAdapter);
		scoresList.setGroupIndicator(null);
		
		
		return view;
	}

}
