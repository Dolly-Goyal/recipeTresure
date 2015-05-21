package com.recipe.dolly.mocktail;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends ActionBarActivity {
    EditText mName,mEmail,mPassword,mConform;
    TextView mHome,mCancel;
    Button mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mName = (EditText) findViewById(R.id.personName);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.pass);
        mConform = (EditText) findViewById(R.id.conform);
        mHome = (TextView) findViewById(R.id.home);
        mCancel = (TextView) findViewById(R.id.cancel);
        mSignUp = (Button) findViewById(R.id.submitDetail);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nName = mName.getText().toString().trim();
                String nEmail = mEmail.getText().toString().trim();
                String nPassword = mPassword.getText().toString().trim();
                String Conform = mConform.getText().toString().trim();

                if (nName.isEmpty() || nEmail.isEmpty() || nPassword.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setTitle("Oops!!!").setMessage(R.string.error_msg)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    setProgressBarIndeterminateVisibility(true);
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(nName);
                    newUser.setEmail(nEmail);
                    newUser.setPassword(nPassword);
                    if (!nPassword.equals(Conform)) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SignUpActivity.this);
                        builder1.setTitle("Sorry!!").setMessage("Password is not match").setPositiveButton(android.R.string.ok,null);
                        AlertDialog dialog = builder1.create();
                        dialog.show();
                    }

                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            setProgressBarIndeterminateVisibility(false);
                            if (e == null) {
                                Intent intent = new Intent(SignUpActivity.this,RecipeDataActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setTitle("Oops!").setMessage(e.getMessage())
                                        .setPositiveButton(android.R.string.ok, null);

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
