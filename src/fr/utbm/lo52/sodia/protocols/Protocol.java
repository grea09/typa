package fr.utbm.lo52.sodia.protocols;

import android.accounts.Account;

public interface Protocol
{
	public void connect(Account account);
	public void send(Account account, String message);
	public void presence(Account account, String status);
	public void disconnect(Account account);
}
