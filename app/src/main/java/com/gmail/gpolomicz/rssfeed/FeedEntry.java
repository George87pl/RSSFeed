package com.gmail.gpolomicz.rssfeed;

public class FeedEntry {

    private String title;
    private String link;
    private String image;
    private String pubDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String author) {
        this.image = author;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public String toString() {
        return  "title='" + title + '\n' +
                ", link='" + link + '\n' +
                ", author='" + image + '\n' +
                ", pubDate='" + pubDate + '\n';
    }
}
