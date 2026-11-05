import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class CatalogoLocalServer {
    public static void main(String[] args) throws Exception {
        int port = 8081;
        String host = "127.0.0.1"; // Seguro por padrão: acesso somente local.
        for (int i = 0; i < args.length; i += 2) {
            if (i + 1 >= args.length) {
                throw new IllegalArgumentException("Uso: [--port 8081] [--host 127.0.0.1]");
            }
            switch (args[i]) {
                case "--port" -> port = Integer.parseInt(args[i + 1]);
                case "--host" -> host = args[i + 1];
                default -> throw new IllegalArgumentException("Argumento desconhecido: " + args[i]);
            }
        }
        byte[] feriados;
        try (var resource = CatalogoLocalServer.class.getResourceAsStream("/feriados.json")) {
            if (resource == null) {
                throw new IllegalStateException("Recurso feriados.json não foi encontrado no classpath.");
            }
            feriados = resource.readAllBytes();
        }
        var server = HttpServer.create(new InetSocketAddress(host, port), 0);
        server.createContext("/", exchange -> {
            try (exchange) {
                int status;
                byte[] body;
                if (!exchange.getRequestURI().toString().equals("/feriados")) {
                    status = 404;
                    body = "{\"error\":\"rota desconhecida\"}".getBytes(StandardCharsets.UTF_8);
                } else if (!exchange.getRequestMethod().equals("GET")) {
                    status = 405;
                    exchange.getResponseHeaders().set("Allow", "GET");
                    body = "{\"error\":\"use GET\"}".getBytes(StandardCharsets.UTF_8);
                } else {
                    status = 200;
                    body = feriados;
                }
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
                exchange.sendResponseHeaders(status, body.length);
                exchange.getResponseBody().write(body);
                System.out.println("HTTP Request: " + exchange.getRequestMethod() + " " + exchange.getRequestURI()
                        + " -> HTTP Response: " + status);
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.stop(0)));
        server.start();
        System.out.println("Estoque Local: http://" + host + ":" + port + "/feriados (Ctrl+C encerra)");
    }
}
