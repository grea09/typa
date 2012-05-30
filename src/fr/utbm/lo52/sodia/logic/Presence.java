package fr.utbm.lo52.sodia.logic;

import android.content.Context;
import fr.utbm.lo52.sodia.R;

public enum Presence
{
	OFFLINE(R.string.presence_offline), INVISIBLE(R.string.presence_invisible), AWAY(
			R.string.presence_away), IDLE(R.string.presence_idle), DO_NOT_DISTURB(
			R.string.presence_do_not_disturb), AVAILABLE(
			R.string.presence_avaible);

	private int resource;

	public static Presence get(long id)
	{
		return Presence.get((int) id);
	}

	public static Presence get(int id)
	{
		return Presence.values()[id];
	}

	private Presence(int resource)
	{
		this.resource = resource;
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
