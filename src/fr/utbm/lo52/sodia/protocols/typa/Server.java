package fr.utbm.lo52.sodia.protocols.typa;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.AsyncTask;

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
						
						break;
					case GET:
						
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
