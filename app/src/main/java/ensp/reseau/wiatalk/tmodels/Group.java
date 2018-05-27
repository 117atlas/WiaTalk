package ensp.reseau.wiatalk.tmodels;

import android.os.SystemClock;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sim'S on 14/05/2018.
 */

public class Group implements Serializable{
    public static final int TYPE_IB = 0;
    public static final int TYPE_GROUP = 1;

    private String id;
    private String nom;
    private int type;
    private String pp;
    private long creationDate;
    private String creatorId;

    public Group() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (id != null ? !id.equals(group.id) : group.id != null) return false;
        return nom != null ? nom.equals(group.nom) : group.nom == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nom != null ? nom.hashCode() : 0);
        return result;
    }

    public static ArrayList<Group> randomGroups(int size){
        if (size<=0) return null;
        ArrayList<Group> groups = new ArrayList<>();
        for (int i=0; i<size; i++){
            Group group = new Group();
            group.setId("0");
            group.setCreationDate(SystemClock.currentThreadTimeMillis());
            group.setCreatorId("1");
            group.setNom("Groupe " + i);
            int randpp = (int) Math.round(Math.random()*10);
            group.setPp(randpp>5?null:"pp"+(randpp%5)+".jpg");
            group.setType(TYPE_GROUP);
            groups.add(group);
        }
        return groups;
    }

}
