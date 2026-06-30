package com.cinema.filmaffinity.feeder;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.DeliveryMode;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.ConnectionFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonPrimitive;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FilmAffinityPublisher implements AutoCloseable {
    private final ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class,
                    (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
            .create();

    public FilmAffinityPublisher() throws Exception {
        this.factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        this.connection = factory.createConnection();
        this.connection.start();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("Review");
        this.producer = session.createProducer(topic);
        this.producer.setDeliveryMode(DeliveryMode.PERSISTENT);
    }

    public void publishObject(Object event) {
        try {
            String json = gson.toJson(event);
            TextMessage msg = session.createTextMessage(json);
            producer.send(msg);
        } catch (Exception e) {
            System.err.println("FilmAffinityPublisher.publishObject error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try { if (producer != null) producer.close(); } catch (Exception ignored) {}
        try { if (session != null) session.close(); } catch (Exception ignored) {}
        try { if (connection != null) connection.close(); } catch (Exception ignored) {}
    }
}