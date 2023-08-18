package com.example.atlantatest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.atlantatest.database.UserDao;
import com.example.atlantatest.database.UserDatabase;
import com.example.atlantatest.response.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersActivity extends AppCompatActivity {

    private UserAdapter userAdapter;
    private RecyclerView recyclerView;
    private EditText edtSearch;
    private UserDao userDao;
    private List<User> userArrayList = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        recyclerView = findViewById(R.id.recyclerView);
        edtSearch = findViewById(R.id.edtSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

            getUsersList();

            getAllUsers();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void getUsersList(){
        Call<JsonArray> call = RetrofitClient.getApiInterface().getUsersList();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                assert response.body() != null;


                if (response.body().size() != 0){

                    for (int i=0; i<response.body().size(); i++){
                        //response.body().get(i).getAsJsonObject();
                        Gson gson = new Gson();
                        TypeToken<List<User>> token = new TypeToken<List<User>>(){};
                        userArrayList = gson.fromJson(response.body(), token.getType());
                    }
                    /*userAdapter = new UserAdapter(userArrayList);
                    recyclerView.setAdapter(userAdapter);
                    userAdapter.notifyDataSetChanged();*/
                    insertUser();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }
    private void getAllUsers(){

        UserDatabase userDatabase = UserDatabase.getDatabase(getApplicationContext());
        userDao = userDatabase.UserDao();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                users = userDao.getAllUsers();
            }
        });
        userAdapter = new UserAdapter(users);
        recyclerView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }
    private void insertUser(){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                userDao.insertAllUsers(userArrayList);
            }
        });
    }
}