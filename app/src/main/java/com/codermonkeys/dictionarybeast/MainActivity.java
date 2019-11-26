package com.codermonkeys.dictionarybeast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity {
    androidx.appcompat.widget.SearchView search;
    static DatabaseHelper myDbHelper;
    static boolean databaseOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       Toolbar toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

        search =  findViewById(R.id.searchView);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setIconified(false);


            }
        });

        myDbHelper = new DatabaseHelper(MainActivity.this);
        if(myDbHelper.checkDataBase()) {

            openDatabase();
        } else  {
            LoadDatabaseAsync task  = new LoadDatabaseAsync(this);
            task.execute();
        }

    }

    protected static void openDatabase() {

        try {
            myDbHelper.openDatabase();
            databaseOpened = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {

            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_exit) {
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
