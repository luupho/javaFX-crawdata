package com.example.datacollection.nftexchanges;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.datacollection.DataCollector;
import com.example.model.TrendingNFT;

public class NiftygatewayDataCollection implements DataCollector<TrendingNFT> {

    @Override
    public List<TrendingNFT> collectData() {
        List<TrendingNFT> nftList = new ArrayList<>();

        // Set the path to your chromedriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\DELL\\eclipse-workspace\\myjavafxapp\\src\\main\\resources\\chromedriver-win64\\chromedriver.exe");

        // Create a new instance of the Chrome driver
        WebDriver driver = new ChromeDriver();
        
       

        // Navigate to the URL where you want to collect data
        driver.get("https://www.niftygateway.com/marketplace?sortBy=recent&trending=true");
        
        // Wait for the dynamic content to load
        WebDriverWait wait = new WebDriverWait(driver, 10); // Timeout after 10 seconds
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.MuiBox-root.css-hlh9rm")));

        // Wait for the dynamic content to load (you might need to adjust the waiting time)
        // Use WebDriverWait as shown in your previous examples

        // Get the HTML content after the page has loaded
        String htmlContent = driver.getPageSource();
        System.out.println("page: " + htmlContent);

        // Parse the HTML content with Jsoup
        Document document = Jsoup.parse(htmlContent);

        // Find all elements with the specified CSS class
        Elements nftElements = document.select("div.MuiBox-root.css-hlh9rm");

        // Iterate through the elements and extract information
        for (Element nftElement : nftElements) {
            String title = nftElement.select("p.MuiTypography-root.MuiTypography-body1.MuiTypography-noWrap.css-1r0io98").attr("title");
            String price = nftElement.select("p.MuiTypography-root.MuiTypography-body1.css-1fiixjq span").text();
            String creator = nftElement.select("div.MuiBox-root.css-1jgs0cu div.css-jtxebx p.MuiTypography-root.MuiTypography-body1.css-orcg7x + p.MuiTypography-root.MuiTypography-body1.css-1sxsghr a").text();
            String editions = nftElement.select("div.MuiBox-root.css-fygj2q div.css-jtxebx p.MuiTypography-root.MuiTypography-body1.css-orcg7x + p.MuiTypography-root.MuiTypography-body1.css-1sxsghr").text();
         // Process or store the extracted information as needed
            System.out.println("Title: " + title);
            System.out.println("Price: " + price);
            System.out.println("Creator: " + creator);
            System.out.println("Editions: " + editions);
            System.out.println();
            // Create a TrendingNFT object
            TrendingNFT trendingNFT = new TrendingNFT(title, price, creator, editions);

            // Add the object to the list
            nftList.add(trendingNFT);
        }

        // Close the WebDriver
        driver.quit();

        return nftList;
    }
    
    public static void main(String[] args) {
    	NiftygatewayDataCollection a = new NiftygatewayDataCollection();
    	List<TrendingNFT> niftyGatewayData = a.collectData();
    	System.out.println(niftyGatewayData);
	}
}
