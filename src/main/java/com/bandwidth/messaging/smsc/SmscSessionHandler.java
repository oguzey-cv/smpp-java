/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bandwidth.messaging.smsc;

import com.cloudhopper.smpp.PduAsyncResponse;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppSessionListener;
import com.cloudhopper.smpp.pdu.Pdu;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.cloudhopper.smpp.pdu.UnbindResp;
import com.cloudhopper.smpp.type.RecoverablePduException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;
import java.nio.channels.ClosedChannelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oleg
 */
public class SmscSessionHandler implements SmppSessionListener {
    
    private final Logger logger;
    private int counter = 0;

    public SmscSessionHandler() {
        this(LoggerFactory.getLogger(SmscSessionHandler.class));
    }

    public SmscSessionHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public String lookupResultMessage(int commandStatus) {
        return null;
    }

    @Override
    public String lookupTlvTagName(short tag) {
        return null;
    }

    @Override
    public void fireChannelUnexpectedlyClosed() {
        logger.info("Default handling is to discard an unexpected channel closed");
    }

    @Override
    public PduResponse firePduRequestReceived(PduRequest pduRequest) {
        logger.warn("Handle request PDU: {}", pduRequest);
        PduResponse response = null;
        int cmdId = pduRequest.getCommandId();
        switch (cmdId) {
            case SmppConstants.CMD_ID_UNBIND:
                response = new UnbindResp();
                response.setSequenceNumber(pduRequest.getSequenceNumber());
                break;
            case SmppConstants.CMD_ID_SUBMIT_SM:
                response = new SubmitSmResp();
                response.setSequenceNumber(pduRequest.getSequenceNumber());
                ((SubmitSmResp)response).setMessageId("hello" + counter);
                counter++;
                break;
            default:
                logger.warn("Received unknown request PDU");
                response = null;
                break;
        }
        return response;
    }

    @Override
    public void fireExpectedPduResponseReceived(PduAsyncResponse pduAsyncResponse) {
        logger.warn("Default handling is to discard expected response PDU: {}", pduAsyncResponse);
    }

    @Override
    public void fireUnexpectedPduResponseReceived(PduResponse pduResponse) {
        logger.warn("Default handling is to discard unexpected response PDU: {}", pduResponse);
    }

    @Override
    public void fireUnrecoverablePduException(UnrecoverablePduException e) {
        logger.warn("Default handling is to discard a unrecoverable exception:", e);
    }

    @Override
    public void fireRecoverablePduException(RecoverablePduException e) {
        logger.warn("Default handling is to discard a recoverable exception:", e);
    }

    @Override
    public void fireUnknownThrowable(Throwable t) {
        if (t instanceof ClosedChannelException) {
            logger.warn("Unknown throwable received, but it was a ClosedChannelException, calling fireChannelUnexpectedlyClosed instead");
            fireChannelUnexpectedlyClosed();
        } else {
            logger.warn("Default handling is to discard an unknown throwable:", t);
        }
    }

    @Override
    public void firePduRequestExpired(PduRequest pduRequest) {
        logger.warn("Default handling is to discard expired request PDU: {}", pduRequest);
    }

    @Override
    public boolean firePduReceived(Pdu pdu) {
        // default handling is to accept pdu for processing up chain
        return true;
    }

    @Override
    public boolean firePduDispatch(Pdu pdu) {
        // default handling is to accept pdu for processing up chain
        return true;
    }
    
}
