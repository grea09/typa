package fr.utbm.lo52.sodia.ui;

import java.util.ArrayList;
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import fr.utbm.lo52.sodia.*;
import fr.utbm.lo52.sodia.R.layout;
import fr.utbm.lo52.sodia.logic.*;
import fr.utbm.lo52.sodia.protocols.*;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<Group> groupes;
	private LayoutInflater inflater;

	public ExpandableListAdapter(Context context, ArrayList<Group> groupes) {
		this.context = context;
		this.groupes = groupes;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	public Object getChild(int gPosition, int cPosition) {
		return groupes.get(gPosition).getContacts().toArray()[cPosition];
	}

	public long getChildId(int gPosition, int cPosition) {
		return cPosition;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final Contact contact = (Contact) getChild(groupPosition, childPosition);
		
		ChildViewHolder childViewHolder;
		
		if (convertView == null) {
			childViewHolder = new ChildViewHolder();
			
			convertView = inflater.inflate(R.layout.contactfragment, null);
			
			childViewHolder.quickContactBadge = (QuickContactBadge) convertView.findViewById(R.id.contactBadge);
			childViewHolder.image = (ImageView) convertView.findViewById(R.id.statusIcon);
			childViewHolder.textView = (TextView) convertView.findViewById(R.id.Name);
			
			childViewHolder.textView.setText(contact.getName());
			childViewHolder.quickContactBadge.setImageToDefault();
			
			convertView.setTag(childViewHolder);
		} 
		
		else {
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}
	
		childViewHolder.textView.setOnClickListener(new OnClickListener() {
		 
            public void onClick(View v) {
            	Toast.makeText(context, "Groupe ", Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "Groupe : " + contact.getGroups().toArray()[0] + " - Bouton : " + contact, Toast.LENGTH_SHORT).show();
            }
        });
		
		return convertView;
	}

	public int getChildrenCount(int gPosition) {
		return groupes.get(gPosition).getContacts().size();
	}

	public Object getGroup(int gPosition) {
		return groupes.get(gPosition);
	}

	public int getGroupCount() {
		return groupes.size();
	}

	public long getGroupId(int gPosition) {
		return gPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupViewHolder gholder;
		
		Group group = (Group) getGroup(groupPosition);
		
		if (convertView == null) {
			gholder = new GroupViewHolder();
			
			convertView = inflater.inflate(R.layout.groupfragment, null);
			
			gholder.textViewGroup = (TextView) convertView.findViewById(R.id.GroupName);
			
			convertView.setTag(gholder);
		} 
		else {
			gholder = (GroupViewHolder) convertView.getTag();
		}
		
		gholder.textViewGroup.setText(group.getName());
		
		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	class GroupViewHolder {
		public TextView textViewGroup;
	}

	class ChildViewHolder {
		public QuickContactBadge quickContactBadge;
		public Layout layout;
		public TextView textView;
		public Layout secondLayout;
		public TextView textViewStatus;
		public ImageView image;
		
	}






}