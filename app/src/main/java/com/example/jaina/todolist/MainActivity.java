package com.example.jaina.todolist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private EditText item;
    private Button add;
    private ListView list ;
    private ArrayList<String> words ;
    private ArrayAdapter adapter;
    int j;
    public static final String file_name = "TDLfile.txt";
    PrintStream output ;
    Scanner scan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //activity starts without onscreen keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        add=(Button)findViewById(R.id.button);
        item=(EditText)findViewById(R.id.textView2);
        list = (ListView)findViewById(R.id.list);
        words = new ArrayList<String>();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,android.R.id.text1,words);
        list.setAdapter(adapter);


        //Action on Add Button Click
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = item.getText().toString();

                words.add(word);

                adapter.notifyDataSetChanged();

                item.setText("");
                //To hide virtual keyboard after addition of the word
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        //To Remove an Item
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String RemovedItem = parent.getItemAtPosition(position).toString();
                words.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Removed : "+RemovedItem,Toast.LENGTH_LONG).show();
                return false;
            }
        });

    }

    //to add words in a file
    protected void onPause()
    {
        super.onPause();
        try {
            output = new PrintStream(openFileOutput(file_name,MODE_PRIVATE));
            for(j=0; j<words.size();j++)
                output.println(words.get(j));
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    //to get words from the file
    protected void onResume()
    {
        super.onResume();
        scan = new Scanner(file_name);
        while (scan.hasNextLine())
        {
            String line=scan.nextLine();
            words.add(line);
            adapter.notifyDataSetChanged();
        }
        scan.close();
    }

}
