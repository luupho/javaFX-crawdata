package com.example.gui;

import com.example.datacollection.nftexchanges.NiftygatewayDataCollection;
import com.example.model.TrendingNFT;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class NFTScreen extends ScrollPane {

    private List<TrendingNFT> currentData;

    public NFTScreen() {
        VBox mainBox = new VBox();
        Label label = new Label("Nifty Gateway Data Collection");
        label.setStyle("-fx-font-size: 24px;");
        mainBox.getChildren().add(label);

        // Tạo một HBox để chứa nút "Fetch Data" và "Export Data"
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);

        // Thêm nút "Fetch Data"
        Button fetchDataButton = new Button("Fetch Data");
        fetchDataButton.setOnAction(event -> fetchAndDisplayNiftyGatewayData());
        buttonBox.getChildren().add(fetchDataButton);

        // Thêm nút "Export Data"
        Button exportButton = new Button("Export Data");
        exportButton.setOnAction(event -> exportData());
        buttonBox.getChildren().add(exportButton);

        // Thêm HBox chứa nút vào mainBox
        mainBox.getChildren().add(buttonBox);

        // Thêm khoảng cách giữa nút và phần còn lại của giao diện
        mainBox.setMargin(buttonBox, new Insets(10, 0, 0, 0));

        // Thêm mainBox vào ScrollPane
        setContent(mainBox);

        // Khởi tạo currentData
        currentData = new ArrayList<>();
    }

    private void fetchAndDisplayNiftyGatewayData() {
        // Kiểm tra xem đã có dữ liệu hay chưa
        if (currentData.isEmpty()) {
            NiftygatewayDataCollection dataCollector = new NiftygatewayDataCollection();
            List<TrendingNFT> niftyGatewayData = dataCollector.collectData();

            // Lưu dữ liệu vào currentData
            currentData.addAll(niftyGatewayData);

            // Hiển thị dữ liệu trên giao diện người dùng
            Platform.runLater(() -> displayDataOnUI(niftyGatewayData));
        }
    }

    private void displayDataOnUI(List<TrendingNFT> data) {
        VBox mainBox = (VBox) getContent();

        for (TrendingNFT item : data) {
            // Tạo một HBox để chứa thông tin của mỗi NFT
            HBox nftBox = new HBox();
            nftBox.setSpacing(10);
            nftBox.setPadding(new Insets(10, 0, 10, 10)); // Căn lề trái 10px

            // Hiển thị thông tin chi tiết
            VBox detailsBox = new VBox();
            detailsBox.getChildren().add(new Label("Title: " + item.getTitle()));
            detailsBox.getChildren().add(new Label("Price: " + item.getPrice()));
            detailsBox.getChildren().add(new Label("Creator: " + item.getCreator()));
            detailsBox.getChildren().add(new Label("Editions: " + item.getEditions()));

            nftBox.getChildren().add(detailsBox);

            mainBox.getChildren().add(nftBox);
        }
    }

    private void exportData() {
        if (!currentData.isEmpty()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Data");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File selectedFile = fileChooser.showSaveDialog(null);

            if (selectedFile != null) {
                // Sử dụng dữ liệu đã lưu trữ trong currentData
                saveNiftyGatewayDataToJson(currentData, selectedFile.getAbsolutePath());
            }
        }
    }

    private void saveNiftyGatewayDataToJson(List<TrendingNFT> data, String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Chuyển danh sách NFT thành định dạng JSON
            String jsonString = objectMapper.writeValueAsString(data);

            // Ghi nội dung JSON vào file
            Files.write(Paths.get(filePath), jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
