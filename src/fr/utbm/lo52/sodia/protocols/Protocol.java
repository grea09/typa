package fr.utbm.lo52.sodia.protocols;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import fr.utbm.lo52.sodia.logic.*;
import fr.utbm.lo52.sodia.protocols.typa.Typa;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author antoine
 *
 */
public abstract class Protocol
{
	private static Set<Class<? extends Protocol> > protocols = new HashSet<Class<? extends Protocol>>();
	
	private static Map<Account, Protocol> accounts = new HashMap<Account, Protocol>();
	
	private static Set<ProtocolListener> listeners = new HashSet<ProtocolListener>();
	
	public static void add(Class<? extends Protocol> protocolClass)
	{
		protocols.add(protocolClass);
	}

	protected Context context;
	
	public void add(ProtocolListener listener)
	{
		listeners.add(listener);
	}
	
	public static Protocol constructor(Account account)
	{
		Set<Class<? extends Protocol>> classes = getProtocolsClass();
		for(Class<? extends Protocol> clazz : classes)
		{
			try
			{
				Protocol value = clazz.newInstance();
				if(value.getAccountType().equals(account.type))
				{
					value.setAccount(account);
					return value;
				}
			} catch (InstantiationException e)
			{
				Log.e(Protocol.class.getName(), "", e);
			} catch (IllegalAccessException e)
			{
				Log.e(Protocol.class.getName(), "", e);
			}
		}
		return null;
	}
	
	public static Protocol get(Account account, Context context)
	{
		Protocol protocol = get(account);
		protocol.context = context;
		Log.d(Protocol.class.getSimpleName(), "Context : " + context);
		protocol.createMe();
		return protocol;
	}
	
	public static Protocol get(Account account)
	{
		if(accounts.containsKey(account))
		{
			return accounts.get(account);
		}
		return constructor(account);
	}
	
	public static Account getAccount(String name)
	{
		for(Account account: Protocol.accounts.keySet())
		{
			if(account.name == name)
			{
				return account;
			}
		}
		return null;
	}
	
	public static Set<Account> getAccounts()
	{
		return accounts.keySet();
	}
	
	public Account[] getAccounts(Context context)
	{
		AccountManager accountManager = AccountManager.get(context);
		return accountManager.getAccountsByType(getAccountType());
	}
	
	public static Set<Account> getAccountsByType(String type)
	{
		Set<Account> accounts = new HashSet<Account>();
		for(Account account: Protocol.accounts.keySet())
		{
			if(account.type == type)
			{
				accounts.add(account);
			}
		}
		return accounts;
	}
	
	public static Set<String> getAccountTypes()
	{
		Set<Account> accounts = Protocol.accounts.keySet();
		Set<String> value = new HashSet<String>();
		for(Account account : accounts)
		{
			value.add(account.type);
		}
		return value;
	}
	
	public static Set<? extends Protocol> getProtocols()
	{
		return (Set<? extends Protocol>) accounts.values();
	}
	
	@SuppressWarnings("unchecked")
	public static<T extends Protocol> Set<T> getProtocolsByType(Class<T> clazz)
	{
		Set<T> protocols = new HashSet<T>();
		for(Protocol protocol : accounts.values())
		{
			if(protocol.getClass() == clazz)
			{
				protocols.add((T) protocol);
			}
		}
		return protocols;
	}
	
	public static Set<Class<? extends Protocol>> getProtocolsClass()
	{
		protocols.add(Typa.class);
		return protocols;
	}
	
	public static void remove(ProtocolListener listener)
	{
		listeners.remove(listener);
	}
	
	public static Class<? extends Protocol> typeToClass(String type)
	{
		Set<Class<? extends Protocol>> classes = getProtocolsClass();
		for(Class<? extends Protocol> clazz : classes)
		{
			try
			{
				Protocol value = clazz.newInstance();
				if(value.getAccountType() == type)
				{
					return clazz;
				}
			} catch (InstantiationException e)
			{
				Log.e(Protocol.class.getName(), "", e);
			} catch (IllegalAccessException e)
			{
				Log.e(Protocol.class.getName(), "", e);
			}
		}
		return null;
	}
	
	protected Account account;
	
	protected Contact me;
	
	public Protocol()
	{
		//Virtual static only.
	}
	
	/**
	 * @throws IllegalArgumentException
	 **/
	protected Protocol(Account account) throws IllegalArgumentException
	{
		this.setAccount(account);
	}
	
	public Account account()
	{
		return account;
	}
	
	/**
	 * Connect to current account
	 */
	public abstract void connect();
	
	public void contacts(final Contact[] contacts)
	{
		AsyncTask.execute(new Runnable()
		{
			
			@Override
			public void run()
			{
				for(ProtocolListener listener : listeners)
				{
					listener.contacts(contacts, account);
				}
				
			}
		});
	}
	
	/**
	 * Disconnect cuurent account
	 */
	public abstract void disconnect();
	
	
	public abstract String getAccountType(); // com.apple.bonjour
	
	
	public abstract int getLogoRessource();

	public Contact getMe()
	{
		return this.me;
	}
	
	public abstract String getName(); //eg Bonjour

	public abstract boolean hasPassword();

	/**
	 * Set status of current account on the network.
	 * @param status status in the db
	 */
	public abstract void presence(Status status) throws Throwable;

	public void receive(final Message message)
	{
		AsyncTask.execute(new Runnable()
			{
				
				@Override
				public void run()
				{
					for(ProtocolListener listener : listeners)
					{
						listener.receive(message, account);
					}
					
				}
			});
	}

	/**
	 * Send a message to contact using this protocol.
	 * @param message
	 * @param contact of the form "someone@server.org"
	 */
	public abstract void send(Message message) throws Throwable;
	
	protected void createMe()
	{
		if(me instanceof Contact)
		{
			return ;
		}
		
		try
		{
			String contactName = account.name.split("" + Im.USER_ID_SEPARATOR)[0];
			me = Contact.getByIm(account.name);
			if(me == null)
			{
				Log.i(getClass().getName(), "Create Me !!!");
				me = Contact.simpleCreate(account, contactName, new Im(account.name, new Status(Presence.AVAILABLE, "", System.currentTimeMillis(), null, null), ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM, getName()), "Friends");
			}
		} catch (RemoteException e)
		{
			Log.e(getClass().getName(), "", e);
		} catch (OperationApplicationException e)
		{
			Log.e(getClass().getName(), "", e);
		}
	}
	
	public void setAccount(Account account) throws IllegalArgumentException
	{
		if (!(account.type.equals(getAccountType())))
		{
			throw new IllegalArgumentException("Account type not suported");
		}
		this.account = account;
		accounts.put(account, this);
		if(context != null)
		{
			createMe();
		}
	}
}
