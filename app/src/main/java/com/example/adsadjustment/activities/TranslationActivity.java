package com.example.adsadjustment.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adsadjustment.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class TranslationActivity extends AppCompatActivity {

    private TextView mSourceLang;
    private EditText mSourcetext;
    private Button mTranslateBtn;
    private TextView mTranslatedText;
    private String sourceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);

        mSourceLang = findViewById(R.id.sourceLang);
        mSourcetext = findViewById(R.id.sourceText);
        mTranslateBtn = findViewById(R.id.translate);
        mTranslatedText = findViewById(R.id.translatedText);

        mTranslateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                identifyLanguage();
            }
        });


    }

    private void identifyLanguage() {
        sourceText = mSourcetext.getText().toString();

        LanguageIdentifier languageIdentifier =
                LanguageIdentification.getClient();
        languageIdentifier.identifyLanguage(sourceText)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {

                            @Override
                            public void onSuccess(String s) {
                                if (s.equals("und")) {
                                    Toast.makeText(getApplicationContext(), "Language Not Identified", Toast.LENGTH_SHORT).show();
                                } else {
                                    getLanguageCode(s);
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be loaded or other internal error.
                                // ...
                            }
                        });


        mSourceLang.setText("Detecting..");

    }

    private void getLanguageCode(String language) {
        String langCode;
        switch (language) {
            case "hi":
                langCode = "hi";
                mSourceLang.setText("Hindi");
                break;
            case "ar":
                langCode = "ar";
                mSourceLang.setText("Arabic");

                break;
            case "ur":
                langCode = "ur";
                mSourceLang.setText("Urdu");

                break;
            default:
                langCode = "und";
        }

        translateText(langCode);
    }

    private void translateText(String langCode) {

        final boolean[] isModel = {false};

        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(langCode)
                        .setTargetLanguage(TranslateLanguage.GERMAN)
                        .build();
        final Translator englishGermanTranslator =
                Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        englishGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                isModel[0] = true;
                            }

                        });


        mTranslatedText.setText("Translating..");

        englishGermanTranslator.translate(sourceText)
                .addOnSuccessListener(
                        new OnSuccessListener() {

                            @Override
                            public void onSuccess(Object o) {
                                mTranslatedText.setText(o.toString());
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mTranslatedText.setText("Translation could not be possible at the moment please try again!");
                            }
                        });

    }
}