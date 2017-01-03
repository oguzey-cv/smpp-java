/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bandwidth.messaging.smsc;

import com.cloudhopper.smpp.SmppServerConfiguration;
import com.cloudhopper.smpp.SmppServerHandler;
import com.cloudhopper.smpp.SmppServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.impl.DefaultSmppServer;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.BaseBind;
import com.cloudhopper.smpp.pdu.BaseBindResp;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppProcessingException;

/**
 *
 * @author oleg
 */
public class Smsc {
    public static final String NAME = "SMSC1";
    public static final String HOST = "0.0.0.0";
    public static final int PORT = 18000;
    public static final String SYSTEMID = "VMA";
    public static final String PASSWORD = "test";
    
    private static final Logger logger = LoggerFactory.getLogger(Smsc.class);
    private DefaultSmppServer server = null;
    private SmppServerHandler handler = null;
    

    public Smsc() {
        handler = new SmppServerHandler() {

            @Override
            public void sessionBindRequested(Long sessionId, SmppSessionConfiguration session, BaseBind bindRequest) throws SmppProcessingException {
                logger.info("Requested new session: {}", session);
            }

            @Override
            public void sessionCreated(Long sessionId, SmppServerSession session, BaseBindResp preparedBindResponse) throws SmppProcessingException {
                logger.info("Session created: {}", session);
                session.serverReady(new SmscSessionHandler());
            }

            @Override
            public void sessionDestroyed(Long sessionId, SmppServerSession session) {
                logger.info("Session destroyed: {}", session);
                session.destroy();
            }
        };
        server = new DefaultSmppServer(createConfiguration(), handler);
        logger.info("Smsc created");
    }
    
    public void start() {
        logger.info("Server {} is starting", NAME);
        try {
            server.start();
        } catch (SmppChannelException e) {
            logger.info("Start error: {}", e);
        }
    }
    
    public void stop() {
        logger.info("Server {} is stoping", NAME);
        server.stop();
    }
    
    private SmppServerConfiguration createConfiguration()
    {
        SmppServerConfiguration configuration = new SmppServerConfiguration();
        configuration.setDefaultWindowSize(5);
        configuration.setMaxConnectionSize(10);
        configuration.setName(NAME);
        configuration.setHost(HOST);
        configuration.setPort(PORT);
        configuration.setConnectTimeout(100);
        configuration.setBindTimeout(100);
        configuration.setSystemId(SYSTEMID);
        return configuration;
    }
}
