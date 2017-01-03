/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bandwidth.messaging;

import com.bandwidth.messaging.smsc.Smsc;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author oleg
 */
public class Main {
    
    public static void main (String [] args)
    {
        Smsc smsc = new Smsc();
        smsc.start();
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
        smsc.stop();
    }
    
}
