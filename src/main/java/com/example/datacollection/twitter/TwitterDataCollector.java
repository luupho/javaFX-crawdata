

package com.example.datacollection.twitter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.example.datacollection.DataCollector;
import com.example.model.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TwitterDataCollector implements DataCollector{
    
    private final String username;
    private final String password;
    private final String name;
    

    public TwitterDataCollector(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
    }
	
    @Override
    public List<Tweet> collectData() {
        List<Tweet> tweetList = new ArrayList<>();

        // Set the path to your chromedriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\DELL\\eclipse-workspace\\myjavafxapp\\src\\main\\resources\\chromedriver-win64\\chromedriver.exe");

        // Create a new instance of the Chrome driver
        WebDriver driver = new ChromeDriver();

        // Navigate to the Twitter URL where you want to collect data
        driver.get("https://twitter.com/i/flow/login");
        
        performLogin(driver);

        // Wait for the dynamic content to load
        WebDriverWait wait = new WebDriverWait(driver, 60); // Timeout after 10 seconds
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.css-175oi2r.r-eqz5dr.r-16y2uox.r-1wbh5a2")));

        // Get the HTML content after the page has loaded
        String htmlContent = driver.getPageSource();

        // Parse the HTML content with Jsoup
        Document document = Jsoup.parse(htmlContent);

        // Find tweet elements using CSS selectors
        Elements tweetElements = document.select("div.css-175oi2r.r-eqz5dr.r-16y2uox.r-1wbh5a2");

        for (Element tweetElement : tweetElements) {
            String overview = tweetElement.select("span.css-1qaijid.r-bcqeeo.r-qvutc0.r-poiln3").text();
            String postTime = tweetElement.select("time").text();
            String content = tweetElement.select("div.css-1rynq56.r-8akbws.r-krxsd3.r-dnmrzs.r-1udh08x.r-bcqeeo.r-qvutc0.r-1qd0xha.r-a023e6.r-rjixqe.r-16dba41.r-bnwqim").text();
            String info = tweetElement.select("span.css-1qaijid.r-qvutc0.r-poiln3.r-n6v787.r-1cwl3u0.r-1k6nrdp.r-s1qlax").text();
            String hashtag = tweetElement.select("div.css-1qaijid.r-1re7ezh").text();

            // Process or store the extracted information as needed
            System.out.println("Overview: " + overview);
            System.out.println("Post Time: " + postTime);
            System.out.println("Content: " + content);
            System.out.println("Info: " + info);
            System.out.println("Hashtag: " + hashtag);
            System.out.println();

            // Create a Tweet object
            Tweet tweet = new Tweet(overview, postTime, content, info, hashtag);

            // Add the object to the list
            tweetList.add(tweet);
        }

        // Close the WebDriver
//        driver.quit();

        return tweetList;
    }
    
    private void performLogin(WebDriver driver) {
        try {
            driver.get("https://twitter.com/login");

            // Nhập tên đăng nhập (gmail)
            WebElement usernameInput = new WebDriverWait(driver, 20)
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='text']")));
            usernameInput.sendKeys(username);

            // Nhấn nút "Next" để chuyển đến bước nhập tên người dùng
            WebElement nextButton = new WebDriverWait(driver, 20)
            	    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.css-175oi2r.r-sdzlij.r-1phboty.r-rs99b7.r-lrvibr.r-ywje51.r-usiww2.r-13qz1uu.r-2yi16.r-1qi8awa.r-ymttw5.r-1loqt21.r-o7ynqc.r-6416eg.r-1ny4l3l")));
            nextButton.click();

            // Đợi cho trường nhập tên người dùng xuất hiện và nhập tên người dùng
            WebElement usernameOrEmailInput = new WebDriverWait(driver, 20)
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='text']")));
            usernameOrEmailInput.sendKeys(name);

            // Nhấn nút "Next" để chuyển đến bước nhập mật khẩu
            nextButton = new WebDriverWait(driver, 20)
            	    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.css-175oi2r.r-sdzlij.r-1phboty.r-rs99b7.r-lrvibr.r-19yznuf.r-64el8z.r-1dye5f7.r-1loqt21.r-o7ynqc.r-6416eg.r-1ny4l3l")));
            nextButton.click();
            
            // Đợi cho trường nhập mật khẩu xuất hiện và nhập mật khẩu
            WebElement passwordInput = new WebDriverWait(driver, 20)
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='password']")));
            passwordInput.sendKeys(password);

            // Nhấn nút "Login" để hoàn tất quá trình đăng nhập
            WebElement loginButton = new WebDriverWait(driver, 20)
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[data-testid='LoginForm_Login_Button']")));
            loginButton.click();


        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public static void main(String[] args) {

    }
}
