package com.example.savepreciouswildlife.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savepreciouswildlife.DataBaseHelper;
import com.example.savepreciouswildlife.MainActivity;
import com.example.savepreciouswildlife.models.Patient;
import com.example.savepreciouswildlife.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import org.json.JSONException;

import java.math.BigDecimal;
// @ author Kateryna
// in this activity I made a PayPal configuration and passed a donated sum to PayPal service for proceeding
public class DonateSumActivity extends AppCompatActivity {
    DataBaseHelper db;
    EditText donatedSum;
    Button buttonPressDonate;
    Button buttonReturn;

    int sumDonated;
    int patientID;
    int sumNeedOld;
    int sumDonatedOld;
    int sumLeftOld;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }
    // @author Kateryna
    // PayPal configuration instance
    public  static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(com.example.savepreciouswildlife.PayPalConfiguration.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_sum);
        // @ author Kateryna.
        // Here we start PayPal service
        Intent intent = new Intent(DonateSumActivity.this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
        startService(intent);
        // receiving bundle with information about the bird we want to donate for
        try{
            Bundle message = getIntent().getExtras();
            if(message != null) {
                patientID =getIntent().getExtras().getInt("ID_PATIENT");
                sumNeedOld =getIntent().getExtras().getInt("SUMNEED_PATIENT");
                sumDonatedOld =getIntent().getExtras().getInt("SUMDONATED_PATIENT");
                sumLeftOld =getIntent().getExtras().getInt("SUMLEFT_PATIENT");
            }

        } catch(Exception ex) {
            Log.e("FORM", ex.getMessage());
        }
        TextView bundleResults = findViewById(R.id.textViewBundleResult);
        String string = "Patient id: " + patientID + "\n" +
                "Sum need for treatment: " + sumNeedOld + "\n" +
                "Sum already donated: " + sumDonatedOld + "\n" +
                "Sum left: " + sumLeftOld;
        bundleResults.setText(string);
        donatedSum = findViewById(R.id.editTextDonateSum);
        buttonPressDonate = findViewById(R.id.buttonPressDonate);
        buttonReturn = findViewById(R.id.buttonReturn);

        buttonPressDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                processPayment();
            }
        });

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DonateSumActivity.this, MainActivity.class));
            }
        });

    }
    private void processPayment() {
        try {
            sumDonated = Integer.parseInt(donatedSum.getText().toString());
            if(sumDonated > sumLeftOld) {
                Toast.makeText(DonateSumActivity.this,"Please, donate the sum not greater than " + sumLeftOld, Toast.LENGTH_SHORT).show();
            }
            else if(sumDonated == 0) {
                Toast.makeText(DonateSumActivity.this,"Please, donate the sum greater than 0", Toast.LENGTH_SHORT).show();
            }
            else {

                PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(sumDonated)),"USD","Donate for birds",
                        PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(this,PaymentActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
                startActivityForResult(intent,PAYPAL_REQUEST_CODE);
            }
        }
         catch(Exception ex) {
            Log.e("DONATE", ex.getMessage());
            Toast.makeText(DonateSumActivity.this,"Donation field cannot be empty!", Toast.LENGTH_SHORT).show();
         }


    }
    // @author Kateryna
    // in this method if the payment process executed successfully DB helper class is opened and information about the donated sum and
    // all related amounts are witten in the database
    //information about the Paypal payment will put in the instance of JSONObject and go to the PaymentDetailsActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        int sumDonatedNew;
                        int sumLeftNew;
                        sumDonatedNew = sumDonatedOld + sumDonated;
                        sumLeftNew = sumLeftOld - sumDonated;
                        db = new DataBaseHelper(getApplicationContext());
                        Patient updatedPatient = new Patient(patientID,sumNeedOld,sumDonatedNew,sumLeftNew);
                        db.updatePatient(updatedPatient);
                        db.closeDB();
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        Intent intent = new Intent(this, PaymentDetailsActivity.class);
                        intent.putExtra("PaymentDetails", paymentDetails);
                        intent.putExtra("PaymentAmount", String.valueOf(sumDonated));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (resultCode == PaymentActivity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancel!", Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(this, "Invalid!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}