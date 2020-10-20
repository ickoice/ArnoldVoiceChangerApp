package com.radanlievristo.arnoldvoicechangerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button speakButton;
    TextView arnoldQuoteTextView;
    EditText sentenceEditText;

    ProgressDialog pd;

    String BASE_URL = "https://mumble.stream/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpProgressDialog();

        arnoldQuoteTextView = findViewById(R.id.arnoldQuoteTextView);

        sentenceEditText = findViewById(R.id.sentenceEditText);

        speakButton = findViewById(R.id.speakButton);

        speakButton.setOnClickListener(v -> {

            pd.show();

            speakButton.setEnabled(false);

            String text = sentenceEditText.getText().toString();

            SpeakRequestModel speakRequest = new SpeakRequestModel(text);

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            SpeakApi speakApi = retrofit.create(SpeakApi.class);

            Call<ResponseBody> call = speakApi.speakQuote(speakRequest);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {

                        String filePath = UtilFunctions.writeResponseToDisk(response.body(), MainActivity.this);

                        Uri uri = Uri.parse(filePath);

                        pd.dismiss();

                        UtilFunctions.playSound(uri, MainActivity.this);

                        speakButton.setEnabled(true);

                        sentenceEditText.setText("");

                        arnoldQuoteTextView.setText(text);

                    } else {

                        pd.dismiss();

                        speakButton.setEnabled(true);

                        Toast.makeText(getApplicationContext(), "Arnold is having a hard time reading your sentence. Please try again!", Toast.LENGTH_LONG).show();

                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    pd.dismiss();

                    speakButton.setEnabled(true);

                    Toast.makeText(getApplicationContext(), "Arnold is having a hard time reading your sentence. Please try again!", Toast.LENGTH_LONG).show();

                }
            });



        });

    }

    public void setUpProgressDialog(){
        pd = new ProgressDialog(this);
        pd.setMessage("Arnold is reading...");
        pd.setCancelable(false);
    }
}
