package com.example.jingjing.firebasev3_test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;

    ListView listViewMovie;
    List<Movie> movieList;
    EditText editTextMovieName;
    Button buttonAddMovie;
    Button buttonLoad;
    Spinner spinnerCity;
    Context myContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewMovie = (ListView) findViewById(R.id.listViewMovie);
        movieList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        editTextMovieName = (EditText) findViewById(R.id.editTextMovieName);
        buttonAddMovie = (Button) findViewById(R.id.buttonAddMovie);
        buttonLoad = (Button) findViewById(R.id.buttonLoad);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
        myContext = this;


        // Why keyboard is still keep showing up ?
        /*
        editTextMovieName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = editTextMovieName.getInputType(); // backup the input type
                editTextMovieName.setInputType(InputType.TYPE_NULL); // disable soft input
                editTextMovieName.onTouchEvent(event); // call native handler
                editTextMovieName.setInputType(inType); // restore input type
                return true; // consume touch even
            }
        });
        */

        buttonAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMovie();
            }
        });

        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieList.clear();

                db.collection("JingJing-MovieList")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("itspeter", document.getId() + " => " + document.getData());
                                        JSONObject tmpJsonobj = new JSONObject(document.getData());
                                        // Convert to Movie class
                                        try {
                                            Movie movie = new Movie(
                                                    tmpJsonobj.getString("movie"),
                                                    tmpJsonobj.getString("city"),
                                                    tmpJsonobj.getString("theater"));
                                            movieList.add(movie);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        ArrayAdapter adapter = new MovieList(MainActivity.this, movieList);
                                        listViewMovie.setAdapter(adapter);
                                    }
                                } else {
                                    Log.w("itspeter", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });

    }

    private void addMovie() {
        String city = spinnerCity.getSelectedItem().toString();
        String movie = editTextMovieName.getText().toString().trim();
        String theater = "ThisIsAApple";


        if (!TextUtils.isEmpty(movie)) {
            // Create a new user with a first and last name
            Map<String, Object> entryToPush = new HashMap<>();
            entryToPush.put("city", city);
            entryToPush.put("movie", movie);
            entryToPush.put("theater", theater);

            // Add a new document with a generated ID
            db.collection("JingJing-MovieList")
                    .add(entryToPush)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String debugMsg = "DocumentSnapshot added with ID: " + documentReference.getId();
                            Toast.makeText(myContext,debugMsg, Toast.LENGTH_LONG );
                            Log.d("itspeter", debugMsg);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String debugMsg = "Error adding document" + e.toString();
                            Toast.makeText(myContext,debugMsg, Toast.LENGTH_LONG );
                            Log.w("itspeter", debugMsg);
                        }
                    });

        } else {
            Toast.makeText(this, "You should eat shit", Toast.LENGTH_LONG).show();
        }
    }
}
