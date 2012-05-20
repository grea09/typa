package fr.utbm.lo52.sodia.ui;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.view.ActionProvider;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;

import fr.utbm.lo52.sodia.R;

public class ChatsActionProvider extends ActionProvider implements OnMenuItemClickListener {
	 
	private Context mContext;
	
	public ChatsActionProvider(Context context) {
		super(context);
		mContext = context;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean hasSubMenu() {
		Log.d(this.getClass().getSimpleName(), "hasSubMenu");
		return true;
	}

	@Override
	public View onCreateActionView() {
		 // Inflate the action view to be shown on the action bar.
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		View view = layoutInflater.inflate(R.layout.chats_action_provider, null);
		Button button = (Button) view.findViewById(R.id.chats);
		button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
			
				Log.d(this.getClass().getSimpleName(), "onClick");
			}
		});
		return view;
	}
	
	@Override
	public void onPrepareSubMenu(SubMenu subMenu) {
		Log.d(this.getClass().getSimpleName(), "onPrepareSubMenu");
		subMenu.clear();

		for (int i = 0; i < 3; i++) {
			subMenu.add(0, i, i, "Quel chat ?")
					.setIcon(R.drawable.ic_launcher)
					.setOnMenuItemClickListener(this);
		}
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		Toast.makeText(mContext, item.getTitle(), Toast.LENGTH_SHORT).show();
		return true;
	}

}
