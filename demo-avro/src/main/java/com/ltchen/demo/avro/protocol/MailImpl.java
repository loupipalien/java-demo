package com.ltchen.demo.avro.protocol;

/**
 * @author: 01139983
 */
public class MailImpl implements Mail {

    @Override
    public String send(Message message) {
        System.out.println("Sending message.");
        return "Sending message to " + message.getFrom().toString() +
                "  from " + message.getTo().toString() +
                " with body " + message.getBody().toString();
    }
}
