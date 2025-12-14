import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatBotClient {

    // هذا الميثود يرسل الرسالة إلى الخادم ويستقبل الرد
    public static String getResponse(String message) {
        try {
            // تأكد من وضع رابط الـ API هنا
            URL url = new URL("https://e9fa-34-127-67-240.ngrok-free.app/chat");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // إعدادات الطلب
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // الرسالة المطلوبة
            String jsonInputString = "{\"message\": \"" + message + "\"}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // استقبل الاستجابة
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "utf-8")
            );
            StringBuilder response = new StringBuilder();
            String responseLine;

            while ((responseLine = in.readLine()) != null) {
                response.append(responseLine.trim());
            }
            in.close();

            String jsonResponse = response.toString();
            int start = jsonResponse.indexOf(":\"") + 2;
            int end = jsonResponse.lastIndexOf("\"");
            String extracted = jsonResponse.substring(start, end);

            // فك الترميز
            return java.net.URLDecoder.decode(extracted, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Could not get response from the server.";
        }
    }
}
