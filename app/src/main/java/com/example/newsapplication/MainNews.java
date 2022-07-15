package com.example.newsapplication;

import java.util.ArrayList;

public class MainNews {
    // Model for the Response Object from Api Call

    // Variables
    private String status , totalresults;
    private ArrayList<Model.Data> articles;

    // Constructor
    public MainNews(String status, String totalresults, ArrayList<Model.Data> articles) {
        this.status = status;
        this.totalresults = totalresults;
        this.articles = articles;
    }

    // Getters And Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalresults() {
        return totalresults;
    }

    public void setTotalresults(String totalresults) {
        this.totalresults = totalresults;
    }

    public ArrayList<Model.Data> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Model.Data> articles) {
        this.articles = articles;
    }
}
