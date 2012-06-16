package fr.utbm.lo52.sodia.protocols;

import android.accounts.Account;
import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Message;

public interface ProtocolListener {

	public void receive(Message message, Account account);
	public void contacts(Contact[] contacts, Account account);
	
}
