package fr.utbm.lo52.sodia.logic;

import android.content.Context;
import fr.utbm.lo52.sodia.R;

public enum Presence
{
	AVAILABLE(R.string.presence_avaible, R.drawable.ic_status_available),
	AWAY(R.string.presence_away, R.drawable.ic_status_away), 
	DO_NOT_DISTURB(R.string.presence_do_not_disturb, R.drawable.ic_status_busy),
	INVISIBLE(R.string.presence_invisible, R.drawable.ic_status_invisible), 
	OFFLINE(R.string.presence_offline, R.drawable.ic_status_offline);

	public int getImage() {
		return image;
	}

	private int resource;
	private int image;

	public static Presence get(long id)
	{
		return Presence.get((int) id);
	}

	public static Presence get(int id)
	{
		return Presence.values()[id];
	}

	private Presence(int resource, int image)
	{
		this.resource = resource;
		this.image = image;
	}

	public int getNameResource()
	{
		return resource;
	}

	public String toString(Context context)
	{
		return context.getString(resource);
	}
}
