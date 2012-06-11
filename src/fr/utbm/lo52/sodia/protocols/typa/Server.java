package fr.utbm.lo52.sodia.protocols.typa;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;

import android.os.AsyncTask;
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Im;
import fr.utbm.lo52.sodia.logic.Message;
import fr.utbm.lo52.sodia.logic.Mime;
import fr.utbm.lo52.sodia.protocols.Protocol;

public class Server extends AsyncTask<Void, Void, Void>
{
	private ServerSocket serverSocket;
	
	@Override
	protected Void doInBackground(Void... params)
	{
		try
		{
			serverSocket = new ServerSocket(Typa.PORT);
			while(true)
			{
				Socket socket = serverSocket.accept();
				Formater formater = new Formater(socket.getInputStream());
				switch(formater.operation)
				{
					case RET:
						switch(formater.type)
						{
						case CONTACT:
							
							break;
						case MESSAGE:
							
							break;
						}
						break;
						
					case GET:
						Formater response = new Formater();
						response.operation = Formater.Operation.RET;
						response.type = formater.type;
						boolean send = true;
						Set<Typa> protocols = Protocol.getProtocolsByType(Typa.class);
						ArrayList<Contact> contacts = new ArrayList<Contact>();
						for(Protocol protocol: protocols)
						{
							contacts.add(protocol.getMe());
						}
						switch(formater.type)
						{
						case CONTACT:
							response.setContacts(contacts);
							break;

						case MESSAGE:
							ArrayList<Message> messages = new ArrayList<Message>();
							switch (formater.getMessages().get(0).type())
							{
							case POSITION:
								messages.add(new Message(Mime.POSITION, new int[]{0,0}));//TODO get Location
								break;

							case PRESENCE:
								for(Protocol protocol: protocols)
								{
									for(Im im: protocol.getMe().getImByProtocol((new Typa()).getName()))
									{
										messages.add(new Message(Mime.PRESENCE, im.getStatus()));
									}
								}
								break;
							default : 
								send = false; 
								break;
							}
							response.setMessages(messages);
							break;
						}
						if(send)
						{
							OutputStream outputStream = socket.getOutputStream();
							outputStream.write(response.toString().getBytes());
							outputStream.flush();
							outputStream.close();
						}
						break;
				}
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return null;
	}
	
}
