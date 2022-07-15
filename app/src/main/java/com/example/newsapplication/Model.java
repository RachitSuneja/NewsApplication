package com.example.newsapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Model {

    // Model for the articles we need
    Data data;
    public Data getData() {
        return data;
    }

    public static class Data{
        String publishedAt , title , description , urlToImage ;

        @SerializedName("source")
        private source source;

        public String getPublishedAt() {
            return publishedAt;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getUrlToImage() {
            return urlToImage;
        }

        public source getSource() {
            return source;
        }
    }




    public static class source{

        @SerializedName("name")
        @Expose
        private String name;

        public String getName() {
            return name;
        }

    }




}
