package com.example.gui;

import com.example.datacollection.twitter.TwitterDataCollector;
import com.example.model.Tweet;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Pair;

import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;

public class TwitterScreen extends VBox {

    private ListView<Tweet> tweetsListView;
    private TextField username;
    private TextField name;
    private PasswordField password;

    public TwitterScreen() {
        // Label cho tiêu đề màn hình
        Label label = new Label("Twitter Data Collector");
        label.setFont(new Font(24)); // Đặt kích thước chữ

        // ListView để hiển thị tweets
        tweetsListView = new ListView<>();
        tweetsListView.setPrefHeight(400); // Đặt chiều cao của ListView
        tweetsListView.setCellFactory(param -> new ListCell<Tweet>() {
            @Override
            protected void updateItem(Tweet item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getContent() == null) {
                    setText(null);
                } else {
                    setText(item.getContent());
                    setOnMouseClicked(event -> showTweetDetails(item));
                }
            }
        });

        // Button để kích hoạt thu thập dữ liệu từ Twitter
        Button fetchDataButton = new Button("Fetch Twitter Data");
        fetchDataButton.setOnAction(event -> {
            // Hiển thị hộp thoại đăng nhập
            showLoginDialog(() -> fetchTwitterData());
        });

        // Thêm các thành phần vào VBox
        getChildren().addAll(label, tweetsListView, fetchDataButton);

        // Căn giữa VBox
        setAlignment(Pos.CENTER);
        setSpacing(20); // Đặt khoảng cách giữa các thành phần
        setPadding(new javafx.geometry.Insets(20)); // Đặt độ lệch của VBox

        // Mặc định, không thu thập dữ liệu khi màn hình được tạo
        
     // Thêm nút xuất dữ liệu
        Button exportDataButton = new Button("Export Data");
        exportDataButton.setOnAction(event -> exportData());
        getChildren().add(exportDataButton);

    }
    
 // Phương thức xử lý sự kiện khi nút xuất dữ liệu được nhấn
    private void exportData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            List<Tweet> tweetsToExport = tweetsListView.getItems();
            saveTweetsToJson(tweetsToExport, selectedFile.getAbsolutePath());
        }
    }
    
 // Phương thức lưu tweets vào file JSON
    private void saveTweetsToJson(List<Tweet> tweets, String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Chuyển danh sách tweets thành định dạng JSON
            String jsonString = objectMapper.writeValueAsString(tweets);

            // Ghi nội dung JSON vào file
            Files.write(Paths.get(filePath), jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchTwitterData() {
        TwitterDataCollector dataCollector = new TwitterDataCollector(username.getText(), name.getText(), password.getText());
        List<Tweet> tweetsWithHashtags = dataCollector.collectData();
        System.out.println("check data: " + tweetsWithHashtags);

        // Hiển thị dữ liệu trên ListView
        ObservableList<Tweet> observableTweets = FXCollections.observableArrayList(tweetsWithHashtags);
        tweetsListView.setItems(observableTweets);
    }

    private void showLoginDialog(Runnable onSuccess) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login to Twitter");
        dialog.setHeaderText("Please enter your Twitter credentials.");

        // Set the button types
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username, name, and password labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        username = new TextField();
        username.setPromptText("Username");
        name = new TextField();  // Thêm trường "Name"
        name.setPromptText("Name");
        password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(name, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(password, 1, 2);

        // Enable/Disable login button depending on whether a username was entered.
        javafx.scene.Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> {
            username.requestFocus();
            username.selectAll();
        });

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(credentials -> {
            // Execute onSuccess callback
            onSuccess.run();
        });
    }

    private void showTweetDetails(Tweet tweet) {
        // Hiển thị thông tin chi tiết của tweet
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Tweet Details");
        dialog.setHeaderText("Details for the selected tweet:");

        // Set the button types
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        // Create a VBox to hold the tweet details
        VBox tweetDetails = new VBox();
        tweetDetails.getChildren().add(new Label("Overview: " + tweet.getOverview()));
        tweetDetails.getChildren().add(new Label("Post Time: " + tweet.getPostTime()));
        tweetDetails.getChildren().add(new Label("Content: " + tweet.getContent()));
        tweetDetails.getChildren().add(new Label("Info: " + tweet.getInfo()));
        tweetDetails.getChildren().add(new Label("Hashtag: " + tweet.getHashtag()));

        dialog.getDialogPane().setContent(tweetDetails);

        // Show the dialog
        dialog.showAndWait();
    }
}
