package com.application.quizly;

public class Quiz {
    private String id;

    private String title;
    private String category;
    private String description;
    private String imageUrl;

    public Quiz() {}

    public Quiz(String title,String category, String description, String imageUrl) {
        this.setTitle(title);
        this.setCategory(category);
        this.setDescription(description);
        this.setImageUrl(imageUrl);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
