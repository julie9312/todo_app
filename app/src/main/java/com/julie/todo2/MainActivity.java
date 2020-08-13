package com.julie.todo2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.julie.todo2.adapter.RecyclerViewAdapter;
import com.julie.todo2.todo.Todo;
import com.julie.todo2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    RecyclerViewAdapter adapter;
    ArrayList<Todo> todoArrayList = new ArrayList<>();

    RequestQueue requestQueue;

    // 페이징 처리를 위한 변수
    int offset = 0;
    int limit = 25;
    int cnt;

    // 정렬을 위한 변수
    String order;
    String path = "/api/v1/todo";
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if( (lastPosition + 1) == totalCount){
                    if(cnt == limit) {
                        // 네트워크 통해서, 데이터를 더 불러오면 된다.
                        addNetworkData(path);
                    }
                }
            }
        });

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        path = "/api/v1/todo";

        getNetworkData(path);
    }

        private void getNetworkData(String path) {

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    Util.BASE_URL +  path + "?offset="+offset,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                boolean success = response.getBoolean("success");
                                if(success == false){
                                    // 유저한테 에러있다고 알리고 리턴.
                                    return;
                                }
                                JSONArray items = response.getJSONArray("result");
                                for(int i = 0; i < items.length(); i++){
                                    int id = items.getJSONObject(i).getInt("id");
                                    String title = items.getJSONObject(i).getString("title");
                                    String date = items.getJSONObject(i).getString("date");
                                    String completed = items.getJSONObject(i).getString("completed");

                                    Todo todo = new Todo(id, title, date, completed);
                                    todoArrayList.add(todo);
                                }

                                adapter = new RecyclerViewAdapter(MainActivity.this, todoArrayList);
                                recyclerView.setAdapter(adapter);

                                offset = offset + items.length();
                                cnt = offset;


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );
            requestQueue.add(request);
        }


        private void addNetworkData(String path) {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    Util.BASE_URL +  path + "?offset="+offset,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                boolean success = response.getBoolean("success");
                                if(success == false){
                                    // 유저한테 네트워크 문제있다고 알려준다.
                                    return;
                                }
                                JSONArray items = response.getJSONArray("result");
                                for(int i = 0; i < items.length(); i++){
                                    int id = items.getJSONObject(i).getInt("id");
                                    String title = items.getJSONObject(i).getString("title");
                                    String date = items.getJSONObject(i).getString("date");
                                    String completed = items.getJSONObject(i).getString("completed");

                                    Todo todo = new Todo(id, title, date, completed);
                                    todoArrayList.add(todo);
                                }

                                adapter.notifyDataSetChanged();

                                // 페이징을 위해서 오프셋을 변경시켜놔야 한다.
                                offset = offset + response.getInt("cnt");
                                cnt = response.getInt("cnt");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );
            requestQueue.add(request);
        }

    public void addCompleted(final int position) {

        Todo todo = todoArrayList.get(position);
        int todo_id = todo.getId();

        JSONObject body = new JSONObject();
        try {
            body.put("todoId", todo_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Util.BASE_URL + "/api/v1/todo",
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 어레이리스트의 값을 변경시켜줘야 한다.
                        Todo todo = todoArrayList.get(position);
                        if(todo.getCompleted().equals("0")) {
                            todo.setCompleted("1");
                        } else {
                            todo.setCompleted("0");
                        }

                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        Volley.newRequestQueue(MainActivity.this).add(request);
    }
}