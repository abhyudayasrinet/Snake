package com.ggwp.snake;

import com.ggwp.snake.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_main);
        
		
		AdView adView = (AdView) this.findViewById(R.id.adViewMainMenu);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
 
		
		
		Button play = (Button) findViewById(R.id.play);
		Button high_scores = (Button) findViewById(R.id.highscores);
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
        		builder.setTitle("Choose Difficulty")
        			.setItems(R.array.difficulty_options, new DialogInterface.OnClickListener() {
                    
        				public void onClick(DialogInterface dialog, int which) {
        					
        					finish();
        					Intent intent = new Intent(getApplicationContext(), Levels.class);
        					overridePendingTransition(R.anim.animation,R.anim.animation2);
        					
        					//beginner
        					if(which == 0) {
        						intent.putExtra("speed", 3);
        						intent.putExtra("difficulty", 0);
        						startActivity(intent);
        						
        					}
        					//normal
        					if(which == 1) {

        						
        						intent.putExtra("speed", 5);
        						intent.putExtra("difficulty", 1);
        						startActivity(intent);
        					}
        					//expert
        					if(which == 2) {

        						
        						intent.putExtra("speed", 10);
        						intent.putExtra("difficulty", 2);
        						startActivity(intent);
        					}
        				}
        			});
        		
        		AlertDialog dialog = builder.create();
    			dialog.setCancelable(true);
    			dialog.show();
				
				
								
				
			}
		});
		
		high_scores.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
				Intent intent = new Intent(getApplicationContext(), HighScores.class);
				overridePendingTransition(R.anim.animation,R.anim.animation2);
				startActivity(intent);
				
			}
		});
		
		final SharedPreferences mPreferences = getSharedPreferences("com.example.snake", Activity.MODE_PRIVATE);
		final int rateAppCounter = mPreferences.getInt("rateAppCounter", 1);
		
		AlertDialog.Builder rateAppBuilder = new Builder(MainMenu.this);
		
		if(rateAppCounter != -1 && rateAppCounter%10 == 0) {
			
			
			
			rateAppBuilder.setTitle("RATE US").setMessage("Had fun? Would you like to rate the app?");
			rateAppBuilder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					SharedPreferences.Editor editor = mPreferences.edit();
					editor.putInt("rateAppCounter", -1);
					editor.commit();
					
					//TODO Fix link
					//startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("www.google.com")));
					 
				}
			});
			rateAppBuilder.setNeutralButton("Later", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SharedPreferences.Editor editor = mPreferences.edit();
					editor.putInt("rateAppCounter", rateAppCounter+1);
					editor.commit();
				}
			});
			rateAppBuilder.setNegativeButton("Never", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				
					SharedPreferences.Editor editor = mPreferences.edit();
					editor.putInt("rateAppCounter", -1);
					editor.commit();
					
				}
			});
			
			AlertDialog dialog = rateAppBuilder.create();
			dialog.setCancelable(true);
			dialog.show();
			
		}
		else if(rateAppCounter != -1){
			SharedPreferences.Editor editor = mPreferences.edit();
			editor.putInt("rateAppCounter", rateAppCounter+1);
			editor.commit();
		}
		
	}

}
