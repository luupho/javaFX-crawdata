package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Thu thập dữ liệu từ trang web sử dụng Jsoup
        String url = "https://aivietnguyen.blogspot.com/";
        String data = fetchData(url);

        // Hiển thị dữ liệu thu thập được trong Label
        Label label = new Label(data);
        Scene scene = new Scene(label, 600, 400);

        stage.setScene(scene);
        stage.setTitle("Data Collection App");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private String fetchData(String url) {
        try {
            // Kết nối và lấy HTML từ trang web
            Document doc = Jsoup.connect(url).get();

            // Lọc dữ liệu cụ thể từ HTML
            Elements paragraphs = doc.select("p"); // Lấy tất cả các đoạn văn trong thẻ <p>

            // Tạo một StringBuilder để xây dựng dữ liệu thu thập được
            StringBuilder data = new StringBuilder();

            // Duyệt qua các đoạn văn và thêm vào StringBuilder
            for (Element paragraph : paragraphs) {
                data.append(paragraph.text()).append("\n");
            }

            return data.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error fetching data from the web.";
        }
    }
}
