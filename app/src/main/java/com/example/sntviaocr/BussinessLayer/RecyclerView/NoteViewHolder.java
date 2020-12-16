package com.example.sntviaocr.BussinessLayer.RecyclerView;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sntviaocr.ApplicationLayer.BasicComponents.BasicActivityClass;
import com.example.sntviaocr.BussinessLayer.TTPDF.TextToPdf;
import com.example.sntviaocr.R;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.io.File;

public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static boolean remove_card = true;
    private int Note_id;
    private String image_path;
    private String image_path_thumbnail;
    private final TextView title;
    //Used in old recycler view theme
    private String content;
    private final ImageView imageView;
    private final TextView noteTakingDateTime;
    private final Button deleteButton;
    private final Button copyButton;
    private final Button shareButton;
    private final Button downloadButton;
    private final Button speakUpButton;
    private final Button pdfButton;
    private final CoordinatorLayout container;
    MaterialButtonToggleGroup materialButtonToggleGroup;
    MaterialCardView materialCardView;
    public static boolean speaking = false;
    public String viewHolderFileNames;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title_of_recycler_view);
//      Used in old recycler view theme
//      content = itemView.findViewById(R.id.content_of_recycler_view);
        imageView = itemView.findViewById(R.id.image_of_recycler_view);
        noteTakingDateTime = itemView.findViewById(R.id.date_of_note_taken_recycler_view_item);
        deleteButton = itemView.findViewById(R.id.note_recycler_view_item_delete);
        copyButton = itemView.findViewById(R.id.note_recycler_view_item_copy);
        shareButton = itemView.findViewById(R.id.note_recycler_view_item_share);
        materialButtonToggleGroup = itemView.findViewById(R.id.note_recycler_view_item_toggle_button_group);
        materialCardView = itemView.findViewById(R.id.card_content_layout);
        downloadButton = itemView.findViewById(R.id.note_recycler_view_item_download_audio);
        speakUpButton = itemView.findViewById(R.id.note_recycler_view_item_speak_up);
        pdfButton = itemView.findViewById(R.id.note_recycler_view_item_download_pdf);

        speakUpButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        copyButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        downloadButton.setOnClickListener(this);
        pdfButton.setOnClickListener(this);
        materialCardView.setOnClickListener(this);

        // Swipe and Dismiss
        container = itemView.findViewById(R.id.card_container);
    }


    private static void onDragStateChanged(int state, MaterialCardView cardContentLayout) {
        switch (state) {
            case SwipeDismissBehavior.STATE_DRAGGING:
            case SwipeDismissBehavior.STATE_SETTLING:
                cardContentLayout.setDragged(true);
                break;
            case SwipeDismissBehavior.STATE_IDLE:
                cardContentLayout.setDragged(false);
                break;
            default: break;
        }
    }

    private static void resetCard(MaterialCardView cardContentLayout) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) cardContentLayout
                .getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        cardContentLayout.setAlpha(1.0f);
        cardContentLayout.requestLayout();
        remove_card = false;
    }

    public void bind(int noteId, String rTitle, String rContent, String rCapturedImagePath, String rCapturedImageThumbnailPath, String capturedImageDateTime){
        Note_id = noteId;
        title.setText(rTitle.trim());
        //Used in old recycler view theme
//        content.setText(rContent);
        content = rContent;
        //TODO Fetch Image by "rCapturedImagePath" and replace "null"
//        imageView.setImageBitmap(null);
        Picasso.get().load(new File(rCapturedImageThumbnailPath)).into(imageView);
        noteTakingDateTime.setText(capturedImageDateTime);
        image_path = rCapturedImagePath;
        image_path_thumbnail = rCapturedImageThumbnailPath;
        viewHolderFileNames = image_path.substring(image_path.lastIndexOf("/")+1, image_path.lastIndexOf(".")-1);
    }

    static NoteViewHolder create(ViewGroup parent) {
        //OLD Recycler view theme
        //        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.single_recycler_view_holder, parent, false);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_recycler_view_item, parent, false);

        return new NoteViewHolder(view);
    }

    public int getNote_id() {
        return Note_id;
    }

    public static void setToClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }


    public static void delete_images_from_app_folder(String rImagePath){
        File file = new File(rImagePath);
        if (file.exists()) {
            if(file.delete() == true)
            {
                    Log.i("Simple Internal Image Delete", "Deleted Image " + rImagePath);
            }
            else
            {
                    Log.i("Simple Internal Image Delete", "Can't Delete Image " + rImagePath);
//                        deleteFileFromMediaStore(context.getContentResolver(), file);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_content_layout:
                    Log.i("CLICKED", "Card Content Layout");
                    BasicActivityClass.observerCounter++;
                    BasicActivityClass.ClickedNote.NoteId = getNote_id();
                    BasicActivityClass.ClickedNote.Title = title.getText().toString();
                    BasicActivityClass.ClickedNote.CapturedImageDateTime = noteTakingDateTime.getText().toString();
                    BasicActivityClass.ClickedNote.CapturedImagePath = image_path;
                    BasicActivityClass.ClickedNote.Content = content;
                    BasicActivityClass.NoteIdClicked.setValue(getNote_id());
                    materialButtonToggleGroup.clearChecked();
                break;

            case R.id.note_recycler_view_item_download_pdf:
                    TextToPdf.stringToPdf(content , viewHolderFileNames);
                    materialButtonToggleGroup.clearChecked();
                break;

            case R.id.note_recycler_view_item_speak_up:
                Toast.makeText(BasicActivityClass.recyclerView.getContext(), "Speak button", Toast.LENGTH_LONG).show();
                if(speaking == false)
                {
                    speaking = true;
                    BasicActivityClass.speak(content);
                }
                else
                {
                    speaking = false;
                    BasicActivityClass.stopSpeaking();
                    materialButtonToggleGroup.clearChecked();
                }
                break;

            case R.id.note_recycler_view_item_download_audio:
                materialButtonToggleGroup.clearChecked();
                String audioFileName = viewHolderFileNames;
                File ep = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SNT_MP3");
                if(!ep.exists()){
                    ep.mkdir();
                }
                String audioFilePath = ep.getAbsolutePath()+"/"+audioFileName+".mp3";
                BasicActivityClass.textToAudio.downloadMp3File(content,new File(audioFilePath));
                Toast.makeText(BasicActivityClass.recyclerView.getContext(),audioFilePath,Toast.LENGTH_LONG).show();
                break;

            case R.id.note_recycler_view_item_delete:
                    BasicActivityClass.mNoteViewModel.remove(getNote_id());
                    delete_images_from_app_folder(image_path);
                    delete_images_from_app_folder(image_path_thumbnail);
                    materialButtonToggleGroup.clearChecked();
                break;

            case R.id.note_recycler_view_item_copy:
                setToClipboard(BasicActivityClass.recyclerView.getContext(),content);
                Toast.makeText(BasicActivityClass.recyclerView.getContext(),"Text Copied" , Toast.LENGTH_SHORT).show();
                materialButtonToggleGroup.clearChecked();
                break;

            case R.id.note_recycler_view_item_share:
                BasicActivityClass.shareText(BasicActivityClass.recyclerView.getContext(), "Sharing Note Text", content);
                materialButtonToggleGroup.clearChecked();
                break;

            default:break;
        }
    }
}
