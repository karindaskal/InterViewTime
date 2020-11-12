package com.example.android2projectnew.Module;

public class Article {

    private String articleImage;
    private String nameArticle;
    private String subjectArticle;
    private String articleLink;

    public Article(String articleImage, String nameArticle, String subjectArticle, String articleLink) {
        this.articleImage = articleImage;
        this.nameArticle = nameArticle;
        this.subjectArticle = subjectArticle;
        this.articleLink = articleLink;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }

    public String getNameArticle() {
        return nameArticle;
    }

    public void setNameArticle(String nameArticle) {
        this.nameArticle = nameArticle;
    }

    public String getSubjectArticle() {
        return subjectArticle;
    }

    public void setSubjectArticle(String subjectArticle) {
        this.subjectArticle = subjectArticle;
    }
}
