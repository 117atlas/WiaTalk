package ensp.reseau.wiatalk.models.utils;

/**
 * Created by Sim'S on 09/05/2018.
 */

public class AFile {
    protected String fileName;
    protected String filePath;

    public AFile() {
    }

    public AFile(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
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
}
