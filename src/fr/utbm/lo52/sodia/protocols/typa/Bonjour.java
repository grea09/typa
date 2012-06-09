package fr.utbm.lo52.sodia.protocols.typa;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.logic.Mime;

public class Bonjour extends AsyncTask<Void, Void, Void>
{

	private String type = "_typa._tcp.local.";

	private JmDNS jmdns = null;
	private ServiceListener listener = null;
	private ServiceInfo serviceInfo;
	
	private static WifiManager.MulticastLock lock;

	@Override
	protected Void doInBackground(Void... voids)
	{
		try
		{
			jmdns = JmDNS.create();
			jmdns.addServiceListener(type, listener = new ServiceListener()
				{
					public void serviceResolved(ServiceEvent event)
					{
						Log.i("Bonjour Discover", "Service resolved: "
								+ event.getInfo().getQualifiedName() + " port:"
								+ event.getInfo().getPort());
						
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
						} catch (IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					public void serviceAdded(ServiceEvent event)
					{
						// Required to force serviceResolved to be called again
						// (after the first search)
						jmdns.requestServiceInfo(event.getType(),
								event.getName(), 1);
						Log.i("Bonjour Discover",
								"Service added: " + event.getName());
						try
						{
							for(InetAddress host : event.getInfo().getInetAddresses())
							{
								Client client = Client.get(host);
								Formater formater = new Formater();
								formater.operation = Formater.Operation.GET;
								formater.size = 0;
								formater.type = Formater.Type.CONTACT;
								client.send(formater);
								//Socket socket = serverSocket.accept();
								// TODO receive contact list
								formater.type = Formater.Type.MESSAGE;
								ArrayList<Message> presence = new ArrayList<Message>();
								presence.add(new Message(Mime.PRESENCE, null));
								formater.setMessages(presence);
								client.send(formater);
								// TODO receive Presence message
							}
						} catch (IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			serviceInfo = ServiceInfo.create(type, "Typa", 0,
					"CSV Protocol for serverless local IM on Android");
			jmdns.registerService(serviceInfo);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		while(true)
		{
			
		}
	}

	/*
	 * protected void onProgressUpdate() {
	 * 
	 * }
	 * 
	 * protected void onPostExecute() {
	 * 
	 * }
	 */
	
	public void close()
	{
		onCancelled();
	}

	public void onCancelled()
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jmdns = null;
		}
		if(lock != null)
		{
			lock.release();
		}
	}
	
	
	public void wifiMultiCastLock(WifiManager wifi)
	{
		//WifiManager wifi = (android.net.wifi.WifiManager) getSystemService(android.content.Context.WIFI_SERVICE);
		lock = wifi.createMulticastLock("mylockthereturn");
		lock.setReferenceCounted(true);
		lock.acquire();
	}
}
