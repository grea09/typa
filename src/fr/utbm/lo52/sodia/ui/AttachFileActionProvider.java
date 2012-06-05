package fr.utbm.lo52.sodia.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.view.ActionProvider;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;

import fr.utbm.lo52.sodia.R;

public class AttachFileActionProvider extends ActionProvider implements OnMenuItemClickListener {

	Context mContext;
	
	public AttachFileActionProvider(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public View onCreateActionView() {
		Log.d(this.getClass().getSimpleName(), "onCreateActionView");

		TextView textView = new TextView(mContext);
		textView.setText("Pick");

		return null;
	}

	@Override
	public boolean onPerformDefaultAction() {
		Log.d(this.getClass().getSimpleName(), "onPerformDefaultAction");

		return super.onPerformDefaultAction();
	}

	@Override
	public boolean hasSubMenu() {
		Log.d(this.getClass().getSimpleName(), "hasSubMenu");

		return true;
	}

	@Override
	public void onPrepareSubMenu(SubMenu subMenu) {
		Log.d(this.getClass().getSimpleName(), "onPrepareSubMenu");

		subMenu.clear();
		subMenu.add(0, 0, 0, "Music").setOnMenuItemClickListener(this).setIcon(R.drawable.ic_play);
		subMenu.add(0, 1, 1, "Image").setOnMenuItemClickListener(this).setIcon(R.drawable.ic_picture);
	}

	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()){
		case 0:
			break;
		case 1:
			break;
		}
		return true;
	}
}
