package com.example.khuta.englishclub;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;


public class DictionaryActivity extends AppCompatActivity {

    private TextView wordTranslate;
    private TextView textTransslated , englishtext;
    private Button translateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        //initializing components
        wordTranslate = findViewById(R.id.wordtotranslate);
        textTransslated = findViewById(R.id.texttranslated);
        englishtext = findViewById(R.id.englishtext);
        translateButton = findViewById(R.id.translate_button);

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CallbackTask().execute(translations(wordTranslate.getText().toString()));
            }
        });

    }

    //get translate format
    private String translations(String words) {
        final String language = "en";
        final String target_lang = "ro";
        final String word = words;
        final String word_id = word.toLowerCase(); //word id is case sensitive and lowercase is required
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id + "/translations=" + target_lang;
    }


    // send word to translate
    @SuppressLint("StaticFieldLeak")
    private class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            //TODO: replace with your own app id and app key
            final String app_id = "44924817";
            final String app_key = "20d15647699db0d76b62edc2a4ead4fc";
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestProperty("app_id",app_id);
                urlConnection.setRequestProperty("app_key",app_key);

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                return stringBuilder.toString();

            }
            catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }


        // get translated word (result)
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("dictionary" , "i am in dictionary and the translaetd is " +result);

            String translatedText = null , englishText = null;

            try {
                JSONObject js = new JSONObject(result);
                JSONArray resuJsonArraylt = js.getJSONArray("results");

                JSONObject entry = resuJsonArraylt.getJSONObject(0);
                JSONArray array = entry.getJSONArray("lexicalEntries");

                JSONObject entries = array.getJSONObject(0);
                JSONArray e = entries.getJSONArray("entries");

                JSONObject jsonObject = e.getJSONObject(0);
                JSONArray sensesarray = jsonObject.getJSONArray("senses");

                JSONObject examples = sensesarray.getJSONObject(0);
                JSONArray examplesarray = examples.getJSONArray("examples");

                JSONObject translations = examplesarray.getJSONObject(0);
                JSONArray translationsarray = translations.getJSONArray("translations");

                JSONObject text = translationsarray.getJSONObject(0);

                englishText = translations.getString("text");
                translatedText = text.getString("text");

                if(englishText != null && translatedText != null){
                    englishtext.setText(englishText);
                    textTransslated.setText(translatedText);
                }


            } catch (JSONException e) {
                textTransslated.setText("Error! try again");
                englishtext.setText("Error! try again");
                e.printStackTrace();
            }

        }
    }


}
