package com.example.datacollection.twitter;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterApiExample {

    public static void main(String[] args) {
        // Thiết lập các thông tin xác thực
        String apiKey = "";
        String apiSecretKey = "";
        String accessToken = "";
        String accessTokenSecret = "";

        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(apiKey, apiSecretKey);
        twitter.setOAuthAccessToken(new twitter4j.auth.AccessToken(accessToken, accessTokenSecret));

        try {
            String userId = "1228393702244134912";
            User user = twitter.showUser(userId);
            
            // Hiển thị thông tin người dùng
            System.out.println("User ID: " + user.getId());
            System.out.println("Screen Name: " + user.getScreenName());
            System.out.println("Name: " + user.getName());
            System.out.println("Location: " + user.getLocation());
            System.out.println("Description: " + user.getDescription());

        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
