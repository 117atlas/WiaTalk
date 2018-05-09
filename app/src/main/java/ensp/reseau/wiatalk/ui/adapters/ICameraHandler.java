package ensp.reseau.wiatalk.ui.adapters;

/**
 * Created by Sim'S on 09/05/2018.
 */

public interface ICameraHandler {
    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_VIDEO = 2;
    public void cameraHandler(int type);
}
