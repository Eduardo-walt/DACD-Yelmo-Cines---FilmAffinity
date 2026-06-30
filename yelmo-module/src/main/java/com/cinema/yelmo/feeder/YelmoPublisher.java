package com.cinema.yelmo.feeder;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.DeliveryMode;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.ConnectionFactory;
import com.google.gson.Gson;

public class YelmoPublisher implements AutoCloseable {
    private final ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private final Gson gson = new Gson();

    public YelmoPublisher() throws Exception {
        this.factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        this.connection = factory.createConnection();
        this.connection.start();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("Movie");
        this.producer = session.createProducer(topic);
        this.producer.setDeliveryMode(DeliveryMode.PERSISTENT);
    }

    public void publishObject(Object event) {
        try {
            String json = gson.toJson(event);
            TextMessage msg = session.createTextMessage(json);
            producer.send(msg);
        } catch (Exception e) {
            System.err.println("YelmoPublisher.publishObject error: " + e.getMessage());
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
