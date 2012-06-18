package fr.utbm.lo52.sodia.protocols.typa;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;

import android.accounts.Account;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Im;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.logic.Mime;
import fr.utbm.lo52.sodia.protocols.Protocol;

public class Server extends Service
{

	private ServerSocket serverSocket;
	private Bonjour bonjour;

	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	
	private static Server instance;
	private boolean started;

	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler
	{
		public ServiceHandler(Looper looper)
		{
			super(looper);
		}

		@Override
		public void handleMessage(android.os.Message msg)
		{
			try
			{
				bonjour = new Bonjour(Server.this);
				serverSocket = new ServerSocket(Typa.PORT);
				bonjour.connect();
				while (true)
				{
					Log.i(getClass().getSimpleName(), "Waiting for connection ...");
					Socket socket = serverSocket.accept();
					Log.i(getClass().getSimpleName(), "Connection !");
					Formater formater = new Formater(socket.getInputStream());
					Log.d(getClass().getSimpleName(), "Message :");
					Log.d(getClass().getSimpleName(), "	operation :" + formater.operation);
					Log.d(getClass().getSimpleName(), "	type :" + formater.type);
					switch (formater.operation)
					{
					case RET:
						Set<Typa> typas = Protocol
								.getProtocolsByType(Typa.class);
						switch (formater.type)
						{
						case CONTACT:
							for (Typa typa : typas)
							{
								typa.contacts((Contact[]) formater
										.getContacts().toArray());
							}
							break;
						case MESSAGE:
							for (Message message : formater.getMessages())
							{
								for (Contact contact : message.getTo())
								{
									for (Im im : contact
											.getImByProtocol((new Typa())
													.getName()))
									{
										Account account = Protocol
												.getAccount(im
														.getUserId()
														.split(""
																+ Im.USER_ID_SEPARATOR)[0]);
										if (account != null)
										{
											Protocol.get(account).receive(
													message);
										}
									}
								}
							}
							break;
						}
						break;
					case GET:
						if(Client.isConnected(socket.getInetAddress()))
						{
							Log.w(getClass().getSimpleName(), "Request from non discovered client ! Rejected");
							break;
						}
						Formater response = new Formater();
						response.operation = Formater.Operation.RET;
						response.type = formater.type;
						boolean send = true;
						Set<Typa> protocols = Protocol
								.getProtocolsByType(Typa.class);
						ArrayList<Contact> contacts = new ArrayList<Contact>();
						for (Protocol protocol : protocols)
						{
							contacts.add(protocol.getMe());
						}
						switch (formater.type)
						{
						case CONTACT:
							response.setContacts(contacts);
							break;

						case MESSAGE:
							ArrayList<Message> messages = new ArrayList<Message>();
							switch (formater.getMessages().get(0).type())
							{
							case POSITION:
								messages.add(new Message(Mime.POSITION,
										new int[]
											{ 0, 0 }));// TODO get Location
								break;

							case PRESENCE:
								for (Protocol protocol : protocols)
								{
									for (Im im : protocol.getMe()
											.getImByProtocol(
													(new Typa()).getName()))
									{
										messages.add(new Message(Mime.PRESENCE,
												im.getStatus()));
									}
								}
								break;
							default:
								send = false;
								break;
							}
							response.setMessages(messages);
							break;
						}
						if (send)
						{
							response.output = Client.get(socket.getInetAddress()).getClientSocket().getOutputStream();
							try
							{
								response.send();
							} catch (Throwable e)
							{
								Log.e(getClass().getName(), "Send Response Failed", e);
							}
						}
						break;
					}
				}
			} catch (IOException e)
			{
				Log.e(getClass().getName(), "", e);
			}
		}
	}

	@Override
	public void onCreate()
	{
		// Start up the thread running the service. Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block. We also make it
		// background priority so CPU-intensive work will not disrupt our UI.
		HandlerThread thread = new HandlerThread("ServiceStartArguments",
				Thread.MIN_PRIORITY);
		thread.start();

		// Get the HandlerThread's Looper and use it for our Handler
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if(instance instanceof Server)
		{
			started = false;
			stopSelfResult(startId);
			return Service.MODE_PRIVATE;
		}
		instance = this;
		started = true;
		
		Log.i(getClass().getName(), "Server started.");
		
		// For each start request, send a message to start a job and deliver the
		// start ID so we know which request we're stopping when we finish the
		// job
		android.os.Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		mServiceHandler.sendMessage(msg);

		// If we get killed, after returning from here, restart
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// We don't provide binding, so return null
		return null;
	}

	@Override
	public void onDestroy()
	{
		if(instance == this && started)
		{
			instance = null;
			try
			{
				bonjour.close();
				serverSocket.close();
			} catch (IOException e)
			{
				Log.e(getClass().getName(), "Can't close socket", e);
			}
			Log.i(getClass().getName(), "Server stopped.");
		}
		
		
		
	}

}
