package fr.utbm.lo52.sodia.protocols;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.accounts.Account;
import android.content.Context;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.logic.Status;

public class ProtocolManager
{
	
	public static final int NEW_CONTACT_NOTIFICATION_ID = 1;
	
	public static class ProtocolAlreadyRegisteredException extends Exception
	{
		/**
		 * Generated serialVersionUID
		 */
		private static final long serialVersionUID = 1962729403538150923L;

	}

	private static Map<Account, Protocol> accounts;
	
	public static Set<Account> getAccounts()
	{
		return accounts.keySet();
	}
	
	public static Set<Account> getAccountsByType(String type)
	{
		Set<Account> accounts = new HashSet<Account>();
		for(Account account: ProtocolManager.accounts.keySet())
		{
			if(account.type == type)
			{
				accounts.add(account);
			}
		}
		return accounts;
	}
	
	public static Set<Class<? extends Protocol>> getProtocolsClass()
	{
		Set<Class<? extends Protocol>> protocols = new HashSet<Class<? extends Protocol>>();
		for(Protocol protocol : accounts.values())
		{
			protocols.add(protocol.getClass());
		}
		return protocols;
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
	
	public static Set<String> getAccountTypes()
	{
		Set<Account> accounts = ProtocolManager.accounts.keySet();
		Set<String> value = new HashSet<String>();
		for(Account account : accounts)
		{
			value.add(account.type);
		}
		return value;
	}
	
	public static Protocol getProtocol(Account account)
	{
		return accounts.get(account);
	}

	public static void register(Protocol protocol)
			throws ProtocolAlreadyRegisteredException
	{
		if (accounts.containsKey(protocol.account()))
			throw new ProtocolAlreadyRegisteredException();
		accounts.put(protocol.account(), protocol);
	}

	public static void send(Context context, Message message, long contact)
	{
		// TODO accounts.get(account).send(message);
	}

	public static void presence(Context context, Status status, Account account)
	{
		//TODO : add logo
		accounts.get(account).presence(status);
	}

	public static void unregister(Protocol protocol)
	{
		if (accounts.containsValue(protocol)
				&& accounts.containsKey(protocol.account()))
			accounts.remove(protocol.account());
	}
	
	public static void receive(Context context, Message message, String contact, Account account)
	{
		
	}
	
	
	
	public static void presence(Context context, long status, String message, String contact, Account account)
	{
		
	}
	
	public static void newRoster(String name, Array contacts)
	{
		// Creation du nouveau groupe 
		// ajout des contacts
		
	}

}
