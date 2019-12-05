package model;

public class CatalogItem {
    private String uuid;
    private String movieName;
    private String desc;
    private int rating;

    public CatalogItem() {}

    public CatalogItem(String uuid, String movieName, String desc, int rating) {
        this.uuid = uuid;
        this.movieName = movieName;
        this.desc = desc;
        this.rating = rating;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
