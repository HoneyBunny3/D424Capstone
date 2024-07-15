package com.example.d424capstone.utilities;

import com.example.d424capstone.entities.SocialPost;
import com.example.d424capstone.entities.StoreItem;
import com.example.d424capstone.entities.User;

import java.util.List;

public class CSVUtil {
    public static String generateCSVFromUsers(List<User> users) {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("ID,Name,Email,Phone,UserRole\n");
        for (User user : users) {
            csvBuilder.append(user.getUserID()).append(",");
            csvBuilder.append(user.getFirstName()).append(" ").append(user.getLastName()).append(",");
            csvBuilder.append(user.getEmail()).append(",");
            csvBuilder.append(user.getPhone()).append(",");
            csvBuilder.append(user.getRole()).append("\n");
        }
        return csvBuilder.toString();
    }

    public static String generateCSVFromStoreItems(List<StoreItem> storeItems) {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("ID,Item Name,Description,Price,Is Premium\n");
        for (StoreItem item : storeItems) {
            csvBuilder.append(item.getStoreItemID()).append(",");
            csvBuilder.append(item.getName()).append(",");
            csvBuilder.append(item.getDescription()).append(",");
            csvBuilder.append(item.getItemPrice()).append(",");
            csvBuilder.append(item.isPremium()).append("\n");
        }
        return csvBuilder.toString();
    }

    public static String generateCSVFromSocialPosts(List<SocialPost> socialPosts) {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("ID,User ID,Content,Likes\n");
        for (SocialPost post : socialPosts) {
            csvBuilder.append(post.getSocialPostID()).append(",");
            csvBuilder.append(post.getUserID()).append(",");
            csvBuilder.append(post.getContent()).append(",");
            csvBuilder.append(post.getLikes()).append("\n");
        }
        return csvBuilder.toString();
    }
}