package com.sharkdev98.smartnotetakerviaocr.BussinessLayer.Common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonBussinessLogic {

    private static Date note_taking_date;

    public void ShowMessage(String title, String message , Context activity)
    {
        AlertDialog.Builder AlertDialogBuilder = new AlertDialog.Builder(activity)
                                        .setTitle(title)
                                        .setMessage(message)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
        AlertDialogBuilder.create().show();
    }

    public String getDateTimeString()
    {
        note_taking_date = new Date();
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(note_taking_date);
    }

    public String getDateTimeStringForDisplay()
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(note_taking_date);
    }

}
