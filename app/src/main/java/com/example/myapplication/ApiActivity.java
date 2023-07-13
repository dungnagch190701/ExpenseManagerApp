package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.api.ApiService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class ApiActivity extends AppCompatActivity {
    TextView textView,responseCode,userid,number,name,message;
    ArrayList<TripEntity> tripEntities;
    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        textView = findViewById(R.id.text_view_result);
        initView();
        Intent intent = getIntent();
        tripEntities = (ArrayList<TripEntity>) intent.getSerializableExtra("BUNDLE");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cw1786.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
        createPost();
    }
    private void initView() {
        responseCode = findViewById(R.id.response_code);
        userid = findViewById(R.id.user_id);
        number = findViewById(R.id.number);
        name = findViewById(R.id.name);
        message = findViewById(R.id.message);
    }
    private void createPost() {
        ApiModel apiModel = new ApiModel("dungnagch190701",tripEntities);
        Gson gson = new Gson();
        String json = gson.toJson(apiModel);
        Call<Post> call = apiService.createPost(json);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    textView.setText("Code: " + response.code());
                    return;
                }
                Post posts = response.body();
                responseCode.setText(posts.getUploadResponseCode());
                userid.setText(posts.getUserid());
                number.setText(String.valueOf(posts.getNumber()));
                name.setText(posts.getNames());
                message.setText(posts.getMessage());
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });

    }
}