package com.sheygam.masa_2018_g2_30_01_19.presentation;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sheygam.masa_2018_g2_30_01_19.R;
import com.sheygam.masa_2018_g2_30_01_19.data.HttpProvider;
import com.sheygam.masa_2018_g2_30_01_19.data.dto.AuthResponseDto;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    private EditText inputEmail, inputPassword;
    private Button regBtn, loginBtn;
    private AlertDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        loginBtn = findViewById(R.id.login_btn);
        regBtn = findViewById(R.id.reg_btn);
        progressDialog = new AlertDialog.Builder(this)
                .setView(R.layout.dialog_progress_view)
                .setCancelable(false)
                .setTitle("Authorization...")
                .create();
        regBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.reg_btn){
            if(isEmailValid(inputEmail.getText().toString())
                    && isPasswordValid(inputPassword.getText().toString())){
                new RegTask().execute();
            }
        }
    }

    private boolean isEmailValid(String email){
        if(!email.contains("@")){
            inputEmail.setError("Email must contain @");
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password){
        return true;
    }

    private void showProgress(){
        progressDialog.show();
    }

    private void hideProgress(){
        progressDialog.dismiss();
    }

    private void showError(String error){
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(error)
                .setPositiveButton("Ok",null)
                .setCancelable(false)
                .create()
                .show();
    }

    public void isAuthSuccess(String authType){
        View view = getLayoutInflater().inflate(R.layout.dialog_success_view,null);
        TextView titleTxt = view.findViewById(R.id.title_txt);
        titleTxt.setText(authType + " success");
        new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("Ok",MainActivity.this)
                .create()
                .show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        //ToDo show next activity
    }

    private class RegTask extends AsyncTask<Void,Void,String>{
        private String email,password;
        private boolean isSuccess = true;

        @Override
        protected void onPreExecute() {
            email = inputEmail.getText().toString();
            password = inputPassword.getText().toString();
            showProgress();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "Registration ok!";
            try {
                AuthResponseDto response = HttpProvider.getInstance().registration(email,password);
                Log.d("MY_TAG", "doInBackground: " + response.getToken());
                getSharedPreferences("AUTH",MODE_PRIVATE)
                        .edit()
                        .putString("token",response.getToken())
                        .commit();
            } catch (IOException e){
                e.printStackTrace();
                result = "Connection error! Check your internet!";
                isSuccess = false;
            }catch (Exception e) {
                result = e.getMessage();
                isSuccess = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            hideProgress();
            if (isSuccess){
                isAuthSuccess("Registration");
            }else{
                showError(s);
            }
        }
    }
}
