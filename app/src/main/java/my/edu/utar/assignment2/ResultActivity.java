package my.edu.utar.assignment2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {

    private LinearLayout resultLayout;

    // Identify the type of breakdown
    private String sourceName;

    // Ok button direct to MainActivity
    // saveResultButton save breakdown result without directing
    // shareResultButton allows user to share breakdown result
    private Button okButton, saveResultButton, shareResultButton;

    private SQLiteAdapter mySQLiteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Check which breakdown template to use according to the specified source
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("source")) {
            sourceName = intent.getStringExtra("source");
        }

        TextView createValue = findViewById(R.id.tv_create);
        createValue.setText(sourceName);
        switch (sourceName) {
            case "Equal Breakdown":
                handleEqualBreakdown(intent);
                break;
            case "Percentage Breakdown":
                handlePercentageBreakdown(intent);
                break;
            case "Amount Breakdown":
                handleAmountBreakdown(intent);
                break;
            default:
                break;
        }
    }

    // Function to handle equal breakdown
    private void handleEqualBreakdown(Intent intent) {
        // Find existing views
        resultLayout = findViewById(R.id.ll_result);
        TextView titleValue = findViewById(R.id.tv_titleValue);
        TextView whoPaysValue = findViewById(R.id.tv_whoPaysValue);
        TextView dateValue = findViewById(R.id.tv_dateValue);
        TextView billValue = findViewById(R.id.tv_billValue);
        TextView noOfPplValue = findViewById(R.id.tv_noOfPplValue);

        //Button
        okButton = findViewById(R.id.bt_ok);
        saveResultButton = findViewById(R.id.bt_saveResult);
        shareResultButton = findViewById(R.id.bt_shareResult);

        String title = intent.getStringExtra("title");
        titleValue.setText(title);

        String whoPays = intent.getStringExtra("whoPays");
        whoPaysValue.setText(whoPays);

        String date = intent.getStringExtra("date");
        dateValue.setText(date);

        String bill = intent.getStringExtra("bill");
        billValue.setText("RM" + bill);

        String noOfPpl = intent.getStringExtra("noOfPpl");
        noOfPplValue.setText(noOfPpl);

        String eachPersonPays = intent.getStringExtra("eachPersonPays");
        TextView textView = new TextView(this);
        textView.setText("RM" + eachPersonPays);
        textView.setId(View.generateViewId());
        textView.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        resultLayout.addView(textView, layoutParams);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(mainActivityIntent);

            }
        });

        saveResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mySQLiteAdapter = new SQLiteAdapter(getApplicationContext());
                mySQLiteAdapter.openToWrite();

                mySQLiteAdapter.insert( "Equal Breakdown - " + title, whoPays, date, bill, noOfPpl, eachPersonPays);
                mySQLiteAdapter.close();

                Toast.makeText(getApplicationContext(), "Breakdown Result Saved Successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        shareResultButton.setOnClickListener(view -> {
            String subject = "Restaurant Expense Breakdown for " + title;
            String body = "Details are as follows: " + "\n"
                    + "Who Pays: " + whoPays + "\n"
                    + "Date: " + date + "\n"
                    + "Total Bill: RM" + bill + "\n"
                    + "Number of People: " + noOfPpl + "\n"
                    + "Each Person Pays: RM" + eachPersonPays;
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(emailIntent, "Share via Email"));
        });

    }

    // Function to handle percentage breakdown
    private void handlePercentageBreakdown(Intent intent) {
        //Find existing views
        resultLayout = findViewById(R.id.ll_result);
        TextView titleValue = findViewById(R.id.tv_titleValue);
        TextView whoPaysValue = findViewById(R.id.tv_whoPaysValue);
        TextView dateValue = findViewById(R.id.tv_dateValue);
        TextView billValue = findViewById(R.id.tv_billValue);
        TextView noOfPplValue = findViewById(R.id.tv_noOfPplValue);

        //Button
        okButton = findViewById(R.id.bt_ok);
        saveResultButton = findViewById(R.id.bt_saveResult);
        shareResultButton = findViewById(R.id.bt_shareResult);

        String title = intent.getStringExtra("title");
        titleValue.setText(title);

        String whoPays = intent.getStringExtra("whoPays");
        whoPaysValue.setText(whoPays);

        String date = intent.getStringExtra("date");
        dateValue.setText(date);

        String bill = intent.getStringExtra("bill");
        billValue.setText("RM" + bill);

        String noOfPpl = intent.getStringExtra("noOfPpl");
        noOfPplValue.setText(noOfPpl);

        String eachPersonPays = intent.getStringExtra("eachPersonPays");

        if(eachPersonPays != null){
            TextView textView = new TextView(this);
            textView.setText(eachPersonPays);
            textView.setId(View.generateViewId());
            textView.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            resultLayout.addView(textView, layoutParams);
        } else{
            String percentage;
            String personPays;
            String eachPerson = "";

            eachPersonPays = " ";

            for (int i = 1; i <= Integer.parseInt(noOfPpl); i++) {
                percentage = intent.getStringExtra("percentage" + i);
                personPays = intent.getStringExtra("person" + i);

                eachPerson = "Person " + i + " (" + percentage + "%) pays: RM" + personPays;

                TextView textView = new TextView(this);
                textView.setText(eachPerson);
                textView.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                resultLayout.addView(textView, layoutParams);

                eachPersonPays = eachPersonPays + eachPerson + "\n";
            }
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(mainActivityIntent);

            }
        });

        String staticEachPersonPays = eachPersonPays;
        saveResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mySQLiteAdapter = new SQLiteAdapter(getApplicationContext());
                mySQLiteAdapter.openToWrite();
                mySQLiteAdapter.insert( "Percentage Breakdown - " + title, whoPays, date, bill, noOfPpl, staticEachPersonPays);
                mySQLiteAdapter.close();

                Toast.makeText(getApplicationContext(), "Breakdown Result Saved Successfully!", Toast.LENGTH_SHORT).show();

            }
        });

        shareResultButton.setOnClickListener(view -> {
            String subject = "Restaurant Expense Breakdown for " + title;
            String body = "Details are as follows: " + "\n"
                    + "Who Pays: " + whoPays + "\n"
                    + "Date: " + date + "\n"
                    + "Total Bill: RM" + bill + "\n"
                    + "Number of People: " + noOfPpl + "\n"
                    + "Breakdown Result:\n" + staticEachPersonPays;
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(emailIntent, "Share via Email"));
        });
    }

    // Function to handle amount breakdown
    private void handleAmountBreakdown(Intent intent){
        //Find existing views
        resultLayout = findViewById(R.id.ll_result);
        TextView titleValue = findViewById(R.id.tv_titleValue);
        TextView whoPaysValue = findViewById(R.id.tv_whoPaysValue);
        TextView dateValue = findViewById(R.id.tv_dateValue);
        TextView billValue = findViewById(R.id.tv_billValue);
        TextView noOfPplValue = findViewById(R.id.tv_noOfPplValue);

        //Button
        okButton = findViewById(R.id.bt_ok);
        saveResultButton = findViewById(R.id.bt_saveResult);
        shareResultButton = findViewById(R.id.bt_shareResult);

        String title = intent.getStringExtra("title");
        titleValue.setText(title);

        String whoPays = intent.getStringExtra("whoPays");
        whoPaysValue.setText(whoPays);

        String date = intent.getStringExtra("date");
        dateValue.setText(date);

        String bill = intent.getStringExtra("bill");
        billValue.setText("RM" + bill);

        String noOfPpl = intent.getStringExtra("noOfPpl");
        noOfPplValue.setText(noOfPpl);

        String eachPersonPays = intent.getStringExtra("eachPersonPays");

        if(eachPersonPays != null){
            TextView textView = new TextView(this);
            textView.setText(eachPersonPays);
            textView.setId(View.generateViewId());
            textView.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            resultLayout.addView(textView, layoutParams);
        } else {
            String amount;
            String eachPerson = "";

            eachPersonPays = "";
            for (int i = 1; i <= Integer.parseInt(noOfPpl); i++) {
                amount = intent.getStringExtra("person" + i);

                eachPerson = "Person " +  i + " pays: RM" + amount;

                TextView textView = new TextView(this);
                textView.setText(eachPerson);
                textView.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                resultLayout.addView(textView, layoutParams);

                eachPersonPays = eachPersonPays + eachPerson + "\n";
            }
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(mainActivityIntent);
            }
        });

        String staticEachPersonPays = eachPersonPays;
        saveResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mySQLiteAdapter = new SQLiteAdapter(getApplicationContext());
                mySQLiteAdapter.openToWrite();
                mySQLiteAdapter.insert( "Amount Breakdown - " + title, whoPays, date, bill, noOfPpl, staticEachPersonPays);
                mySQLiteAdapter.close();

                Toast.makeText(getApplicationContext(), "Breakdown Result Saved Successfully!", Toast.LENGTH_SHORT).show();

            }
        });

        shareResultButton.setOnClickListener(view -> {
            String subject = "Restaurant Expense Breakdown for " + title;
            String body = "Details are as follows: " + "\n"
                    + "Who Pays: " + whoPays + "\n"
                    + "Date: " + date + "\n"
                    + "Total Bill: RM" + bill + "\n"
                    + "Number of People: " + noOfPpl + "\n"
                    + "Breakdown Result:\n" + staticEachPersonPays;
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(emailIntent, "Share via Email"));
        });
    }

}

