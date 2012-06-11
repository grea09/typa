package fr.utbm.lo52.sodia.protocols;

import fr.utbm.lo52.sodia.logic.Contact;
import fr.utbm.lo52.sodia.logic.Message;

public interface ProtocolListener {

	public void receive(Message message);
	public void contacts(Contact[] contacts);
	
}
