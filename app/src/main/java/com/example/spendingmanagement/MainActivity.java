package com.example.spendingmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.content.SharedPreferences;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String pref = "SpendingManagement";
    public static final String bal = "BalanceKey";
    public static final String lastNote = "LastNote";
    private static DecimalFormat df = new DecimalFormat("0.00");
    Button add, sub;
    EditText date, amount, description;
    TextView balance;
    LinearLayout log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = findViewById(R.id.add);
        add.setOnClickListener(onClickForAdd());
        sub = findViewById(R.id.sub);
        sub.setOnClickListener(onClickForSub());
        date = findViewById(R.id.date);
        amount = findViewById(R.id.amount);
        description = findViewById(R.id.description);
        balance = findViewById(R.id.CurrentBalance);
        log = findViewById(R.id.log);

        sharedPreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        //sharedPreferences.edit().clear().commit();
        if(sharedPreferences.contains(bal)) {
            balance.setText("Current Balance: $" + df.format(sharedPreferences.getFloat(bal, 0.0f)));
        } else {
            balance.setText("Current Balance: $0.0");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(bal, 0.0f);
        }

        if(sharedPreferences.contains(lastNote)) {
            for (int i = 0; i <= sharedPreferences.getInt(lastNote, 0); i++) {
                log.addView(createNewTextView(sharedPreferences.getString(Integer.toString(i), "")));
            }
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(lastNote, -1);
        }
    }

    private OnClickListener onClickForAdd() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                String s = "Adding $" + amount.getText().toString() + " on " + date.getText().toString() +
                        " from " + description.getText().toString();
                log.addView(createNewTextView(s));

                float currBal = sharedPreferences.getFloat(bal, 0.0f);
                currBal += Float.parseFloat(amount.getText().toString());
                int last = sharedPreferences.getInt(lastNote, -1);
                last += 1;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(bal, currBal);
                editor.putInt(lastNote, last);
                editor.putString(Integer.toString(last), s);
                editor.commit();

                balance.setText("Current Balance: $" + df.format(sharedPreferences.getFloat(bal, 0.0f)));
            }
        };
    }

    private OnClickListener onClickForSub() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                String s = "Spent $" + amount.getText().toString() + " on " + date.getText().toString() +
                        " for " + description.getText().toString();
                log.addView(createNewTextView(s));

                float currBal = sharedPreferences.getFloat(bal, 0.0f);
                currBal -= Float.parseFloat(amount.getText().toString());
                int last = sharedPreferences.getInt(lastNote, -1);
                last += 1;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(bal, currBal);
                editor.putInt(lastNote, last);
                editor.putString(Integer.toString(last), s);
                editor.commit();

                balance.setText("Current Balance: $" + df.format(sharedPreferences.getFloat(bal, 0.0f)));
            }
        };
    }

    private TextView createNewTextView(String text) {
        final LayoutParams lparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final TextView newNote = new TextView(this);
        newNote.setLayoutParams(lparams);
        newNote.setText(text);
        newNote.setTextSize(15);
        newNote.setPadding(20, 50, 20, 50);
        return newNote;
    }

}
