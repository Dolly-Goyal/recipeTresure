package com.recipe.dolly.mocktail;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by DELL on 01-05-2015.
 */
public class Mocktail extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "4vXwbF0GGkLwVoHp7NYuAFQZUZtEpUa8Pw53H5y4",
                "IkAiObB8twd0Pi0doMHg9lAqVe6ZD84T3RIRrVKc");
    }
}
