package com.google.codelab.mlkit.activities;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.codelab.mlkit.customview.GraphicOverlay;
import com.google.codelab.mlkit.customview.TextGraphic;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.Arrays;

public class FirebaseMLActivity extends BaseRecognitionActivity {

    @Override
    public void onResume() {
        super.onResume();
        setTitle("Firebase ML");
    }

    @Override
    protected void runTextRecognition() {
        mTextButton.setEnabled(false);
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(mSelectedImage);
        FirebaseVisionCloudTextRecognizerOptions options =
                new FirebaseVisionCloudTextRecognizerOptions.Builder()
                        .setLanguageHints(Arrays.asList("en", "es"))
                        .build();
        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                .getCloudTextRecognizer();
        mTextButton.setEnabled(false);
        textRecognizer.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText result) {
                        mTextButton.setEnabled(true);
                        processTextRecognitionResult(result);
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                mTextButton.setEnabled(true);
                                e.printStackTrace();
                            }
                        });
    }

    private void processTextRecognitionResult(FirebaseVisionText result) {
        mGraphicOverlay.clear();
        if (result.getTextBlocks().isEmpty()) {
            showToast("No text found");
            return;
        }

        for (FirebaseVisionText.TextBlock block: result.getTextBlocks()) {
            for(FirebaseVisionText.Line line: block.getLines()) {
                for (FirebaseVisionText.Element element: line.getElements()) {
                    GraphicOverlay.Graphic textGraphic = new TextGraphic(mGraphicOverlay, mapToTextElement(element));
                    mGraphicOverlay.add(textGraphic);
                }
            }
        }
    }

    private TextGraphic.Element mapToTextElement(FirebaseVisionText.Element element) {
        return new TextGraphic.Element(element.getText(), element.getBoundingBox());
    }
}