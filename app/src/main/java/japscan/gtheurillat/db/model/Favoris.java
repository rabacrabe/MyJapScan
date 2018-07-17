package japscan.gtheurillat.db.model;

/**
 * Created by gtheurillat on 17/07/2018.
 */

public class Favoris {
    private long id;
    private String name;
    private String url;
    private String genre;
    private String status;

    public Favoris() {super();}

    public Favoris(long id, String name, String url, String genre, String status) {
        super();
        this.id = id;
        this.name = name;
        this.url = url;
        this.genre = genre;
        this.status = status;
    }

    public Favoris(String name, String url, String genre, String status) {
        super();
        this.name = name;
        this.url = url;
        this.status = status;
        this.genre = genre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
