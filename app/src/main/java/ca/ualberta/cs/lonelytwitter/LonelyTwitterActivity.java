package ca.ualberta.cs.lonelytwitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LonelyTwitterActivity extends Activity {

	private static final String FILENAME = "file.sav";
	private EditText bodyText;
	private ListView oldTweetsList;
	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	/** Called when the activity is first created. */
	private ArrayAdapter<Tweet> adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		bodyText = (EditText) findViewById(R.id.body);
		Button saveButton = (Button) findViewById(R.id.save);
		oldTweetsList = (ListView) findViewById(R.id.oldTweetsList);
		Button clearButton = (Button) findViewById(R.id.clear);

		saveButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				//setResult(RESULT_OK);
				String text = bodyText.getText().toString();
				ImportantTweet newTweet = new ImportantTweet();
				try {
					newTweet.setMessage(text);
					newTweet.setDate(new Date());

					//finish();
					tweets.add(newTweet);
					adapter.notifyDataSetChanged();
					saveInFile();
				}
				catch(TweetTooLongException e) {
				}


			}
		});
		clearButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				tweets.clear();
				adapter.notifyDataSetChanged();;
				saveInFile();
			}

		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		loadFromFile();
		adapter = new ArrayAdapter<Tweet>(this,
				R.layout.list_item, tweets);
		oldTweetsList.setAdapter(adapter);
	}

	private void loadFromFile() {
		//ArrayList<String> tweets = new ArrayList<String>();
		try {
			FileInputStream fis = openFileInput(FILENAME);
			//Log.d("fis step","fileinput");
			InputStreamReader isr = new InputStreamReader(fis);
			//Log.d("gson step","inputstream");
			BufferedReader reader = new BufferedReader(isr);
			Gson gson = new Gson();

			//create a listTweetType so program knows what is being read
			Type listTweetType = new TypeToken<ArrayList<ImportantTweet>>(){}.getType();
			tweets = gson.fromJson(reader,listTweetType);


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//tweets = new ArrayList<Tweet>();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void saveInFile() {
		try {
			FileOutputStream fos = openFileOutput(FILENAME,0);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter writer = new BufferedWriter(osw);
			Gson gson = new Gson();
			gson.toJson(tweets,writer);
			writer.flush();
			fos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//tweets = new ArrayList<String>();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//catch (TweetTooLongException e) {
            //e.printStackTrace();
        //}
	}
}