package com.google.codelab.mlkit.activities;

public class FirebaseMLActivity extends BaseRecognitionActivity {

    @Override
    public void onResume() {
        super.onResume();
        setTitle("Firebase ML");
    }

    @Override
    protected void runTextRecognition() {
        throw new UnsupportedOperationException("NOT SUPPORTED YET");
    }
}