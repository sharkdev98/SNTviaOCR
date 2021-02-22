package com.sharkdev98.smartnotetakerviaocr.BussinessLayer.TTPDF;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.sharkdev98.smartnotetakerviaocr.ApplicationLayer.BasicComponents.BasicActivityClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TextToPdf {

    /**
     * This function draws the text on the canvas based on the x-, y-position.
     * If it has to break it into lines it will do it based on the max width
     * provided.
     *
     * @author Alessandro Giusa
     * @version 0.1, 14.08.2015
     * @param canvas
     *            canvas to draw on
     * @param paint
     *            paint object
     * @param x
     *            x position to draw on canvas
     * @param y
     *            start y-position to draw the text.
     * @param maxWidth
     *            maximal width for break line calculation
     * @param text
     *            text to draw
     */
    public static void drawTextAndBreakLine(final Canvas canvas, final Paint paint, final float x, final float y, final float maxWidth, final String text) {
        String textToDisplay = text;
        String tempText = "";
        char[] chars;
        float textHeight = paint.descent() - paint.ascent();
        float lastY = y;
        int nextPos = 0;
        int lengthBeforeBreak = textToDisplay.length();
        do {
            lengthBeforeBreak = textToDisplay.length();
            chars = textToDisplay.toCharArray();
            nextPos = paint.breakText(chars, 0, chars.length, maxWidth, null);
            tempText = textToDisplay.substring(0, nextPos);
            textToDisplay = textToDisplay.substring(nextPos, textToDisplay.length());
            canvas.drawText(tempText, x, lastY, paint);
            lastY += textHeight;
        } while(nextPos < lengthBeforeBreak);
    }

    public static int pageWidth = 595;
    public static int pageHeight = 842;
    public static int pagePadding = 30;

    public static void stringToPdf(String data , String fileName) {
        File ep = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/SNT_PDF");
        if(!ep.exists()){
            ep.mkdir();
        }
        try {
            File file = new File(ep.getAbsolutePath()+"/"+fileName+".pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setTextSize(20);
//            paint.setColor(Color.WHITE);
            paint.setSubpixelText(true);
            float textHeight = paint.descent() - paint.ascent();
            TextToPdf.drawTextAndBreakLine(canvas, paint, pagePadding, textHeight, pageWidth-(pagePadding*2) , data);

//            canvas.drawText(data, 10, 10, paint);

            document.finishPage(page);
            document.writeTo(fOut);
            document.close();
            Toast.makeText(BasicActivityClass.recyclerView.getContext(),file.getAbsolutePath(),Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.i("PDFERROR", e.getLocalizedMessage());
        }
    }

}
