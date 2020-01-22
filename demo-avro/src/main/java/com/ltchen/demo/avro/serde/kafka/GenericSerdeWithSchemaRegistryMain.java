package com.ltchen.demo.avro.serde.kafka;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

/**
 * @author: 01139983
 */
public class GenericSerdeWithSchemaRegistryMain {
    public static void main(String[] args) throws IOException {
        String brokers = "10.202.116.45:9092,10.202.116.45:9093,10.202.116.45:9094";
        String registry = "http://10.202.77.201:8888,http://10.202.77.202:8888,http://10.202.77.203:8888";
        String topic = "com.ltchen.demo.avro.serde.User";

        produce(brokers, registry, topic);
        consume(brokers, registry, topic);
    }

    private static void produce(String brokers, String registry, String topic) throws IOException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, registry);
        KafkaProducer<String,GenericRecord> producer = new KafkaProducer<>(props);

        String key = "Alyssa";
        Schema schema = new Schema.Parser().parse(new File("./demo-avro/src/main/avro/serde/user.avsc"));
        GenericRecord value = new GenericData.Record(schema);
        value.put("name", "Alyssa");
        value.put("favorite_number", 256);
        // send
        ProducerRecord<String,GenericRecord> record = new ProducerRecord<>(topic, key, value);
        producer.send(record);
        producer.flush();
        producer.close();
    }

    private static void consume(String brokers, String registry, String topic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "com.ltchen.demo.avro.serde.User");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, registry);

        try (final KafkaConsumer<String, GenericRecord> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(topic));
            while (true) {
                final ConsumerRecords<String, GenericRecord> records = consumer.poll(100);
                for (final ConsumerRecord<String, GenericRecord> record : records) {
                    final String key = record.key();
                    final GenericRecord value = record.value();
                    System.out.printf("key = %s, value = %s%n", key, value);
                }
            }
        }
    }
}
