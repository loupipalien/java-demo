package com.ltchen.demo.avro.protocol;

import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.netty.NettyServer;
import org.apache.avro.ipc.netty.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.ipc.specific.SpecificResponder;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author: 01139983
 */
public class SpecificProtocolMain {

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: <to> <from> <body>");
            System.exit(1);
        }

        // Server
        System.out.println("Starting server.");
        Server server = getServer();
        server.start();
        System.out.println("Started server.");

        // Client
        Transceiver transceiver = new NettyTransceiver(new InetSocketAddress(65111));
        Mail client = SpecificRequestor.getClient(Mail.class, transceiver);
        System.out.println("Created client.");
        // Send
        Message message = new Message(args[0], args[1], args[2]);
        System.out.println("Client send response: " + client.send(message));

        // Close
        transceiver.close();
        server.close();
    }

    private static Server getServer() {
        return new NettyServer(new SpecificResponder(Mail.class, new MailImpl()), new InetSocketAddress(65111));
    }

}
