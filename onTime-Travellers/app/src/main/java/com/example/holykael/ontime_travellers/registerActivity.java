package com.example.holykael.ontime_travellers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;

public class registerActivity extends AppCompatActivity {
    ArrayList<String> listOfPattern=new ArrayList<String>();
    boolean validateNumber=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^3[47][0-9]{5,}$";
        listOfPattern.add(ptAmeExp);
        setContentView(R.layout.activity_register);
        EditText cardnr = (EditText) findViewById(R.id.card_nr) ;
        cardnr.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("DEBUG", "afterTextChanged : "+s);
                String ccNum = s.toString();
                for(String p:listOfPattern){
                    if(ccNum.matches(p)){
                        Log.d("DEBUG", "afterTextChanged : discover");
                        validateNumber=true;
                        break;
                    }
                    else validateNumber=false;
                }

            }
        });

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
