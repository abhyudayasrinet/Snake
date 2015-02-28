package com.ggwp.snake;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends Activity implements OnClickListener {

	
	static int DB_VERSION = 1;
	private static final int SWIPE_MIN_DISTANCE = 15; 
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    GameView view;
    TextView Score;
    LinearLayout mainLayout,TopMenu;
	int width,height;
	int temp_height;
	SnakeDB DB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Score = new TextView(this);
		DB = new SnakeDB(this);
		
		//delete and recreate database if version is lower
		SharedPreferences prefs = getSharedPreferences("com.example.snake",Context.MODE_PRIVATE);
		int currentDbVersion = prefs.getInt("DB_VERSION", 0);
		
		if ( currentDbVersion < DB_VERSION ) {

            Editor editor = prefs.edit();
            editor.putInt("DB_VERSION", DB_VERSION);
            editor.commit();
            DB.deleteAll();
            setObstructions();
		}
		
		
		DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);   
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        Intent intent = getIntent();
        int difficulty = intent.getIntExtra("difficulty",1);
        int level = intent.getIntExtra("level",0);
		view = new GameView(this, width, height,Score,difficulty,level);
		view.setBackgroundColor(Color.parseColor("#f5f5f5"));
		view.SPEED = intent.getIntExtra("speed", 0);
		
		
		mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        
        TopMenu = new LinearLayout(this);
        TopMenu.setOrientation(LinearLayout.HORIZONTAL);
        mainLayout.addView(TopMenu);
        
        final Button pauseButton = new Button(this);
        pauseButton.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,1.0f));
        if(view.pause_game)
        	pauseButton.setText("PLAY");
        else
        	pauseButton.setText("PAUSE");
        
        pauseButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                view.Pause();
                if(view.pause_game)
                	pauseButton.setText("PLAY");
                else
                	pauseButton.setText("PAUSE");
            }
        });
        TopMenu.addView(pauseButton);
        
        Button restart = new Button(this);
        restart.setText("Restart");
        restart.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,1.0f));
        restart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	
               GameActivity.this.finish();
               
               
               Intent intent = new Intent(getApplicationContext(),GameActivity.class);
//               intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS 
//            		   | Intent.FLAG_ACTIVITY_NO_HISTORY);
               overridePendingTransition(R.anim.animation,R.anim.animation2);
               if(view.difficulty == 0) {

					
					intent.putExtra("speed", 3);
					intent.putExtra("difficulty", 0);
				}
				if(view.difficulty == 1) {

					
					intent.putExtra("speed", 5);
					intent.putExtra("difficulty", 1);
				}
				if(view.difficulty == 2) {

					
					intent.putExtra("speed", 10);
					intent.putExtra("difficulty", 2);
				}
				intent.putExtra("level", view.level);
                startActivity(intent);
                //MainActivity.this.finish();
            }
        });
        TopMenu.addView(restart);
        
        
        Score.setTextColor(Color.BLACK);
        Score.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,1.0f));
        Score.setTextAppearance(getApplicationContext(), android.R.attr.textAppearanceLarge);
        Score.setText("SCORE : 0");
        Score.setTextSize(20);
        TopMenu.addView(Score);
        
		final GestureDetector gestureDetector = new GestureDetector(this, 
				new MyGestureDetector());
		
		gestureListener = new View.OnTouchListener() {
            
        	public boolean onTouch(View v, MotionEvent event) {
        		return gestureDetector.onTouchEvent(event);
        	}
		
        };
        
        view.setOnClickListener(GameActivity.this);
		view.setOnTouchListener(gestureListener);
		
		
		final Button tv = restart;
	    ViewTreeObserver vto = tv.getViewTreeObserver();
	    vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	        public void onGlobalLayout() {
	            ViewGroup.MarginLayoutParams vlp = (MarginLayoutParams) tv.getLayoutParams();
	            temp_height =tv.getMeasuredHeight()+vlp.topMargin;
	            if(KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK))
	            	view.setScreenDim(width, height-temp_height);
	            ViewTreeObserver obs = tv.getViewTreeObserver();
	            obs.removeGlobalOnLayoutListener(this);
	        }
	    });
        
        mainLayout.addView(view);
        setContentView(mainLayout);
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if(!hasBackKey) {				
			view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}
	
	class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            
        	try {
            	
//        		Log.d("x",""+(e2.getX()-e1.getX()));
//        		Log.d("y",""+(e2.getY()-e1.getY()));
      
        		if(!view.pause_game) {
	                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE 
	                		&& (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY()))  
	                		) {
	                
	                	
	                	view.DirectHead(3); //LEFT
	                    
	                }
	                
	                else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
	                		&& (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY()))  
	                		) { 
	                	
	                	
	                	view.DirectHead(1); //RIGHT
	                }
	                else if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE 
	                		&& (Math.abs(e1.getX() - e2.getX()) < Math.abs(e1.getY() - e2.getY()))  
	                		) { 
	                	
	                	
	                    view.DirectHead(0); //UP
	                }
	                else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
	                		&& (Math.abs(e1.getX() - e2.getX()) < Math.abs(e1.getY() - e2.getY()))  
	                		) {
	              
	                	
	                	view.DirectHead(2); //DOWN
	                }
        		}
                
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

            @Override
        public boolean onDown(MotionEvent e) {
              return true;
        }
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
	
	public void onBackPressed() {    
		GameActivity.this.finish();
		Intent intent = new Intent(this,MainMenu.class);
	    overridePendingTransition(R.anim.animation,R.anim.animation2);
	    startActivity(intent);
	    
	}
	
	private void setObstructions( ) {		
		
		Obstructions temp; 
		
		//level 1
		temp = new Obstructions(99 ,0, 0, 0);
		DB.addObstruction(1, temp);
		temp = new Obstructions(0 , 99, 0, 0);
  		DB.addObstruction(1, temp);
  		temp = new Obstructions(0 , 0, 99, 0);
  		DB.addObstruction(1, temp);
  		temp = new Obstructions(0 , 0, 0, 99);
  		DB.addObstruction(1, temp);
  		
  		
		//level 2
  		temp = new Obstructions(99 ,0, 0, 0);
		DB.addObstruction(2, temp);
		temp = new Obstructions(0 , 99, 0, 0);
  		DB.addObstruction(2, temp);
  		temp = new Obstructions(0 , 0, 99, 0);
  		DB.addObstruction(2, temp);
  		temp = new Obstructions(0 , 0, 0, 99);
  		DB.addObstruction(2, temp);
  		temp = new Obstructions(17.5f, 50, 72.5f);
  		DB.addObstruction(2, temp);
  		temp = new Obstructions(17.5f, 50, 27.5f);
  		DB.addObstruction(2, temp);
  		
  		//level 3
  		temp = new Obstructions(99 ,0, 0, 0);
		DB.addObstruction(3, temp);
		temp = new Obstructions(0 , 99, 0, 0);
  		DB.addObstruction(3, temp);
  		temp = new Obstructions(0 , 0, 99, 0);
  		DB.addObstruction(3, temp);
  		temp = new Obstructions(0 , 0, 0, 99);
  		DB.addObstruction(3, temp);
  		temp = new Obstructions(18.75f ,76.25f, 25, 25);
  		DB.addObstruction(3, temp); 
  		temp = new Obstructions(47.5f, 47.5f, 25, 25);
  		DB.addObstruction(3, temp);
  		temp = new Obstructions(76.25f, 18.75f, 25, 25);
  		DB.addObstruction(3, temp);
  		
  		//level 4
  		temp = new Obstructions(99 ,0, 0, 0);
		DB.addObstruction(4, temp);
		temp = new Obstructions(0 , 99, 0, 0);
  		DB.addObstruction(4, temp);
  		temp = new Obstructions(0 , 0, 99, 0);
  		DB.addObstruction(4, temp);
  		temp = new Obstructions(0 , 0, 0, 99);
  		DB.addObstruction(4, temp);
  		temp = new Obstructions(17.5f, 27.5f, 27.5f);
  		DB.addObstruction(4, temp);
  		temp = new Obstructions(17.5f, 27.5f, 72.5f);
  		DB.addObstruction(4, temp);
  		temp = new Obstructions(17.5f, 72.5f, 27.5f);
  		DB.addObstruction(4, temp);
  		temp = new Obstructions(17.5f, 72.5f, 72.5f);
  		DB.addObstruction(4, temp);
  		
  		//level 5
  		temp = new Obstructions(99 ,0, 0, 0);
		DB.addObstruction(5, temp);
		temp = new Obstructions(0 , 99, 0, 0);
  		DB.addObstruction(5, temp);
  		temp = new Obstructions(0 , 0, 99, 0);
  		DB.addObstruction(5, temp);
  		temp = new Obstructions(0 , 0, 0, 99);
  		DB.addObstruction(5, temp);
  		temp = new Obstructions( 20, 75, 30, 30); // __
  		DB.addObstruction(5,temp);
  		temp = new Obstructions( 20, 45, 30, 65);// |
  		DB.addObstruction(5,temp);
  		temp = new Obstructions( 50, 45, 30, 30);//  __
  		DB.addObstruction(5,temp);
  		temp = new Obstructions( 50, 15, 65, 30);//    |
  		DB.addObstruction(5,temp); 
  		temp = new Obstructions( 80, 15, 30, 30); // __
  		DB.addObstruction(5,temp);
  		
  		//level 6
  		temp = new Obstructions(99 ,0, 0, 0);
		DB.addObstruction(6, temp);
		temp = new Obstructions(0 , 99, 0, 0);
  		DB.addObstruction(6, temp);
  		temp = new Obstructions(0 , 0, 99, 0);
  		DB.addObstruction(6, temp);
  		temp = new Obstructions(0 , 0, 0, 99);
  		DB.addObstruction(6, temp);
  		temp = new Obstructions(7.5f, 50, 15.5f);
  		DB.addObstruction(6, temp);
  		temp = new Obstructions(7.5f, 20, 38.5f);
  		DB.addObstruction(6, temp);
  		temp = new Obstructions(7.5f, 80, 38.5f);
  		DB.addObstruction(6, temp);
  		temp = new Obstructions(7.5f, 20, 61.5f );
  		DB.addObstruction(6, temp);
  		temp = new Obstructions(7.5f, 80, 61.5f);
  		DB.addObstruction(6, temp);
  		temp = new Obstructions(7.5f, 50, 85.5f);
  		DB.addObstruction(6, temp);
  		
  		//level 7
  		temp = new Obstructions(99 ,0, 0, 0);
		DB.addObstruction(7, temp);
		temp = new Obstructions(0 , 99, 0, 0);
  		DB.addObstruction(7, temp);
  		temp = new Obstructions(0 , 0, 99, 0);
  		DB.addObstruction(7, temp);
  		temp = new Obstructions(0 , 0, 0, 99);
  		DB.addObstruction(7, temp);
  		temp = new Obstructions(25 ,70, 0, 45);
		DB.addObstruction(7, temp);
		temp = new Obstructions(0 , 40, 70, 25);
  		DB.addObstruction(7, temp);
  		temp = new Obstructions(40 , 0, 25, 70);
  		DB.addObstruction(7, temp);
  		temp = new Obstructions(70 , 25, 45, 0);
  		DB.addObstruction(7, temp);
  		
  		//level 8
  		temp = new Obstructions(99 ,0, 0, 0);
		DB.addObstruction(8, temp);
		temp = new Obstructions(0 , 99, 0, 0);
  		DB.addObstruction(8, temp);
  		temp = new Obstructions(0 , 0, 99, 0);
  		DB.addObstruction(8, temp);
  		temp = new Obstructions(0 , 0, 0, 99);
  		DB.addObstruction(8, temp);
  		temp = new Obstructions(20 ,75, 25, 25);
		DB.addObstruction(8, temp);
		temp = new Obstructions(75 , 20, 25, 25);
  		DB.addObstruction(8, temp);
  		temp = new Obstructions(30 , 30, 10, 85);
  		DB.addObstruction(8, temp);
  		temp = new Obstructions(30 , 30, 85, 10);
  		DB.addObstruction(8, temp);
  		
  		
  		
  		//level 9
  		temp = new Obstructions(99 ,0, 0, 0);
		DB.addObstruction(9, temp);
		temp = new Obstructions(0 , 99, 0, 0);
  		DB.addObstruction(9, temp);
  		temp = new Obstructions(0 , 0, 99, 0);
  		DB.addObstruction(9, temp);
  		temp = new Obstructions(0 , 0, 0, 99);
  		DB.addObstruction(9, temp);
  		
  		temp = new Obstructions( 20, 73, 20, 73); // 0 0
  		DB.addObstruction(9,temp);
  		temp = new Obstructions( 47, 46, 20, 73); // 1 0 
  		DB.addObstruction(9,temp);
  		temp = new Obstructions( 74, 20, 20, 73); // 2 0
  		DB.addObstruction(9,temp);
  		
  		temp = new Obstructions( 20, 73, 47, 46); // 0 1 
  		DB.addObstruction(9,temp);
  		temp = new Obstructions( 47, 46, 47, 46); // 1 1 
  		DB.addObstruction(9,temp);
  		temp = new Obstructions( 74, 20, 47, 46); // 2 1
  		DB.addObstruction(9,temp);
  		
  		temp = new Obstructions( 20, 73, 74, 20); // 0 3
  		DB.addObstruction(9,temp);
  		temp = new Obstructions( 47, 46, 74, 20); // 1 3
  		DB.addObstruction(9,temp);
  		temp = new Obstructions( 74, 20, 74, 20); // 2 3
  		DB.addObstruction(9,temp);
  		
  		//level 10
  		temp = new Obstructions(30f, 50, 50);
  		DB.addObstruction(10, temp);
  	
}
	
	
}
