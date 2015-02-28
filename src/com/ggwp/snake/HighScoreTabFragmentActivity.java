package com.ggwp.snake;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Window;


public class HighScoreTabFragmentActivity extends FragmentActivity {
	
	private FragmentTabHost mTabHost;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.highscore_tab_fragment);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("Classic Mode").setIndicator("Classic Mode"),
            HighScoreClassicGameFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Time Mode").setIndicator("Time Mode"),
            HighScoreTimeGameFragment.class, null);
        
		
    }
		
	

}