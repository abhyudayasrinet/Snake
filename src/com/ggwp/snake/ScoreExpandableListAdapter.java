package com.ggwp.snake;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.ggwp.snake.R;

public class ScoreExpandableListAdapter extends BaseExpandableListAdapter {

	Context context;
	List <String> headers;
	HashMap <String,List <String>> children;
	
	public ScoreExpandableListAdapter(Context context,List <String> headerList, HashMap <String,List <String>> childrenHashMap) {
		
		this.context = context;
		this.headers = headerList;		
		this.children = childrenHashMap;
	}
	
	@Override
    public int getGroupCount() {		
        return this.headers.size();
        
    }

	 @Override
    public int getChildrenCount(int groupPosition) {
        return this.children.get(this.headers.get(groupPosition)).size();
    }

	 @Override
	    public Object getGroup(int groupPosition) {
	        return this.headers.get(groupPosition);
	    }

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return this.children.get(this.headers.get(groupPosition)).get(childPosition);
	}

	@Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
		
        String headerTitle = (String) getGroup(groupPosition);
        TextView ListHeaderTV;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_header_highscore, null);
            ListHeaderTV = (TextView) convertView.findViewById(R.id.listHeaderHighScore);
            convertView.setTag(ListHeaderTV);
        }
        else {
        	ListHeaderTV = (TextView) convertView.getTag(); 
        }
 
        
        ListHeaderTV.setTypeface(null, Typeface.BOLD);
        ListHeaderTV.setText(headerTitle);
 
        return convertView;
    }

	class ViewHolder {
		TextView ListChildLevelTV;
		TextView ListChildScoreTV;
	}
	
	@Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final String childText = (String) getChild(groupPosition, childPosition);
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_child_highscore, null);
            holder = new ViewHolder();
            holder.ListChildLevelTV = (TextView) convertView.findViewById(R.id.listChildLevel);
            holder.ListChildScoreTV = (TextView) convertView.findViewById(R.id.listChildScore);
            convertView.setTag(holder);
        }
        else
        	holder = (ViewHolder) convertView.getTag();
 
        
        holder.ListChildScoreTV.setText(childText);
        holder.ListChildLevelTV.setText("Level "+String.valueOf(childPosition+1));
        
        
        return convertView;
    }

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
