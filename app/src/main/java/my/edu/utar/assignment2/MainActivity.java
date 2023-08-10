package my.edu.utar.assignment2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteAdapter mySQLiteAdapter;

    // Array to store the content retrieved from SQLite
    String[] content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addEntryButton = findViewById(R.id.bt_addEntry);
        LinearLayout entriesLayout = findViewById(R.id.ll_entries);

        // Retrieve the resource identifier of a layout file
        int layoutResourceId = getResources().getIdentifier("breakdown_entry", "layout", getPackageName());

        // Check if the layout resource ID is valid
        if (layoutResourceId == 0) {
            Toast.makeText(this, "Invalid layout resource ID", Toast.LENGTH_SHORT).show();
            return;
        }

        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();
        String contentRead = mySQLiteAdapter.queueAll();
        mySQLiteAdapter.close();

        // Array to store content retrieved from SQLite
        content = contentRead.split(";");

        // Start to display entries if at least one entry exists
        if(content.length > 5){
            // Check if the entry if odd or even
            int check = 1;
            View view = new View(this);
            for (int i = content.length; i >= 1; i--) {
                TextView textView = new TextView(this);
                if (i % 6 == 1) {
                    textView = view.findViewById(R.id.tv_title);
                    textView.setText(content[i - 1]);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                            TextView tempView;

                            tempView = view.findViewById(R.id.tv_title);
                            String breakdownTitle = (String)tempView.getText();

                            if(breakdownTitle.contains("Equal Breakdown")){
                                intent.putExtra("source", "Equal Breakdown");
                            } else if(breakdownTitle.contains("Percentage Breakdown")){
                                intent.putExtra("source", "Percentage Breakdown");
                            } else{
                                intent.putExtra("source", "Amount Breakdown");
                            }
                            tempView = view.findViewById(R.id.tv_bill);
                            String breakdownBill = (String)tempView.getText();
                            tempView = view.findViewById(R.id.tv_noOfPpl);
                            String breakdownPeople = (String)tempView.getText();
                            tempView = view.findViewById(R.id.tv_whoPays);
                            String breakdownWhoPays = (String)tempView.getText();
                            tempView = view.findViewById(R.id.tv_date);
                            String breakdownDate = (String)tempView.getText();
                            tempView = view.findViewById(R.id.tv_result);
                            String breakdownResult = (String)tempView.getText();

                            intent.putExtra("title", breakdownTitle);
                            intent.putExtra("whoPays", breakdownWhoPays);
                            intent.putExtra("bill", breakdownBill);
                            intent.putExtra("noOfPpl", breakdownPeople);
                            intent.putExtra("date", breakdownDate);
                            intent.putExtra("eachPersonPays", breakdownResult);
                            startActivity(intent);
                        }
                    });
                } else if (i % 6 == 2){
                    textView = view.findViewById(R.id.tv_whoPays);
                    textView.setText(content[i - 1]);
                } else if (i % 6 == 3) {
                    textView = view.findViewById(R.id.tv_date);
                    textView.setText(content[i - 1]);
                } else if (i % 6 == 4) {
                    textView = view.findViewById(R.id.tv_bill);
                    textView.setText(content[i - 1]);
                } else if (i % 6 == 5) {
                    textView = view.findViewById(R.id.tv_noOfPpl);
                    textView.setText(content[i - 1]);
                } else if (i % 6 == 0){
                    view = LayoutInflater.from(this).inflate(layoutResourceId, entriesLayout, false);
                    if(check == 1){
                        view.setBackgroundColor(0xFFB9B1D4);
                    }
                    check = -check;
                    textView = view.findViewById(R.id.tv_result);
                    textView.setText(content[i - 1]);
                    entriesLayout.addView(view);
                }
            }
        }

        addEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent breakdownActivityIntent = new Intent(getApplicationContext(), BreakDownActivity.class);
                startActivity(breakdownActivityIntent);
            }


        });
    }
}
