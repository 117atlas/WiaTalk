package ensp.reseau.wiatalk.models.utils;

/**
 * Created by Sim'S on 09/05/2018.
 */

public class Document extends AFile{
    private String size;
    private String type;

    public Document() {
    }

    public Document(String size, String type) {
        this.size = size;
        this.type = type;
    }

    public Document(String size) {
        this.size = size;
        this.type = filePath.substring(filePath.lastIndexOf(".")+1, filePath.length());
    }

    public Document(String fileName, String filePath, String size, String type) {
        super(fileName, filePath);
        this.size = size;
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setType(){
        this.type = filePath.substring(filePath.lastIndexOf(".")+1, filePath.length());
    }
}
