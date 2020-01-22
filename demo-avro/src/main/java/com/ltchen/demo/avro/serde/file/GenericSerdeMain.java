package com.ltchen.demo.avro.serde.file;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;

import java.io.File;
import java.io.IOException;

/**
 * @author: 01139983
 */
public class GenericSerdeMain {

    public static void main(String[] args) throws IOException {
        Schema schema = new Schema.Parser().parse(new File("./demo-avro/src/main/avro/serde/user.avsc"));
        File file = new File(GenericSerdeMain.class.getResource("").getPath() + "generic.avro");

        produce(schema, file);
        consume(schema, file);
    }

    private static void produce(Schema schema, File file) throws IOException {
        GenericRecord user1 = new GenericData.Record(schema);
        user1.put("name", "Alyssa");
        user1.put("favorite_number", 256);
        // Leave favorite color null

        GenericRecord user2 = new GenericData.Record(schema);
        user2.put("name", "Ben");
        user2.put("favorite_number", 7);
        user2.put("favorite_color", "red");

        // Serialize user1, user2 and user3 to disk
        DatumWriter<GenericRecord> userDatumWriter = new GenericDatumWriter<>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(userDatumWriter);
        dataFileWriter.create(schema, file);
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.close();
    }

    private static void consume(Schema schema, File file) throws IOException {
        // Deserialize Users from disk
        DatumReader<GenericRecord> userDatumReader = new GenericDatumReader<>(schema);
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(file, userDatumReader);
        GenericRecord record = null;
        while (dataFileReader.hasNext()) {
            // Reuse user object by passing it to next(). This saves us from
            // allocating and garbage collecting many objects for files with
            // many items.
            record = dataFileReader.next(record);
            System.out.println(record);
        }
    }
}
