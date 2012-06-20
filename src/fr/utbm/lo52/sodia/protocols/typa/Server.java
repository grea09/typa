/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utbm.lo52.sodia.protocols.typa;

import android.accounts.Account;
import android.os.AsyncTask;
import android.util.Log;
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Im;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.logic.Mime;
import fr.utbm.lo52.sodia.protocols.Protocol;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antoine
 */
public class Server extends AsyncTask<Void, Void, Void>
{
	private ServerSocket serverSocket;

	@Override
	protected Void doInBackground(Void... params)
	{
		try
		{
			serverSocket = new ServerSocket(Typa.PORT);
		}
		catch (IOException e)
		{
			Log.e(getClass().getName(), "", e);
		}
		while (true)
		{
			try
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
			} catch (IOException e)
			{
				Log.e(getClass().getName(), "", e);
			}
		}
	}
	
	@Override
	public void onCancelled()
	{
		try
		{
			if(serverSocket != null)
			{
				serverSocket.close();
			}
		}
		catch (IOException e)
		{
			Log.e(getClass().getName(), "Can't close socket", e);
		}
	}
}
