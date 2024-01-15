/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package primecntprovider;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author sarun
 */
public class TextListener implements MessageListener {
    private MessageProducer replyProducer;
    private Session session;
    
    public TextListener(Session session) {
              
        this.session = session;
        try {
            replyProducer = session.createProducer(null);
        } catch (JMSException ex) {
            Logger.getLogger(TextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void onMessage(Message message) {
        TextMessage msg = null;
        PrimeCountMachine primeCalculotor = new PrimeCountMachine(); 

        try {
            if (message instanceof TextMessage) {
                msg = (TextMessage) message;
                System.out.println("Reading message: " + msg.getText());
            } else {
                System.err.println("Message is not a TextMessage");
            }
            //split the message
            int[] arr = splitMessage(msg.getText());
            int cntPrime = primeCalculotor.countPrime(arr[0],arr[1]);
            String result = "The number of primes between "+String.valueOf(arr[0])+" and "+String.valueOf(arr[1])+" is "+String.valueOf(cntPrime);
            // set up the reply message 
            TextMessage response = session.createTextMessage(result); 
            System.out.println("sending message : " + response.getText());
            replyProducer.send(message.getJMSReplyTo(), response);
        } catch (JMSException e) {
            System.err.println("JMSException in onMessage(): " + e.toString());
        } catch (Throwable t) {
            System.err.println("Exception in onMessage():" + t.getMessage());
        }
        
    }
    
    public int[] splitMessage(String msg){
        String[] string = msg.split(",");
 
        // declaring an array with the size of string
        int[] arr = new int[string.length];
 
        // parsing the String argument as a signed decimal
        // integer object and storing that integer into the
        // array
        for (int i = 0; i < string.length; i++) {
            arr[i] = Integer.valueOf(string[i]);
        }
        
        return arr;
    }

}
