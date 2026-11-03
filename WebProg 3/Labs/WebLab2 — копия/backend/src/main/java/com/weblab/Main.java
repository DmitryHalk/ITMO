package com.weblab;

import java.nio.charset.StandardCharsets;
import com.fastcgi.FCGIInterface;

public class Main {

    public static void main(String[] args) {
        FCGIInterface fcgi_intrfc = new FCGIInterface();

        while (fcgi_intrfc.FCGIaccept() >= 0) {
            try {
                String query = System.getProperty("QUERY_STRING");

                if (query == null || query.trim().isEmpty()) {
                    throw new ValidationException("Параметры отсутствуют");
                }

                Params params = new Params(query);
                var result = calc(params.getX(), params.getY(), params.getR());

                var json = String.format(RESULT_JSON, result);
                var response = String.format(HTTP_RESPONSE, json.getBytes(StandardCharsets.UTF_8).length, json);

                System.out.print(response);
                System.out.flush();

            } catch (ValidationException e) {
                String safeMessage = e.getMessage() != null ? e.getMessage().replace("\"", "\\\"") : "Error";
                var json = String.format(ERROR_JSON, safeMessage);
                var response = String.format(HTTP_ERROR, json.getBytes(StandardCharsets.UTF_8).length, json);

                System.out.print(response);
                System.out.flush();

            } catch (Exception e) {
                var json = String.format(ERROR_JSON, "Внутренняя ошибка сервера");
                var response = String.format(HTTP_ERROR, json.getBytes(StandardCharsets.UTF_8).length, json);

                System.out.print(response);
                System.out.flush();
            }
        }
    }

    public static boolean calc(float x, float y, float r) {
        if (x == -5 || x == -4 || x == -3 || x == -2 || x == -1 || x == 0 || x == 1 || x == 2 || x == 3) {
            if (x >= 0 && y >= 0 && x * x + y * y <= r * r) {
                return true;
            } else if (x <= 0 && y >= 0 && y <= x + r) {
                return true;
            } else if (x <= 0 && y <= 0 && x >= -r / 2 && y >= -r) {
                return true;
            }
        }
        return false;
    }

    private static final String HTTP_RESPONSE = """
               Status: 200 OK\r
               Content-Type: application/json\r
               Content-Length: %d\r
               \r
               %s""";

    private static final String HTTP_ERROR = """
            Status: 400 Bad Request\r
            Content-Type: application/json\r
            Content-Length: %d\r
            \r
            %s""";

    private static final String RESULT_JSON = """
            {
                "result": %b
            }""";

    private static final String ERROR_JSON = """
            {
                "reason": "%s"
            }""";
}