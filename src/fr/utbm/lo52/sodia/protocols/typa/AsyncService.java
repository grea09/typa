package fr.utbm.lo52.sodia.protocols.typa;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsyncService extends Service
{
	private Bonjour bonjour;
	private Server server;

	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	
	private static AsyncService instance;
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
			synchronized(this)
			{
				server = new Server();
				server.execute(AsyncService.this);
				try
				{
					this.wait();
				}
				catch (InterruptedException ex)
				{
					Logger.getLogger(AsyncService.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			
			bonjour = new Bonjour(AsyncService.this);
			bonjour.connect();
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
		if(instance instanceof AsyncService)
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
			bonjour.close();
			Log.i(getClass().getName(), "Server stopped.");
		}
	}

}
