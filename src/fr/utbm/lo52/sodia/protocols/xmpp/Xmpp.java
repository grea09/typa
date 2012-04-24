package fr.utbm.lo52.sodia.protocols.xmpp;

import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import fr.utbm.lo52.sodia.protocols.Protocol;

import android.accounts.Account;
import android.os.AsyncTask;
import android.util.Log;

public class Xmpp extends AsyncTask<Void, Void, Void> implements Protocol
{
	
	private String type = "_presence._tcp.local.";
	private String create = "_presence._tcp.local.";
	
	private JmDNS jmdns = null;
	private ServiceListener listener = null;
	private ServiceInfo serviceInfo;
	
	@Override
	 protected Void doInBackground(Void ...voids) 
	 {
		try {
			jmdns = JmDNS.create();
			jmdns.addServiceListener(type, listener = new ServiceListener() {

				public void serviceResolved(ServiceEvent event) {
					Log.i("Bonjour Discover", "Service resolved: " + event.getInfo().getQualifiedName() + " port:" + event.getInfo().getPort());
				}

				public void serviceRemoved(ServiceEvent ev) {
					Log.i("Bonjour Discover", "Service removed: " + ev.getName());
				}

				public void serviceAdded(ServiceEvent event) {
					// Required to force serviceResolved to be called again (after the first search)
					jmdns.requestServiceInfo(event.getType(), event.getName(), 1);
					Log.i("Bonjour Discover", "Service added: " + event.getName());
				}
			});
			serviceInfo = ServiceInfo.create(create, "antoine@HTCEVO3D", 0, "plain test service from android");
			jmdns.registerService(serviceInfo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	 }
	 
/*
	 protected void onProgressUpdate() {
		 
	 }

	 protected void onPostExecute() {
		 
	 }
*/
	
	public void onCancelled() {
		if (jmdns != null) {
			if (listener != null) {
				jmdns.removeServiceListener(type, listener);
				listener = null;
			}
			jmdns.unregisterAllServices();
			try {
				jmdns.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jmdns = null;
		}
	}

	@Override
	public void connect(Account account) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(Account account, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void presence(Account account, String status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect(Account account) {
		// TODO Auto-generated method stub
		
	}

 }
