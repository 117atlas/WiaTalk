package ensp.reseau.wiatalk.models.utils;

/**
 * Created by Sim'S on 09/05/2018.
 */

public class Audio {
    private String fileName;
    private String filePath;
    private String title;
    private String artist;
    private String length;

    public Audio() {
    }

    public Audio(String fileName, String filePath, String title, String artist, String length) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.title = title;
        this.artist = artist;
        this.length = length;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
