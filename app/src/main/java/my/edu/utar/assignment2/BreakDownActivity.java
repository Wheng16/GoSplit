package my.edu.utar.assignment2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class BreakDownActivity extends AppCompatActivity {

    private LinearLayout percentageLayout,  amountLayout;
    private LinearLayout additionalPercentageLayout, additionalAmountLayout;
    private int percentageFieldCount = 0, amountFieldCount = 0; // Starting field count, excluding the initial input field

    // Title of the breakdown form
    private TextView titleCreate;

    // Button to handle different breakdown types
    private Button equalButton, percentageButton, amountButton;

    // Date Picker
    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_down);

        // Initiate date picker
        initDatePicker();
        dateButton = findViewById(R.id.bt_date);
        dateButton.setText(getTodaysDate());

        // Find existing views
        EditText title = findViewById(R.id.et_title);
        EditText whoPays = findViewById(R.id.et_whoPays);
        EditText bill = findViewById(R.id.et_bill);
        EditText noOfPpl = findViewById(R.id.et_noOfPpl);

        //EditText date = findViewById(R.id.et_date);
        percentageLayout = findViewById(R.id.ll_percentage);
        amountLayout = findViewById(R.id.ll_amount);

        //Equal Breakdown
        equalButton = findViewById(R.id.bt_createEqual);

        //Percentage Breakdown
        percentageButton = findViewById(R.id.bt_createPercentage); //create button to add percentage
        additionalPercentageLayout = findViewById(R.id.ll_addPercentage); //create layout to store the buttons

        //Amount Breakdown
        amountButton = findViewById(R.id.bt_createAmount);
        additionalAmountLayout = findViewById(R.id.ll_addAmount);

        // Button to handle equal breakdown
        equalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get and store breakdown info
                double breakdownBill = Double.parseDouble(bill.getText().toString());
                int breakdownPeople = Integer.parseInt(noOfPpl.getText().toString());
                String breakdownTitle = title.getText().toString();
                String breakdownWhoPays = whoPays.getText().toString();
                String breakdownDate = dateButton.getText().toString();
                double eachPersonPays = breakdownBill / breakdownPeople;

                // Pass the breakdown info to ResultActivity
                Intent intent = new Intent(BreakDownActivity.this, ResultActivity.class);
                intent.putExtra("source", "Equal Breakdown");
                intent.putExtra("title", breakdownTitle);
                intent.putExtra("whoPays", breakdownWhoPays);
                intent.putExtra("bill", String.format("%.2f", breakdownBill));
                intent.putExtra("noOfPpl", Integer.toString(breakdownPeople));
                intent.putExtra("date", breakdownDate);
                intent.putExtra("eachPersonPays", String.format("%.2f", eachPersonPays));
                startActivity(intent);

            }
        });

        // Button to handle percentage breakdown
        percentageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get and store breakdown info
                double breakdownBill = Double.parseDouble(bill.getText().toString());
                int breakdownPeople = Integer.parseInt(noOfPpl.getText().toString());
                String breakdownTitle = title.getText().toString();
                String breakdownWhoPays = whoPays.getText().toString();
                String breakdownDate = dateButton.getText().toString();

                // Pass the breakdown info to ResultActivity
                Intent intent = new Intent(BreakDownActivity.this, ResultActivity.class);
                intent.putExtra("source", "Percentage Breakdown");
                intent.putExtra("title", breakdownTitle);
                intent.putExtra("whoPays", breakdownWhoPays);
                intent.putExtra("bill", Double.toString(breakdownBill));
                intent.putExtra("noOfPpl", Integer.toString(breakdownPeople));
                intent.putExtra("date", breakdownDate);

                //
                int totalPercentage = 0;
                double totalPays = 0;
                for (int i = 1; i <= percentageFieldCount; i++) {
                    View childView = additionalPercentageLayout.getChildAt(i - 1);
                    if (childView instanceof EditText) {
                        EditText percentageEditText = (EditText) childView;
                        int percentage = Integer.parseInt(percentageEditText.getText().toString());
                        double personPays = breakdownBill * ((double)percentage / 100);

                        totalPercentage += percentage;
                        totalPays += personPays;

                        // Pass the person:percentage to ResultActivity
                        intent.putExtra("percentage" + i, Integer.toString(percentage));
                        intent.putExtra("person" + i, String.format("%.2f", personPays));
                    }
                }

                // Check if there are people who has not been assigned percentage in the breakdown
                // Equally assign them the rest of the percentage to them
                int morePeople = breakdownPeople - percentageFieldCount;
                if(morePeople > 0){
                    int equalPercentage = (100 - totalPercentage) / morePeople;
                    double equalPays = (breakdownBill - totalPays) / morePeople;
                    for(int i = 1; i <= morePeople; i++){
                        intent.putExtra("percentage" + (percentageFieldCount + i), Integer.toString(equalPercentage));
                        intent.putExtra("person" + (percentageFieldCount + i), String.format("%.2f", equalPays));
                    }
                }

                startActivity(intent);
            }
        });

        // Button to start amount breakdown
        amountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get and store breakdown info
                double breakdownBill = Double.parseDouble(bill.getText().toString());
                int breakdownPeople = Integer.parseInt(noOfPpl.getText().toString());
                String breakdownTitle = title.getText().toString();
                String breakdownWhoPays = whoPays.getText().toString();
                String breakdownDate = dateButton.getText().toString();

                // Check if the sum of the individual amount = total bill amount
                double[] eachAmount = new double[20];
                double totalAmount = 0;

                for (int i = 1; i <= amountFieldCount; i++){
                    View childView = additionalAmountLayout.getChildAt((i - 1));
                    EditText amountEditText = (EditText) childView;
                    double amount = Double.parseDouble(amountEditText.getText().toString());

                    totalAmount += amount;
                    eachAmount[i - 1] = amount;
                }

                if(totalAmount != breakdownBill){
                    Toast.makeText(getApplicationContext(),
                            "The sum of the individual amount must be EQUAL to total bill amount entered", Toast.LENGTH_SHORT).show();
                } else{
                    // Pass the breakdown info to ResultActivity
                    Intent intent = new Intent(BreakDownActivity.this, ResultActivity.class);
                    intent.putExtra("source", "Amount Breakdown");
                    intent.putExtra("title", breakdownTitle);
                    intent.putExtra("whoPays", breakdownWhoPays);
                    intent.putExtra("bill", Double.toString(breakdownBill));
                    intent.putExtra("noOfPpl", Integer.toString(breakdownPeople));
                    intent.putExtra("date", breakdownDate);

                    for(int i = 1; i <= eachAmount.length; i++){
                        intent.putExtra("person" + i, String.format("%.2f", eachAmount[i - 1]));
                    }

                    startActivity(intent);
                }
            }
        });


    }

    // Listeners to hide and show buttons
    public void onEqualClick(View view){
        titleCreate = findViewById(R.id.tv_create);
        titleCreate.setText("New Equal Breakdown");

        //Set buttons' visibility
        equalButton.setVisibility(View.VISIBLE);
        percentageButton.setVisibility(View.GONE);
        amountButton.setVisibility(View.GONE);

        percentageLayout.setVisibility(View.GONE);
        amountLayout.setVisibility(View.GONE);
    }

    public void onPercentageClick(View view){
        titleCreate = findViewById(R.id.tv_create);
        titleCreate.setText("New Percentage Breakdown");

        //Set buttons' visibility
        percentageButton.setVisibility(View.VISIBLE);
        equalButton.setVisibility(View.GONE);
        amountButton.setVisibility(View.GONE);

        percentageLayout.setVisibility(View.VISIBLE);
        amountLayout.setVisibility(View.GONE);
    }

    public void onAmountClick(View view){
        titleCreate = findViewById(R.id.tv_create);
        titleCreate.setText("New Amount Breakdown");

        //Set buttons' visibility
        amountButton.setVisibility(View.VISIBLE);
        equalButton.setVisibility(View.GONE);
        percentageButton.setVisibility(View.GONE);

        percentageLayout.setVisibility(View.GONE);
        amountLayout.setVisibility(View.VISIBLE);
    }

    // Listener to add and set percentage field dynamically
    public void onAddPercentageClick(View view) {
        EditText editText = new EditText(this);

        percentageFieldCount++;

        editText.setId(View.generateViewId());
        editText.setHint("Enter Percentage " + percentageFieldCount + " (e.g. 50)");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        additionalPercentageLayout.addView(editText, layoutParams);

    }

    // Listener to add and set amount field dynamically
    public void onAddAmountClick(View view) {
        EditText editText = new EditText(this);

        amountFieldCount++;

        editText.setId(View.generateViewId());
        editText.setHint("Enter Amount " + amountFieldCount + " (e.g. 100.00)");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        additionalAmountLayout.addView(editText, layoutParams);

    }

    public void onOpenDatePickerClick(View view) {
        datePickerDialog.show();
    }

    private String getTodaysDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);

    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month){

        if(month == 1){
            return "JAN";
        } else if(month == 2){
            return "FEB";
        } else if(month == 3){
            return "MAR";
        } else if(month == 4){
            return "APR";
        } else if(month == 5){
            return "MAY";
        } else if(month == 6){
            return "JUN";
        } else if(month == 7){
            return "JUL";
        } else if(month == 8){
            return "AUG";
        } else if(month == 9){
            return "SEP";
        } else if(month == 10){
            return "OCT";
        } else if(month == 11){
            return "NOV";
        } else if(month == 12){
            return "DEC";
        } else{
            return "JAN";
        }
    }
}