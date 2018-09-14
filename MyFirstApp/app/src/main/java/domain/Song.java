package domain;

import java.util.Objects;

public class Song {
    private long id;
    private String title;
    private Artist artist;
    private String path; //make this uri

    public Song(long id, String title, Artist artist, String path) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.path = path;
    }

    public Song(String title, Artist artist, String path){
        this.title = title;
        this.artist = artist;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString(){
        return artist.getName() + " - " + title;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Song)) {
            return false;
        }
        Song song = (Song) o;
        return Objects.equals(title, song.title) &&
                Objects.equals(artist, song.artist) &&
                Objects.equals(path, song.path);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id, title, artist, path);
    }
}
