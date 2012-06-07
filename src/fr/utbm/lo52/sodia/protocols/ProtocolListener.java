package fr.utbm.lo52.sodia.protocols;

import fr.utbm.lo52.sodia.logic.Message;

public interface ProtocolListener {

	public void receive(Message message);
	
	
}
