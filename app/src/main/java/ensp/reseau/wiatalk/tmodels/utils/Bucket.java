package ensp.reseau.wiatalk.tmodels.utils;

import java.io.Serializable;

/**
 * Created by Sim'S on 10/05/2018.
 */

public class Bucket implements Serializable{
    private String name;
    private String path;
    private String firstImageContainedPath;
    private boolean isPhoto;

    public Bucket() {
    }

    public Bucket(String name, String path, String firstImageContainedPath) {
        this.name = name;
        this.path = path;
        this.firstImageContainedPath = firstImageContainedPath;
        isPhoto = true;
    }

    public Bucket(String name, String path, String firstImageContainedPath, boolean isPhoto) {
        this.name = name;
        this.path = path;
        this.firstImageContainedPath = firstImageContainedPath;
        this.isPhoto = isPhoto;
    }

    public boolean isPhoto() {
        return isPhoto;
    }

    public void setPhoto(boolean photo) {
        isPhoto = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFirstImageContainedPath() {
        return firstImageContainedPath;
    }

    public void setFirstImageContainedPath(String firstImageContainedPath) {
        this.firstImageContainedPath = firstImageContainedPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bucket bucket = (Bucket) o;

        return path != null ? path.equals(bucket.path) : bucket.path == null;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
