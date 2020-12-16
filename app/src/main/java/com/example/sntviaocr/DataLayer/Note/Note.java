package com.example.sntviaocr.DataLayer.Note;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import static androidx.room.ColumnInfo.INTEGER;
import static androidx.room.ColumnInfo.TEXT;

@Entity(tableName = "note_table")
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "NoteId",typeAffinity = INTEGER)
    public int NoteId = 0;

    @ColumnInfo(name = "Title", typeAffinity = TEXT)
    public String Title = "";

    @ColumnInfo(name = "Content", typeAffinity = TEXT)
    public String Content = "";

    @ColumnInfo(name = "CapturedImagePath" , typeAffinity = TEXT)
    public String CapturedImagePath;

    @ColumnInfo(name = "CapturedImageThumbnailPath" , typeAffinity = TEXT)
    public String CapturedImageThumbnailPath;

    @ColumnInfo(name = "CapturedImageDateTime", typeAffinity = TEXT)
    public String CapturedImageDateTime = "";

    @ColumnInfo(name = "user_email", typeAffinity = TEXT)
    public String user_email = "";

    public static byte[] setCapturedImage(@NonNull Bitmap rCapturedImage) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        rCapturedImage.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] rCapturedImage) {
        return BitmapFactory.decodeByteArray(rCapturedImage, 0, rCapturedImage.length);
    }

    public Note(@NonNull String Title , @NonNull String Content , @NonNull String CapturedImageThumbnailPath, @NonNull String CapturedImagePath , @NonNull String CapturedImageDateTime, @NonNull String user_email){
        this.Title = Title;
        this.Content = Content;
        this.CapturedImagePath = CapturedImagePath;
        this.CapturedImageThumbnailPath = CapturedImageThumbnailPath;
        this.CapturedImageDateTime = CapturedImageDateTime;
        this.user_email = user_email;
    }
}