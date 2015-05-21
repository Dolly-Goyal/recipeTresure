package com.recipe.dolly.mocktail;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class RecipeDataActivity extends ActionBarActivity {
    ImageView mShowImage;
    Uri outputFileUri;
    private Spinner mSpinner;
    EditText mRecipeName, mRecipeIngredients, mRecipeDetail;
    Button mSubmitRecipe;
    Toolbar mToolbar;
    int spinnerItemSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_data);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);

        mRecipeName = (EditText) findViewById(R.id.recipeName);
        mRecipeIngredients = (EditText) findViewById(R.id.ingredients);
        mRecipeDetail = (EditText) findViewById(R.id.detail);
        mSubmitRecipe = (Button) findViewById(R.id.submitRecipe);

        NavigationDrawerFragment drawerNavigation = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_fragment);
        drawerNavigation.setUp(R.id.navigation_drawer_fragment,
                (DrawerLayout) findViewById(R.id.drawerLayoutNavigation), mToolbar);

        mShowImage = (ImageView) findViewById(R.id.showImage);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.Recipe_Type));
        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerItemSelected = position;
                Log.i("slect Item", position + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mSubmitRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpinner.getSelectedItemPosition() != 0) {
                    ParseObject recipe = new ParseObject("RecipeData");
                    recipe.put("Recipe_Name", mRecipeName.getText().toString());
                    recipe.put("Ingredients", mRecipeIngredients.getText().toString());
                    recipe.put("Detail", mRecipeDetail.getText().toString());
                    recipe.put("Recipe_Type", getResources().getStringArray(R.array.Recipe_Type)[spinnerItemSelected]);
                    recipe.put("User_Name", ParseUser.getCurrentUser().getUsername());

                    recipe.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                        }
                    });
                    Intent intent = new Intent(RecipeDataActivity.this, RecipeDataActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.camera_icon) {
            openImageIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openImageIntent() {
        // Setting up Uri of camera image to save.
        //Creating directory to store files.
        final File root = new File(Environment.getExternalStorageDirectory()
                + File.separator + "Test" + File.separator);
        root.mkdirs();
        //Creating unique filename using systemtime.
        final String fileName = System.currentTimeMillis() + ".jpg";
        //Creating output Uri for onActivityResult once the image is chosen.
        final File sdImageMainDirectory = new File(root, fileName);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);
        // Camera Intent
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(
                captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent mIntent = new Intent(captureIntent);
            mIntent.setComponent(new ComponentName(
                    res.activityInfo.packageName, res.activityInfo.name));
            mIntent.setPackage(packageName);
            mIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(mIntent);
        }
        // Gallery Intent
        final Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Chooser intent for choosing between options
        //Adding gallery option to chooser
        final Intent chooserIntent = Intent.createChooser(galleryIntent,
                "Select image source");
        // Adding camera option
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent,
                Constant.SELECT_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.SELECT_IMAGE_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action
                                .equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }
                Uri selectedImageUri;
                if (isCamera) {
                    //Obtain the captured image based on previously saved outputFileUri and store it to selectedImageUri
                    selectedImageUri = outputFileUri;
                } else {
                    //Obtain the gallery i
                    selectedImageUri = data == null ? null : data.getData();
                }
                mShowImage.setImageURI(selectedImageUri);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
