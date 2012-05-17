package fr.utbm.lo52.sodia.logic;

import android.accounts.Account;

public class Im
{
	private String id;
	private Account account;
	private Presence presence;
	private String status;
	public String getId()
	{
		return this.id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public Account getAccount()
	{
		return this.account;
	}
	public void setAccount(Account account)
	{
		this.account = account;
	}
	public Presence getPresence()
	{
		return this.presence;
	}
	public void setPresence(Presence presence)
	{
		this.presence = presence;
	}
	public String getStatus()
	{
		return this.status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
}
