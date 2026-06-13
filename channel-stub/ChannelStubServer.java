import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChannelStubServer {

    private static final int PORT = System.getenv("PORT") != null ? Integer.parseInt(System.getenv("PORT")) : 5001;
    private static final String CRM_CALLBACK_URL = System.getenv("CRM_CALLBACK_URL") != null ? System.getenv("CRM_CALLBACK_URL") : "http://localhost:8080/api/v1/callbacks/channel";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private static final Pattern COMM_ID_PATTERN = Pattern.compile("\"communicationId\"\\s*:\\s*\"([a-fA-F0-9\\-]+)\"");

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);
        server.createContext("/api/v1/stub/send", new SendHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Channel Stub Server started on port " + PORT + ". Calling back to " + CRM_CALLBACK_URL);
    }

    static class SendHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            InputStream is = exchange.getRequestBody();
            String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("Received send request");

            Matcher matcher = COMM_ID_PATTERN.matcher(requestBody);
            if (matcher.find()) {
                String communicationId = matcher.group(1);
                String channelMessageId = "msg-" + UUID.randomUUID().toString();

                String response = "{\"channelMessageId\": \"" + channelMessageId + "\", \"success\": true}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(202, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

                // Trigger lifecycle simulation
                simulateLifecycle(communicationId, channelMessageId);
            } else {
                String response = "{\"error\": \"Missing communicationId\"}";
                exchange.sendResponseHeaders(400, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    private static void simulateLifecycle(String communicationId, String messageId) {
        // We simulate a race condition! The callback might fire before the main app commits the transaction.
        // We simulate a network drop by firing the same callback twice (idempotency test).
        
        // 1. Send DELIVERED (after 500ms)
        scheduler.schedule(() -> {
            sendWebhook(communicationId, messageId, "DELIVERED", "{}");
            // Test Idempotency: Send DELIVERED twice!
            sendWebhook(communicationId, messageId, "DELIVERED", "{}");
        }, 500, TimeUnit.MILLISECONDS);

        // 2. Send OPENED (after 1500ms) - only 50% open rate for simulation
        scheduler.schedule(() -> {
            if (Math.random() > 0.5) {
                sendWebhook(communicationId, messageId, "OPENED", "{}");

                // 3. Send CLICKED (after 3000ms) - only 50% click rate if opened
                scheduler.schedule(() -> {
                    if (Math.random() > 0.5) {
                        sendWebhook(communicationId, messageId, "CLICKED", "{}");
                        
                        // 4. Send CONVERTED (after 4500ms)
                        scheduler.schedule(() -> {
                            if (Math.random() > 0.5) {
                                sendWebhook(communicationId, messageId, "CONVERTED", "{}");
                            }
                        }, 1500, TimeUnit.MILLISECONDS);
                    }
                }, 1500, TimeUnit.MILLISECONDS);
            }
        }, 1500, TimeUnit.MILLISECONDS);
    }

    private static void sendWebhook(String commId, String msgId, String eventType, String payload) {
        try {
            String jsonBody = String.format(
                "{\"communicationId\":\"%s\",\"channelMessageId\":\"%s\",\"eventType\":\"%s\",\"payload\":%s}",
                commId, msgId, eventType, payload
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CRM_CALLBACK_URL))
                    .header("Content-Type", "application/json")
                    .header("X-API-KEY", "likhit@178926a")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Webhook sent: " + eventType + " for " + msgId + " -> Status: " + response.statusCode());
        } catch (Exception e) {
            System.err.println("Failed to send webhook: " + e.getMessage());
        }
    }
}
