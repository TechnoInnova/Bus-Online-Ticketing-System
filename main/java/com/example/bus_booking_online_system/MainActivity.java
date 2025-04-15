package com.example.bus_booking_online_system;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editTextFrom, editTextTo;
    Button btnSearch, btnBook, btnDownloadTicket, btnIHavePaid;
    TextView textViewBus, textQrLabel, paymentStatus, ticketDetails;
    ImageView qrImageView;

    String selectedFrom, selectedTo, selectedTime, selectedFare, selectedBusName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        editTextFrom = findViewById(R.id.editTextFrom);
        editTextTo = findViewById(R.id.editTextTo);
        btnSearch = findViewById(R.id.btnSearch);
        btnBook = findViewById(R.id.btnBook);
        btnDownloadTicket = findViewById(R.id.btnDownloadTicket);
        btnIHavePaid = findViewById(R.id.btnIHavePaid);
        textViewBus = findViewById(R.id.textViewBus);
        qrImageView = findViewById(R.id.qrImageView);
        textQrLabel = findViewById(R.id.textQrLabel);
        paymentStatus = findViewById(R.id.paymentStatus);
        ticketDetails = findViewById(R.id.ticketDetails);

        // Initially hide views
        btnBook.setVisibility(View.GONE);
        textViewBus.setVisibility(View.GONE);
        qrImageView.setVisibility(View.GONE);
        textQrLabel.setVisibility(View.GONE);
        paymentStatus.setVisibility(View.GONE);
        btnDownloadTicket.setVisibility(View.GONE);
        btnIHavePaid.setVisibility(View.GONE);
        ticketDetails.setVisibility(View.GONE);

        // Search Buses
        btnSearch.setOnClickListener(v -> {
            selectedFrom = editTextFrom.getText().toString().trim();
            selectedTo = editTextTo.getText().toString().trim();

            if (selectedFrom.isEmpty() || selectedTo.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Bus data array
                BusInfo[] buses = {
                        new BusInfo("Metro Travels", "9:00 AM", "₹250"),
                        new BusInfo("Rajdhani Express", "11:30 AM", "₹300"),
                        new BusInfo("Volvo Deluxe", "1:00 PM", "₹400"),
                        new BusInfo("Orange Tours", "3:45 PM", "₹280"),
                        new BusInfo("MSRTC Shivneri", "6:15 PM", "₹350")
                };

                // Randomly select a bus
                int index = (int) (Math.random() * buses.length);
                BusInfo selectedBus = buses[index];
                selectedBusName = selectedBus.name;
                selectedTime = selectedBus.time;
                selectedFare = selectedBus.fare;

                textViewBus.setText(selectedBusName + ": " + selectedFrom + " to " + selectedTo +
                        "\nTime: " + selectedTime + "\nFare: " + selectedFare);
                textViewBus.setVisibility(View.VISIBLE);
                btnBook.setVisibility(View.VISIBLE);
            }
        });

        // Book Ticket & Generate QR
        btnBook.setOnClickListener(v -> {
            String fareAmount = selectedFare.replaceAll("[^\\d]", ""); // Remove ₹ symbol
            String paymentLink = "upi://pay?pa=yourupiid@bank&pn=BusBooking&am=" + fareAmount + "&cu=INR";

            try {
                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap qrBitmap = encoder.encodeBitmap(paymentLink, BarcodeFormat.QR_CODE, 400, 400);
                qrImageView.setImageBitmap(qrBitmap);

                qrImageView.setVisibility(View.VISIBLE);
                textQrLabel.setText("Scan this QR to Pay");
                textQrLabel.setVisibility(View.VISIBLE);
                btnIHavePaid.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "QR Generation Failed", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        // Simulate Manual Payment Confirmation
        btnIHavePaid.setOnClickListener(v -> {
            paymentStatus.setText("✅ Payment Successful!");
            paymentStatus.setVisibility(View.VISIBLE);

            ticketDetails.setText("🎫 Ticket Details\nBus: " + selectedBusName +
                    "\nFrom: " + selectedFrom + "\nTo: " + selectedTo +
                    "\nTime: " + selectedTime + "\nFare: " + selectedFare);
            ticketDetails.setVisibility(View.VISIBLE);

            btnDownloadTicket.setVisibility(View.VISIBLE);
        });

        // Download Ticket
        btnDownloadTicket.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Ticket downloaded successfully!", Toast.LENGTH_SHORT).show();
            // TODO: Implement PDF generation logic here
        });
    }

    // Inner class to represent Bus Information
    class BusInfo {
        String name;
        String time;
        String fare;

        BusInfo(String name, String time, String fare) {
            this.name = name;
            this.time = time;
            this.fare = fare;
        }
    }
}
