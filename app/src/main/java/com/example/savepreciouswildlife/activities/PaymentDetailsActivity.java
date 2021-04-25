package com.example.savepreciouswildlife.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.savepreciouswildlife.MainActivity;
import com.example.savepreciouswildlife.R;

import org.json.JSONException;
import org.json.JSONObject;
// @author Kateryna.
// Activity that will get the bundle from PayPal and show the result of the payment: id, donated sum,
// status of the transaction
public class PaymentDetailsActivity extends AppCompatActivity {
    TextView details, amount, status;
    Button buttonReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        details = findViewById(R.id.textViewPayPalResults);
        amount = findViewById(R.id.textViewPayPalAmount);
        status = findViewById(R.id.textViewPayPalStatus);
        Intent intent = getIntent();
        // author Kateryna
        //JSONObject instance keeps information about the payment
        try {
            JSONObject object = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(object.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        buttonReturn = findViewById(R.id.buttonReturnToMain);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentDetailsActivity.this, MainActivity.class));
            }
        });
    }
    private void showDetails(JSONObject response,String paymentAmount) {
        try {
            details.setText("id: " + response.getString("id"));
            status.setText("Status: " + response.getString("state"));
            amount.setText("Donated: USD $ " + paymentAmount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}