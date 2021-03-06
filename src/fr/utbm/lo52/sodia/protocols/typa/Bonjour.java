package fr.utbm.lo52.sodia.protocols.typa;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.logic.Mime;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.logging.Level;
import java.util.logging.Logger;

class Bonjour
{

	private String type = "_typa._tcp.local.";

	private JmDNS jmdns = null;
	private ServiceListener listener = null;
	private ServiceInfo serviceInfo;
	
	private Context context;
	private static WifiManager.MulticastLock lock;
	
	public static String getLocalHostName(Context context)
	{
		WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(android.content.Context.WIFI_SERVICE);
		int ip = wifi.getConnectionInfo().getIpAddress();
		if(ip == 0)
		{
			return "localhost";
		}
		else
		{
			return String.format("%d.%d.%d.%d",
					(ip       & 0xff),
					(ip >>  8 & 0xff),
					(ip >> 16 & 0xff),
					(ip >> 24 & 0xff)
			);
		}
	}
	
	public Bonjour(Context context)
	{
		this.context = context;
		WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(android.content.Context.WIFI_SERVICE);
		lock = wifi.createMulticastLock("mylockthereturn");
		lock.setReferenceCounted(true);
		lock.acquire();
	}

	public void connect()
	{
		try
		{
			jmdns = JmDNS.create();
			jmdns.addServiceListener(type, listener = new ServiceListener()
				{
					public void serviceResolved(ServiceEvent event)
					{
						Log.i("Bonjour Discover", "Service resolved: "
								+ event.getInfo().getQualifiedName());
						for(InetAddress host : event.getInfo().getInetAddresses())
						{
							Log.i("Bonjour Discover", "	host:" + host);
						}
						Log.i("Bonjour Discover", "	port:" + event.getInfo().getPort());
						try
						{
							for(InetAddress host : event.getInfo().getInetAddresses())
							{
								if(host instanceof Inet6Address)
								{
									Log.i(getClass().getName(), "IPv6 ignored");
									continue;
								}
								if(host.equals(InetAddress.getByName(getLocalHostName(context))))
								{
									Log.i(getClass().getName(), "Local service detected !");
									break;
								}
								discover(host);
							}
						} catch (IOException e)
						{
							Log.e(getClass().getName(), "", e);
						} catch (Throwable e)
						{
							Log.e(getClass().getName(), "", e);
						}
					}

					public void serviceRemoved(ServiceEvent event)
					{
						Log.i("Bonjour Discover",
								"Service removed: " + event.getName());
						try
						{
							for(InetAddress host : event.getInfo().getInetAddresses())
							{
								Client client = Client.get(host);
								client.finalize();
							}
						} catch (Throwable e)
						{
							Log.e(getClass().getName(), "", e);
						}
					}

					public void serviceAdded(ServiceEvent event)
					{
						// Required to force serviceResolved to be called again
						// (after the first search)
						jmdns.requestServiceInfo(event.getType(),
								event.getName(), 1);
						Log.i("Bonjour Discover",
								"Service added:"
								+ event.getName() + " host:" + event.getInfo().getInet4Addresses()[0] + " port:"
								+ event.getInfo().getPort());
					}
				});
			serviceInfo = ServiceInfo.create(type, "Typa", Typa.PORT,
					"CSV Protocol for serverless local IM on Android");
			jmdns.registerService(serviceInfo);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		if (jmdns != null)
		{
			if (listener != null)
			{
				jmdns.removeServiceListener(type, listener);
				listener = null;
			}
			jmdns.unregisterAllServices();
			try
			{
				jmdns.close();
			} catch (IOException e)
			{
				Log.e(getClass().getName(), "", e);
			}
			jmdns = null;
		}
		if(lock != null)
		{
			lock.release();
		}
	}
	
	public static void discover(InetAddress host) throws IOException, Throwable
	{
		Client client = Client.get(host);
		Formater formater = new Formater();
		formater.operation = Formater.Operation.GET;
		formater.size = 0;
		formater.type = Formater.Type.CONTACT;
		client.send(formater);

		formater.type = Formater.Type.MESSAGE;
		ArrayList<Message> presence = new ArrayList<Message>();
		presence.add(new Message(Mime.PRESENCE, null));
		formater.setMessages(presence);
		client.send(formater);
	}
}
