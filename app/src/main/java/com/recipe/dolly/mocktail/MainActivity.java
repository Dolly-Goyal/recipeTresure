package com.recipe.dolly.mocktail;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private Toolbar mToolbar;
    GridView mGridView;
    List<String> rName,uName;
    protected List<ParseObject> recipeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);

        mGridView = (GridView) findViewById(R.id.gridView);
        rName = new ArrayList<String>();
        uName = new ArrayList<String>();
        recipeData = new ArrayList<ParseObject>();

        NavigationFragment drawerFragment = (NavigationFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        drawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout)findViewById(R.id.drawerLayout), mToolbar);

        ParseQuery<ParseObject> recipeQuery = ParseQuery.getQuery("RecipeData");
        recipeQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    recipeData = parseObjects;
                    int i = 0;
                    for (ParseObject rData : recipeData) {
                        rName.add(rData.getString(Constant.KEY_RECIPE_NAME));
                        uName.add(rData.getString(Constant.KEY_PERSON_NAME));
                        Log.d("Parse data fetch", rName.toString());
                        i++;
                    }
                    CustomAdapter adapter = new CustomAdapter(MainActivity.this,rName,uName);
                    mGridView.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(this,RecipeDataActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(this,RecipeDataActivity.class);
            startActivity(intent);
            finish();

        }
    }
}
