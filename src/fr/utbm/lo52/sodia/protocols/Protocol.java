package fr.utbm.lo52.sodia.protocols;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.accounts.Account;
import android.content.Context;
import android.graphics.Bitmap;
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.logic.Status;
import fr.utbm.lo52.sodia.protocols.ProtocolManager.ProtocolAlreadyRegisteredException;
import fr.utbm.lo52.sodia.ui.ContactNotification;

/**
 * @author antoine
 *
 */
public abstract class Protocol
{
	public abstract String getName(); //eg Bonjour
	public abstract String getAccountType(); // com.apple.bonjour
	public abstract int getLogoRessource();
	
	public abstract boolean hasPassword();
	
	protected Account account;
	protected Contact me;
	
	public Protocol()
	{
		//Please set the account before register
	}
	
	
	/**
	 * @throws IllegalArgumentException, ProtocolAlreadyRegisteredException
	 * @throws UnknownHostException 
	 **/
	public Protocol(Account account) throws IllegalArgumentException, ProtocolAlreadyRegisteredException, UnknownHostException
	{
		this.setAccount(account);
	}

	public Account account()
	{
		return account;
	}
	
	public void setAccount(Account account) throws IllegalArgumentException, ProtocolAlreadyRegisteredException, UnknownHostException
	{
		if (account.type != getAccountType())
		{
			throw new IllegalArgumentException("Account type not suported");
		}
		this.account = account;
		me = Contact.getByIm(account.name + "@" + InetAddress.getLocalHost().getHostName());
		ProtocolManager.register(this);
	}

	/**
	 * Connect to current account
	 */
	public abstract void connect();

	/**
	 * Send a message to contact using this protocol.
	 * @param message
	 * @param contact of the form "someone@server.org"
	 */
	public abstract void send(Message message);

	/**
	 * Set status of current account on the network.
	 * @param status status in the db
	 */
	public abstract void presence(Status status);

	/**
	 * Disconnect cuurent account
	 */
	public abstract void disconnect();
	
	
	protected void receive(Context context, Message message, String contact)
	{
		ProtocolManager.receive(context, message, contact, account);
	}
	
	protected void newContact(Context context, Bitmap photo, String name, String contact)
	{
		ContactNotification.newContactNotification(context, photo, name, contact, account);
	}
	
	protected void presence(Context context, long status, String message, String contact)
	{
		ProtocolManager.presence(context, status, message, contact, account);
	}
	public Contact getMe()
	{
		return this.me;
	}
}
