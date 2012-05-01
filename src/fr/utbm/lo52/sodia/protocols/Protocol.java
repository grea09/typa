package fr.utbm.lo52.sodia.protocols;

import fr.utbm.lo52.sodia.protocols.ProtocolManager.ProtocolAlreadyRegisteredException;
import fr.utbm.lo52.sodia.logic.Message;
import android.accounts.Account;

/**
 * @author antoine
 *
 */
public abstract class Protocol
{
	public static final Class<? extends Protocol> CLASS = Protocol.class;
	public static final String NAME = ""; //eg Bonjour
	public static final String ACCOUNT_TYPE = ""; // com.apple.bonjour
	public static final boolean HAS_PASSWORD = true;
	protected Account account;

	/**
	 * @throws IllegalArgumentException
	 *             , ProtocolAlreadyRegisteredException
	 **/
	public Protocol(Account account) throws IllegalArgumentException,
			ProtocolAlreadyRegisteredException
	{
		if (account.type != ACCOUNT_TYPE)
		{
			throw new IllegalArgumentException("Account type not suported");
		}
		this.account = account;
		ProtocolManager.register(this);
	}

	public Account account()
	{
		return account;
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
	public abstract void send(Message message, String contact);

	/**
	 * Set status of current account on the network.
	 * @param status status in the db
	 */
	public abstract void presence(long status);

	/**
	 * Disconnect cuurent account
	 */
	public abstract void disconnect();
}
