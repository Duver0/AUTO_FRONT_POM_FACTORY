package pages;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import utils.TestIdentityUtils;

public class ConsultorioPreconditionsHelper {

    private static final String DEFAULT_API_BASE_URL = "http://localhost:3000";
    private static final int DEFAULT_CONSULTORIOS_TOTAL = 5;
    private static final String DEFAULT_AUTH_COOKIE_NAME = "auth_token";
    private static final String SIGN_IN_PATH = "/auth/signIn";
    private static final String CONSULTORIO_STATE_PATH = "/medicos/consultorio/estado/";
    private static final String RELEASE_CONSULTORIO_PATH = "/medicos/consultorio/liberar";
    private static final int HTTP_OK = 200;
    private static final int HTTP_FORBIDDEN = 403;
    private static final int HTTP_ACCEPTED = 202;
    private static final Pattern JSON_STRING_FIELD_PATTERN = Pattern.compile("\\\"%s\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"");
    private static final Map<String, String> TRACKED_TESTMAIL_MEDICOS = new ConcurrentHashMap<>();

    private final HttpClient httpClient;
    private final String apiBaseUrl;
    private final int consultoriosTotal;

    public ConsultorioPreconditionsHelper() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        this.apiBaseUrl = resolveApiBaseUrl();
        this.consultoriosTotal = resolveConsultoriosTotal();
    }

    public String buildStrongPassword() {
        return "Duver123--";
    }

    public void registerTestMedicoCredentials(String email, String password) {
        if (!TestIdentityUtils.isTestmailAddress(email) || password == null || password.isBlank()) {
            return;
        }

        TRACKED_TESTMAIL_MEDICOS.put(email, password);
    }

    public void releaseTrackedTestmailConsultorios() {
        StringBuilder failures = new StringBuilder();

        TRACKED_TESTMAIL_MEDICOS.forEach((email, password) -> {
            try {
                releaseConsultorioForMedico(email, password);
            } catch (RuntimeException exception) {
                failures.append(System.lineSeparator())
                        .append("- ")
                        .append(email)
                        .append(": ")
                        .append(exception.getMessage());
            }
        });

        if (failures.length() > 0) {
            throw new IllegalStateException("No se pudo completar la liberacion de consultorios:" + failures);
        }
    }

    public String findAvailableConsultorioId(String email, String password) {
        String token = signInAndGetToken(email, password);

        for (int index = 1; index <= consultoriosTotal; index++) {
            String consultorioId = "C" + index;
            HttpResponse<String> response = getWithBearer(CONSULTORIO_STATE_PATH + consultorioId, token);
            if (response.statusCode() != HTTP_OK) {
                continue;
            }

            String estado = extractJsonStringField(response.body(), "estado");
            if ("SinMedico".equals(estado)) {
                return consultorioId;
            }
        }

        throw new IllegalStateException("No se encontro un consultorio en estado SinMedico para esta ejecucion");
    }

    public String readAuthTokenFromCookie(WebDriver driver) {
        String cookieName = System.getenv().getOrDefault("NEXT_PUBLIC_AUTH_COOKIE_NAME", DEFAULT_AUTH_COOKIE_NAME);
        Cookie cookie = driver.manage().getCookieNamed(cookieName);
        if (cookie == null || cookie.getValue() == null || cookie.getValue().isBlank()) {
            throw new IllegalStateException("No se encontro cookie de autenticacion en el navegador");
        }
        return cookie.getValue();
    }

    public String requestAccessDeniedMessage(String token, String consultorioId) {
        HttpResponse<String> response = getWithBearer(CONSULTORIO_STATE_PATH + consultorioId, token);
        if (response.statusCode() != HTTP_FORBIDDEN) {
            throw new IllegalStateException(
                    "Se esperaba HTTP 403 para acceso denegado y se obtuvo "
                            + response.statusCode()
                            + ". Body: "
                            + response.body());
        }

        String message = extractJsonStringField(response.body(), "message");
        if (message == null || message.isBlank()) {
            throw new IllegalStateException("No fue posible extraer el mensaje de acceso denegado del backend");
        }
        return message;
    }

    private void releaseConsultorioForMedico(String email, String password) {
        String token = signInAndGetToken(email, password);
        HttpResponse<String> response = postWithBearer(RELEASE_CONSULTORIO_PATH, token);
        int status = response.statusCode();

        if (status == HTTP_ACCEPTED || status == 400 || status == 404 || status == 409) {
            TRACKED_TESTMAIL_MEDICOS.remove(email);
            return;
        }

        throw new IllegalStateException(
                "Error liberando consultorio. HTTP " + status + ". Body: " + response.body());
    }

    private String signInAndGetToken(String email, String password) {
        String payload = "{"
            + "\"email\":\"" + escapeJson(email) + "\","
                + "\"password\":\"" + escapeJson(password) + "\""
                + "}";

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiBaseUrl + SIGN_IN_PATH))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = send(request);
        if (response.statusCode() != HTTP_OK) {
            throw new IllegalStateException(
                    "No fue posible autenticar el usuario de precondicion. HTTP "
                            + response.statusCode()
                            + ". Body: "
                            + response.body());
        }

        String token = extractJsonStringField(response.body(), "token");
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("No se obtuvo token en el login de precondicion");
        }
        return token;
    }

    private HttpResponse<String> getWithBearer(String path, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + path))
                .timeout(Duration.ofSeconds(15))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        return send(request);
    }

    private HttpResponse<String> postWithBearer(String path, String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + path))
                .timeout(Duration.ofSeconds(15))
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        return send(request);
    }

    private HttpResponse<String> send(HttpRequest request) {
        IOException lastIoException = null;

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException exception) {
                lastIoException = exception;
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Ejecucion interrumpida durante precondiciones de consultorio", exception);
            }
        }

        throw new IllegalStateException("Error de red al ejecutar precondiciones de consultorio", lastIoException);
    }

    private String resolveApiBaseUrl() {
        String raw = System.getenv("NEXT_PUBLIC_API_BASE_URL");
        if (raw == null || raw.isBlank()) {
            return DEFAULT_API_BASE_URL;
        }
        return raw.endsWith("/") ? raw.substring(0, raw.length() - 1) : raw;
    }

    private int resolveConsultoriosTotal() {
        String raw = System.getenv("NEXT_PUBLIC_CONSULTORIOS_TOTAL");
        if (raw == null || raw.isBlank()) {
            return DEFAULT_CONSULTORIOS_TOTAL;
        }

        try {
            int parsed = Integer.parseInt(raw);
            return parsed > 0 ? parsed : DEFAULT_CONSULTORIOS_TOTAL;
        } catch (NumberFormatException exception) {
            return DEFAULT_CONSULTORIOS_TOTAL;
        }
    }

    private String extractJsonStringField(String json, String fieldName) {
        Pattern pattern = Pattern.compile(String.format(JSON_STRING_FIELD_PATTERN.pattern(), Pattern.quote(fieldName)));
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            return null;
        }
        return matcher.group(1);
    }

    private String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}