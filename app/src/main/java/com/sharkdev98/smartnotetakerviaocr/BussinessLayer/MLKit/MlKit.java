package com.sharkdev98.smartnotetakerviaocr.BussinessLayer.MLKit;

import android.graphics.Point;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.sharkdev98.smartnotetakerviaocr.ApplicationLayer.BasicComponents.ICallback;
import com.sharkdev98.smartnotetakerviaocr.BussinessLayer.Common.CommonBussinessLogic;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;


public class MlKit extends CommonBussinessLogic {

    private static String MlExtractedText;
    static boolean TextFound = false;
    public static int title_length = 20;
    ICallback ic;

    public MlKit(ICallback rCallback) {
        super();
        ic = rCallback;
    }

    public void setIc(ICallback ic) {
        this.ic = ic;
    }

    public boolean isTextFound() {
        return TextFound;
    }

    public String getMlExtractedText() {
        return MlExtractedText;
    }

    public void recognizeText(InputImage image) {
        TextRecognizer recognizer = TextRecognition.getClient();
        Task<Text> result = recognizer.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                    MlExtractedText = "";
                                    for (Text.TextBlock block : visionText.getTextBlocks()) {
                                        Rect boundingBox = block.getBoundingBox();
                                        Point[] cornerPoints = block.getCornerPoints();
                                        MlExtractedText = MlExtractedText + " " + block.getText() + " ";
                                    }

//                                    ShowMessage("Extracted Text From Image",MlExtractedText,activity);
                                    MlExtractedText = MlExtractedText.replaceAll("\n", " ");
                                    TextFound = true;
                                    recognizer.close();
                                    ic.callback(MlExtractedText);
                            }
                            })
                            .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        MlExtractedText = "";
                                        TextFound = false;
                                        recognizer.close();
                                    }
                                }
                            );
    }

}
