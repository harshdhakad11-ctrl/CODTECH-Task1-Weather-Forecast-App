package com.dhakad.weatherapp.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dhakad.weatherapp.R;

import org.json.JSONObject;

import com.bumptech.glide.Glide;
import android.widget.ImageView;

import android.app.AlertDialog;
import android.widget.EditText;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import android.location.Geocoder;
import android.location.Address;

import java.util.List;
import java.util.Locale;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhakad.weatherapp.adapters.HourlyAdapter;
import com.dhakad.weatherapp.adapters.WeeklyAdapter;
import com.dhakad.weatherapp.models.HourlyModel;
import com.dhakad.weatherapp.models.WeeklyModel;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class MainActivity extends AppCompatActivity {

    // UI Views
    private TextView txtCity;
    private TextView txtDate;
    private TextView txtTime;
    private TextView txtTemp;
    private TextView txtCondition;
    private TextView txtHumidity;
    private TextView txtWind;
    private TextView txtRain;

    // Top buttons
    private ImageButton btnSearch;
    private ImageButton btnLocation;

    private ImageView imgWeather;
    // Location Client
    private FusedLocationProviderClient fusedLocationClient;

    // Permission Launcher
    private ActivityResultLauncher<String> locationPermissionLauncher;
    // RecyclerViews
    private RecyclerView hourlyRecycler;
    private RecyclerView weeklyRecycler;

    // Adapters
    private HourlyAdapter hourlyAdapter;
    private WeeklyAdapter weeklyAdapter;

    // Data Lists
    private ArrayList<HourlyModel> hourlyList = new ArrayList<>();
    private ArrayList<WeeklyModel> weeklyList = new ArrayList<>();

    // WeatherAPI Key
    private static final String API_KEY = "06b238111e0d4570942105816260207";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Register permission launcher
        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {

                    if (isGranted) {

                        getCurrentLocation();

                    } else {

                        Toast.makeText(
                                this,
                                "Location permission denied",
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                });

        // Initialize Views
        txtCity = findViewById(R.id.txtCity);
        txtTemp = findViewById(R.id.txtTemp);
        txtCondition = findViewById(R.id.txtCondition);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtWind = findViewById(R.id.txtWind);
        txtRain = findViewById(R.id.txtRain);

        btnSearch = findViewById(R.id.btnSearch);
        btnLocation = findViewById(R.id.btnLocation);

        imgWeather = findViewById(R.id.imgWeather);
        // ===============================
        // Initialize RecyclerViews
        // ===============================

        hourlyRecycler = findViewById(R.id.hourlyRecycler);
        weeklyRecycler = findViewById(R.id.weeklyRecycler);

       // Hourly RecyclerView
        hourlyRecycler.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false));
        hourlyRecycler.setHasFixedSize(true);

        hourlyAdapter = new HourlyAdapter(hourlyList);
        hourlyRecycler.setAdapter(hourlyAdapter);

       // Weekly RecyclerView
        weeklyRecycler.setLayoutManager(
                new LinearLayoutManager(this));

        weeklyAdapter = new WeeklyAdapter(weeklyList);
        weeklyRecycler.setAdapter(weeklyAdapter);

        // Load default city weather
        getWeather("New Delhi");

        // Open search dialog
        btnSearch.setOnClickListener(v -> {

            // Create EditText for city input
            EditText input = new EditText(MainActivity.this);

            input.setHint("Enter City Name");

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle("Search City");

            builder.setView(input);

            // Search button
            builder.setPositiveButton("Search", (dialog, which) -> {

                String city = input.getText().toString().trim();

                if (!city.isEmpty()) {

                    getWeather(city);

                } else {

                    Toast.makeText(
                            MainActivity.this,
                            "Please enter city",
                            Toast.LENGTH_SHORT
                    ).show();

                }

            });

            // Cancel button
            builder.setNegativeButton("Cancel", null);

            builder.show();

        });

        // Location button click
        btnLocation.setOnClickListener(v -> {

            if (ContextCompat.checkSelfPermission(
                    MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                // Permission already granted
                getCurrentLocation();

            } else {

                // Request permission
                locationPermissionLauncher.launch(
                        Manifest.permission.ACCESS_FINE_LOCATION);

            }

        });
    }

    /**
     * Fetch weather from WeatherAPI
     */
    private void getWeather(String city) {

        String url = "https://api.weatherapi.com/v1/forecast.json?key="
                + API_KEY
                + "&q="
                + city
                + "&days=7&aqi=yes&alerts=no";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,

                response -> {

                    try {

                        JSONObject location = response.getJSONObject("location");
                        // Get local date & time from API
                        String localTime = location.getString("localtime");

                        // API format
                        SimpleDateFormat apiFormat =
                                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

                       // Date format
                        SimpleDateFormat dateFormat =
                                new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault());

                       // Time format
                        SimpleDateFormat timeFormat =
                                new SimpleDateFormat("hh:mm a", Locale.getDefault());

                        Date date = apiFormat.parse(localTime);

                        if (date != null) {

                            txtDate.setText(dateFormat.format(date));

                            txtTime.setText(timeFormat.format(date));

                        }

                        JSONObject current = response.getJSONObject("current");

                        JSONObject day = response
                                .getJSONObject("forecast")
                                .getJSONArray("forecastday")
                                .getJSONObject(0)
                                .getJSONObject("day");

                        // Clear old data
                        hourlyList.clear();
                        weeklyList.clear();

                       // Get forecast array
                        JSONArray forecastArray = response
                                .getJSONObject("forecast")
                                .getJSONArray("forecastday");

                        // Update UI
                        txtCity.setText(location.getString("name"));

                        txtTemp.setText(Math.round(current.getDouble("temp_c")) + "°");

                        txtCondition.setText(
                                current.getJSONObject("condition")
                                        .getString("text")
                        );

                        txtHumidity.setText(current.getInt("humidity") + "%");

                        txtWind.setText(Math.round(current.getDouble("wind_kph")) + " km/h");

                        txtRain.setText(day.getInt("daily_chance_of_rain") + "%");

                        // Get weather icon URL from API
                        String iconUrl = current
                                .getJSONObject("condition")
                                .getString("icon");

                       // WeatherAPI returns URL starting with //
                       // Add https:
                        iconUrl = "https:" + iconUrl;

                       // Load icon using Glide
                        Glide.with(MainActivity.this)
                                .load(iconUrl)
                                .into(imgWeather);

                        // ===============================
                        // Today's Hourly Forecast
                        // ===============================

                        JSONArray hourArray = forecastArray
                                .getJSONObject(0)
                                .getJSONArray("hour");

                      // Clear old list
                        hourlyList.clear();

                       // Get current hour (0-23)
                        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

                      // Show forecast from current hour onwards
                        for (int i = currentHour; i < hourArray.length(); i++) {

                            JSONObject hour = hourArray.getJSONObject(i);

                            // Get time (e.g. 13:00)
                            String time = hour.getString("time").substring(11);

                            // Convert 24-hour to 12-hour format
                            SimpleDateFormat input =
                                    new SimpleDateFormat("HH:mm", Locale.getDefault());

                            SimpleDateFormat output =
                                    new SimpleDateFormat("hh a", Locale.getDefault());

                            Date parsedTime = input.parse(time);

                            if (parsedTime != null) {
                                time = output.format(parsedTime);
                            }

                            String temp = Math.round(hour.getDouble("temp_c")) + "°";

                            String icon = "https:"
                                    + hour.getJSONObject("condition")
                                    .getString("icon");

                            hourlyList.add(new HourlyModel(
                                    time,
                                    temp,
                                    icon
                            ));
                        }

                     // Refresh RecyclerView
                        hourlyAdapter.notifyDataSetChanged();

                      // ================================
                      // 7 Days Forecast
                      // ================================

                        for (int i = 0; i < forecastArray.length(); i++) {

                            JSONObject forecastDay = forecastArray.getJSONObject(i);

                            // Date
                            String forecastDate = forecastDay.getString("date");

                            // Convert date to day name
                            java.text.SimpleDateFormat inputFormat =
                                    new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

                            java.text.SimpleDateFormat outputFormat =
                                    new java.text.SimpleDateFormat("EEE", java.util.Locale.getDefault());

                            java.util.Date d = inputFormat.parse(forecastDate);

                            String dayName = outputFormat.format(d);

                            JSONObject dayObject = forecastDay.getJSONObject("day");

                            String maxTemp = Math.round(dayObject.getDouble("maxtemp_c")) + "°";
                            String minTemp = Math.round(dayObject.getDouble("mintemp_c")) + "°";

                            String condition = dayObject
                                    .getJSONObject("condition")
                                    .getString("text");

                            String icon = "https:"
                                    + dayObject
                                    .getJSONObject("condition")
                                    .getString("icon");

                            weeklyList.add(new WeeklyModel(
                                    dayName,
                                    condition,
                                    maxTemp,
                                    minTemp,
                                    icon
                            ));
                        }

// Refresh RecyclerView
                        weeklyAdapter.notifyDataSetChanged();

                    } catch (Exception e) {

                        e.printStackTrace();

                        Toast.makeText(
                                MainActivity.this,
                                e.toString(),
                                Toast.LENGTH_LONG
                        ).show();

                    }

                },

                error -> Toast.makeText(
                        MainActivity.this,
                        "City not found!",
                        Toast.LENGTH_SHORT
                ).show()

        );

        queue.add(request);

    }

    /**
     * Get current location and fetch weather
     */
    private void getCurrentLocation() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {

                    if (location != null) {

                        try {

                            Geocoder geocoder =
                                    new Geocoder(this, Locale.getDefault());

                            List<Address> addresses =
                                    geocoder.getFromLocation(
                                            location.getLatitude(),
                                            location.getLongitude(),
                                            1);

                            if (addresses != null && !addresses.isEmpty()) {

                                String city = addresses.get(0).getLocality();

                                if (city != null) {

                                    getWeather(city);

                                } else {

                                    Toast.makeText(
                                            this,
                                            "City not found",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                }

                            }

                        } catch (Exception e) {

                            e.printStackTrace();

                        }

                    } else {

                        Toast.makeText(
                                this,
                                "Unable to get location",
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                });

    }
}