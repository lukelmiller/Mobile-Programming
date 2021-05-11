package com.csce4623.ahnelson.restclientexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;

public class AddComment extends AppCompatActivity {

    EditText etAddName;
    EditText etAddEmail;
    EditText etAddBody;
    Button btAddSave;
    Comment c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        etAddBody = (EditText) findViewById(R.id.etAddBody);
        etAddName = (EditText) findViewById(R.id.etAddName);
        etAddEmail = (EditText) findViewById(R.id.etAddEmail);
        btAddSave = (Button) findViewById(R.id.btAddSave);
        c = new Comment();




        btAddSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.setBody(etAddBody.getText().toString());
                c.setEmail(etAddEmail.getText().toString());
                c.setName(etAddName.getText().toString());
                Intent intent = new Intent();
                intent.putExtra("Body", c.getBody());
                intent.putExtra("Name", c.getName());
                intent.putExtra("Email", c.getEmail());
                setResult(2,intent);
                finish();

            }
        });
    }
}