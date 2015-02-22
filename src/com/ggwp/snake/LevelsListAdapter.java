package com.ggwp.snake;

import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ggwp.snake.R;

public class LevelsListAdapter extends ArrayAdapter<String>{

	
	Context context;
	List<String> levels;
	public LevelsListAdapter(Context context, int resource, List<String> objects) {
		super(context, resource, objects);
		this.context=context;
		levels = objects;
		
	}
	
	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		
		String level  =  getItem(position);
		LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        
		final TextView tv;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            
            tv = (TextView) convertView.findViewById(R.id.Level);
            convertView.setTag(tv);
        } else
            tv = (TextView) convertView.getTag();
		
		tv.setText(level);
		
        tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				((Activity)context).finish();
				Intent intent = new Intent(context, MainActivity.class);
				((Activity)context).overridePendingTransition(R.anim.animation,R.anim.animation2);
				intent.putExtra("speed", ((Activity)context).getIntent().getIntExtra("speed",5) );
				intent.putExtra("difficulty", ((Activity)context).getIntent().getIntExtra("difficulty",1));
				intent.putExtra("level", Integer.parseInt(tv.getText().toString()));
				((Activity)context).startActivity(intent);
			}
		});
		return convertView;
	}

}
