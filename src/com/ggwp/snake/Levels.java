package com.ggwp.snake;

import java.util.ArrayList;
import java.util.List;
import com.ggwp.snake.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class Levels extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.levels);
		
		AdView adView = (AdView) this.findViewById(R.id.adViewLevels);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
 
		
		
		
		List<String> levels = new ArrayList<String>();
		for(int i = 1 ; i<=10;i++) {
			
			levels.add(""+(i));
			
		}
		ListView listView = (ListView) findViewById(R.id.levelslist);
		LevelsListAdapter adapter = new LevelsListAdapter(this,
                R.layout.list_item, levels);
        listView.setAdapter(adapter);
				
	}
	
	public void onBackPressed() {    
		Levels.this.finish();
	    Intent intent = new Intent(this,MainMenu.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS 
     		   | Intent.FLAG_ACTIVITY_NO_HISTORY);
	    overridePendingTransition(R.anim.animation,R.anim.animation2);
	    startActivity(intent);
	}

}
