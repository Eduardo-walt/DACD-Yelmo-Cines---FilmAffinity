package com.cinema.businessunit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class BusinessUnit {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String CLIENT_ID = "business-unit";
    private static final Gson gson = new Gson();

    // Datamart en memoria: ss -> lista de eventos (payload) por topic
    private final Map<String, List<JsonObject>> movieDatamart = new ConcurrentHashMap<>();
    private final Map<String, List<JsonObject>> reviewDatamart = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        BusinessUnit bu = new BusinessUnit();

        // Ruta absoluta fija al event store, no depende del working directory
        String eventStorePath = "C:\\Universidad\\cuarto\\DACD\\DACD\\cinema-data-integration\\eventstore";
        bu.loadHistoricalEvents(eventStorePath);

        bu.subscribeRealtime();
        bu.runCli();
    }

    // 1. Carga eventos históricos desde el event store al arrancar
    private void loadHistoricalEvents(String basePath) throws IOException {
        Path base = Paths.get(basePath);
        if (!Files.exists(base)) {
            System.out.println("[BUSINESS-UNIT] No hay event store previo en " + base.toAbsolutePath());
            return;
        }
        try (Stream<Path> files = Files.walk(base)) {
            files.filter(p -> p.toString().endsWith(".events"))
                    .forEach(this::loadEventsFile);
        }
        System.out.println("[BUSINESS-UNIT] Datamart inicializado con histórico. Movies="
                + movieDatamart.values().stream().mapToInt(List::size).sum()
                + " Reviews=" + reviewDatamart.values().stream().mapToInt(List::size).sum());
    }

    private void loadEventsFile(Path file) {
        try {
            List<String> lines = Files.readAllLines(file);
            for (String line : lines) {
                if (line.isBlank()) continue;
                JsonObject event = gson.fromJson(line, JsonObject.class);
                storeInDatamart(event);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo " + file + ": " + e.getMessage());
        }
    }

    // 2. Suscripción en tiempo real (durable) a Movie y Review
    private void subscribeRealtime() throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = factory.createConnection();
        connection.setClientID(CLIENT_ID);
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic movieTopic = session.createTopic("Movie");
        Topic reviewTopic = session.createTopic("Review");

        MessageConsumer movieConsumer = session.createDurableSubscriber(movieTopic, "bu-movie-sub");
        MessageConsumer reviewConsumer = session.createDurableSubscriber(reviewTopic, "bu-review-sub");

        movieConsumer.setMessageListener(msg -> handleMessage(msg));
        reviewConsumer.setMessageListener(msg -> handleMessage(msg));

        connection.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { connection.close(); } catch (Exception ignored) {}
        }));

        System.out.println("[BUSINESS-UNIT] Suscrito en tiempo real a Movie y Review");
    }

    private void handleMessage(Message msg) {
        try {
            if (msg instanceof TextMessage tm) {
                JsonObject event = gson.fromJson(tm.getText(), JsonObject.class);
                storeInDatamart(event);
            }
        } catch (Exception e) {
            System.err.println("Error procesando mensaje en tiempo real: " + e.getMessage());
        }
    }

    private void storeInDatamart(JsonObject event) {
        if (event == null || !event.has("topic") || !event.has("ss") || !event.has("payload")) return;
        String topic = event.get("topic").getAsString();
        String ss = event.get("ss").getAsString();
        JsonObject payload = event.getAsJsonObject("payload");

        if (topic.equals("Movie")) {
            movieDatamart.computeIfAbsent(ss, k -> new ArrayList<>()).add(payload);
        } else if (topic.equals("Review")) {
            reviewDatamart.computeIfAbsent(ss, k -> new ArrayList<>()).add(payload);
        }
    }

    // 3. Funcionalidad de valor expuesta por CLI
    private void runCli() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== BUSINESS UNIT - CLI ===");
        System.out.println("Comandos: stats | reviews | sesiones | salir");

        while (true) {
            System.out.print("\n> ");
            String cmd = scanner.nextLine().trim();

            switch (cmd) {
                case "stats" -> {
                    int totalMovies = movieDatamart.values().stream().mapToInt(List::size).sum();
                    int totalReviews = reviewDatamart.values().stream().mapToInt(List::size).sum();
                    System.out.println("Sesiones en datamart: " + totalMovies);
                    System.out.println("Reseñas en datamart: " + totalReviews);
                }
                case "reviews" -> {
                    reviewDatamart.forEach((ss, list) -> {
                        System.out.println("Fuente: " + ss + " (" + list.size() + " reseñas)");
                        list.stream().limit(5).forEach(r -> System.out.println("  " + r));
                    });
                }
                case "sesiones" -> {
                    movieDatamart.forEach((ss, list) -> {
                        System.out.println("Fuente: " + ss + " (" + list.size() + " sesiones)");
                        list.stream().limit(5).forEach(m -> System.out.println("  " + m));
                    });
                }
                case "salir" -> {
                    System.out.println("Cerrando business-unit...");
                    return;
                }
                default -> System.out.println("Comando no reconocido. Usa: stats | reviews | sesiones | salir");
            }
        }
    }
}