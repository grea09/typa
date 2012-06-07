package fr.utbm.lo52.sodia.protocols.typa.authentificator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import fr.utbm.lo52.sodia.protocols.Authentificator;
import fr.utbm.lo52.sodia.protocols.typa.Typa;

/**
 * Service to handle Account authentication. It instantiates the authenticator
 * and returns its IBinder.
 */
public class AuthenticationService extends Service {

    private Authentificator<Typa> authenticator;

    @Override
    public void onCreate() {
		authenticator = new Authentificator<Typa>(this, new Typa());
    }

    @Override
    public void onDestroy() {
     
    }

    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
