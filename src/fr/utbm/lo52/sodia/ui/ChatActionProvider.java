package fr.utbm.lo52.sodia.ui;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import fr.utbm.lo52.sodia.R;

public class ChatActionProvider extends ActionProvider implements OnMenuItemClickListener
{
	private static final int LIST_LENGTH = 3;
	private Context context;

	public ChatActionProvider(Context context)
	{
		super(context);
		this.context = context;
	}

	@Override
	public View onCreateActionView() {
		Log.d(this.getClass().getSimpleName(), "onCreateActionView");
		 // Inflate the action view to be shown on the action bar.
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.chatfragment, null);
//        ImageView imageView = (ImageView) view.findViewById(R.id.chatFragmentContactPhoto);
//        imageView.setImageBitmap(bm);
        return view;
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

		PackageManager manager = context.getPackageManager();
		List<ApplicationInfo> applicationList = manager
				.getInstalledApplications(PackageManager.GET_ACTIVITIES);

		for (int i = 0; i < Math.min(LIST_LENGTH, applicationList.size()); i++) {
			ApplicationInfo appInfo = applicationList.get(i);

			subMenu.add(0, i, i, manager.getApplicationLabel(appInfo))
					.setIcon(appInfo.loadIcon(manager))
					.setOnMenuItemClickListener(this);
		}

		if (LIST_LENGTH < applicationList.size()) {
			subMenu = subMenu.addSubMenu(Menu.NONE, LIST_LENGTH, LIST_LENGTH,
					"hoge");

			for (int i = 0; i < applicationList.size(); i++) {
				ApplicationInfo appInfo = applicationList.get(i);

				subMenu.add(0, i, i, manager.getApplicationLabel(appInfo))
						.setIcon(appInfo.loadIcon(manager))
						.setOnMenuItemClickListener(this);
			}
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
		return true;
	}

}
