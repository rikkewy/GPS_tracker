package com.example.gpstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Initialization extends AppCompatActivity {

    EditText user_name, password, repassword;
    Button signup, signin;
    DB myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);

        user_name = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        signup = (Button) findViewById(R.id.btn_signup);
        signin = (Button) findViewById(R.id.btn_signin);
        myDB = new DB(this);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = user_name.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                if (user.equals("") || pass.equals("") || repass.equals("")){
                    Toast.makeText(Initialization.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = myDB.checkusername(user);
                        if (checkuser == false) {
                            Boolean insert_data = myDB.insertData(user, pass);
                            if (insert_data == true) Toast.makeText(Initialization.this, "Вы успешно зарегистрированы", Toast.LENGTH_SHORT).show();
                            else Toast.makeText(Initialization.this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(Initialization.this, "Вы уже зарегистрированы", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(Initialization.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}