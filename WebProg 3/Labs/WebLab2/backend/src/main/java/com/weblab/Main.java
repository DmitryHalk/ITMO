package com.weblab;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fastcgi.FCGIInterface;


public class Main {

    public static void main(String[] args) throws ValidationException {
        var fcgiIntarface = new FCGIInterface();
        while (fcgiIntarface.FCGIaccept() >= 0) {
            try {

                long startTime = System.nanoTime();

                // Получаем текущее время
                LocalDateTime currentTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedTime = currentTime.format(formatter);

                var queryParams = System.getProperties().getProperty("QUERY_STRING");
                var params = new Params(queryParams);


                var result = calculate(params.getX(), params.getY(), params.getR());

                // Расчёт времени выполнения
                long endTime = System.nanoTime();
                long duration = (endTime - startTime) / 1_000_000; // Время выполнения в наносекундах

                var json = String.format(RESULT_JSON, result, formattedTime, duration);
                var response = String.format(HTTP_RESPONSE, json.getBytes(StandardCharsets.UTF_8).length + 2, json);
                System.out.println(response);
            } catch (ValidationException e) {
                var json = String.format(ERROR_JSON, e.getMessage());
                var response = String.format(HTTP_ERROR, json.getBytes(StandardCharsets.UTF_8).length + 2, json);
                System.out.println(response);
            }
        }

    }

    private static boolean calculate(float x, float y, float r) {
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
               Status: 200 OK
               Content-Type: aplication/json
               Content-Lenght: %d
               
               %s
               """;

    private static final String HTTP_ERROR = """
            Status: 400 Bad Request
            Content-Type: application/json
            Content-Length: %d
            
            %s
            """;
    private static final String RESULT_JSON = """
            {
                "result": %b,
                "currentTime": "%s",
                "executionTimeMs": %d
            }
            """;
    private static final String ERROR_JSON = """
            {
                "reason": "%s"
            }
            """;

}

