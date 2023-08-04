package com.example.statussaverpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class about extends AppCompatActivity {

    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        email = findViewById(R.id.email);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iemail = new Intent(Intent.ACTION_SEND);
                iemail.setType("message/rfc822");
                iemail.putExtra(Intent.EXTRA_EMAIL, new String[]{"maverickbits.official@gmail.com"});
                startActivity(iemail);

            }
        });
    }
}