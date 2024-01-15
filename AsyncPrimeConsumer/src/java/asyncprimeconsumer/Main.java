/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package asyncprimeconsumer;

import java.util.Scanner;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author sarun
 */
public class Main {
    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/SimpleJMSQueue")
    private static Queue queue;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Connection connection = null;
        TextListener listener = null;
                 
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(
                        false,
                        Session.AUTO_ACKNOWLEDGE);
            listener = new TextListener();
            //Create a temporary queue that this client will listen for responses on then create a consumer
            //that consumes message from this temporary queue...for a real application a client should reuse
            //the same temp queue for each message to the server...one temp queue per client
            Queue tempDest = session.createTemporaryQueue();
            MessageConsumer responseConsumer = session.createConsumer(tempDest);
            responseConsumer.setMessageListener(listener);
            MessageProducer producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage();
            message.setJMSReplyTo(tempDest);
            connection.start();
            
            String ch = "";
            Scanner inp = new Scanner(System.in);
            
            while(true) {     
                System.out.println("Enter two numbers. Use ',' to seperate each number. To end the program press enter ");
                ch = inp.nextLine();
                if(messageVerrify(ch)){
                    message.setText(ch);
                    System.out.println("Sending message: " + message.getText());
                    producer.send(message);
                }
                else {
                    System.out.println("Format Input Error Should be : number,number ");
                }
                if (ch.equals("")) {
                        break;
                    }
            }
            
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
    }
    
    public static boolean messageVerrify(String msg){
        try{
            String[] string = msg.split(",");

            // declaring an array with the size of string
            int[] arr = new int[string.length];

            // parsing the String argument as a signed decimal
            // integer object and storing that integer into the
            // array
            for (int i = 0; i < string.length; i++) {
                arr[i] = Integer.valueOf(string[i]);
            }
            
            if(arr.length == 2){
                return true;
            }
            return false;
        }
        catch(NumberFormatException nfe){
            return false;
        }
    }
}
