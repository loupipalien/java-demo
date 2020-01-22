package com.ltchen.demo.avro.protocol;

import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.generic.GenericRequestor;
import org.apache.avro.ipc.generic.GenericResponder;
import org.apache.avro.ipc.jetty.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author: 01139983
 */
public class GenericProtocolMain {

    public static void main(String[] args) throws Exception {
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
        Protocol protocol = getProtocol();
        Transceiver transceiver = new HttpTransceiver(new URL("http://localhost:8888"));
        GenericRequestor client = new GenericRequestor(protocol, transceiver);
        System.out.println("Created client.");
        // Send
        GenericRecord record = new GenericData.Record(protocol.getType("Message"));
        record.put("from", args[0]);
        record.put("to", args[1]);
        record.put("body", args[2]);
        GenericRecord request = new GenericData.Record(protocol.getMessages().get("send").getRequest());
        request.put("message", record);
        System.out.println("Client send response: " + client.request("send", request));

        // Close
        transceiver.close();
        server.close();
    }

    private static Protocol getProtocol() {
        File file = new File("./demo-avro/src/main/avro/protocol/mail.avpr");
        try {
            return Protocol.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Server getServer() throws IOException {
        return new HttpServer(new GenericResponder(getProtocol()) {
            @Override
            public Object respond(Protocol.Message message, Object request) throws Exception {
                if (message.getName().equals("send")) {
                    System.out.println("Sending message.");
                    GenericRecord record = (GenericRecord) ((GenericRecord) request).get("message");
                    String to = record.get("to").toString();
                    String from = record.get("from").toString();
                    String body = record.get("body").toString();
                    return "Sending message to " + to + "  from " + from + " with body " + body;
                }
                throw new UnsupportedOperationException("Not this method: " + message.getName());
            }
        }, 8888);
    }
}
