package fr.utbm.lo52.sodia.protocols;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.accounts.Account;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Group;
import fr.utbm.lo52.sodia.logic.Im;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.logic.Name;
import fr.utbm.lo52.sodia.logic.Presence;
import fr.utbm.lo52.sodia.logic.RawContact;
import fr.utbm.lo52.sodia.logic.Status;
import fr.utbm.lo52.sodia.protocols.typa.Typa;

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
	
	public void setAccount(Account account) throws IllegalArgumentException
	{
		if (!(account.type.equals(getAccountType())))
		{
			throw new IllegalArgumentException("Account type not suported");
		}
		this.account = account;
		try
		{
			me = Contact.getByIm(account.name + Im.USER_ID_SEPARATOR + InetAddress.getLocalHost().getHostName());
			if(me == null)
			{
				me = new Contact(account.name);
				Im im = new Im(account.name + "@" + InetAddress.getLocalHost().getHostName(), account.name, new Status(Presence.AVAILABLE, "", System.currentTimeMillis(), null, null)); // TODO Add label and icon
				RawContact rawContact = new RawContact(false, account, new Name(account.name, null, null, null));
				rawContact.addIm(im);
				Group group = new Group("LAN");
				group.setAccount(account);
				me.addRawContact(rawContact);
				me.add(group);
				me.save();
			}
		} catch (UnknownHostException e)
		{
			Log.e(getClass().getName(), "", e);
		} catch (RemoteException e)
		{
			Log.e(getClass().getName(), "", e);
		} catch (OperationApplicationException e)
		{
			Log.e(getClass().getName(), "", e);
		}
		accounts.put(account, this);
	}
}
