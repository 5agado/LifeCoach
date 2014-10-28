package util;

import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class SOAPLoggingHandler implements SOAPHandler<SOAPMessageContext> {
	private final static Logger LOGGER = Logger
			.getLogger(SOAPLoggingHandler.class.getName());

	public SOAPLoggingHandler() {
		super();
		LOGGER.log(Level.INFO, "Init Logging Handler");
	}

	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	@Override
	public boolean handleMessage(SOAPMessageContext smc) {
		log(smc);
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext smc) {
		log(smc);
		return false;
	}

	@Override
	public void close(MessageContext context) {

	}

	/*
	 * Check the MESSAGE_OUTBOUND_PROPERTY in the context to see if this is an
	 * outgoing or incoming message. Write a brief message to the print stream
	 * and output the message. The writeTo() method can throw SOAPException or
	 * IOException
	 */
	private void log(SOAPMessageContext smc) {
		Boolean outboundProperty = (Boolean) smc
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (outboundProperty.booleanValue()) {
			LOGGER.log(Level.INFO, "\nOutbound message:");
		} else {
			LOGGER.log(Level.INFO, "\nInbound message:");
		}

		SOAPMessage message = smc.getMessage();
		try {
			LOGGER.log(Level.INFO, message.getContentDescription());
		} catch (Exception e) {
			LOGGER.log(Level.INFO, "Exception in handler", e);
		}
	}
}
