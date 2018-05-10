package ensp.reseau.wiatalk.ui.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.net.URI;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.ui.activities.AudiosActivity;
import ensp.reseau.wiatalk.ui.activities.DiscussionActivity;
import ensp.reseau.wiatalk.ui.activities.DocumentsActivity;
import ensp.reseau.wiatalk.ui.activities.GalleryBucketActivity;
import ensp.reseau.wiatalk.ui.activities.PhotosActivity;
import ensp.reseau.wiatalk.ui.adapters.FileChooserPhotoAdapter;
import ensp.reseau.wiatalk.ui.adapters.ICameraHandler;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileChooserBottomFragment extends BottomSheetDialogFragment {

    private ImageButton close;
    private RecyclerView photos;
    private TextView camera;
    private TextView gallery;
    private TextView video;
    private TextView audio;
    private TextView document;

    public FileChooserBottomFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.file_chooser_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        close = view.findViewById(R.id.close_file_chooser);
        photos = view.findViewById(R.id.photos_list);
        camera = view.findViewById(R.id.camera);
        gallery = view.findViewById(R.id.gallery);
        video = view.findViewById(R.id.video);
        audio = view.findViewById(R.id.audio);
        document = view.findViewById(R.id.document);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.camera:{
                        dismiss();
                        ((ICameraHandler)getActivity()).cameraHandler(ICameraHandler.TYPE_PHOTO);
                    } break;
                    case R.id.gallery:{
                        dismiss();
                        Intent intent = new Intent(getContext(), GalleryBucketActivity.class);
                        getActivity().startActivityForResult(intent, U.SELECT_GALLERY_REQ_CODES);
                    } break;
                    case R.id.video:{
                        dismiss();
                        ((ICameraHandler)getActivity()).cameraHandler(ICameraHandler.TYPE_VIDEO);
                    } break;
                    case R.id.audio:{
                        Intent intent = new Intent(getContext(), AudiosActivity.class);
                        getActivity().startActivityForResult(intent, U.SELECT_AUDIOS_REQ_CODES);
                    } break;
                    case R.id.document:{
                        Intent intent = new Intent(getContext(), DocumentsActivity.class);
                        getActivity().startActivityForResult(intent, U.SELECT_DOCS_REQ_CODES);
                    } break;
                }
            }
        };
        camera.setOnClickListener(listener);
        gallery.setOnClickListener(listener);
        video.setOnClickListener(listener);
        audio.setOnClickListener(listener);
        document
                .setOnClickListener(listener);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        FileChooserPhotoAdapter adapter = new FileChooserPhotoAdapter(getContext());
        photos.setLayoutManager(manager);
        photos.setAdapter(adapter);
        adapter.setPhotos(null);
    }



}
