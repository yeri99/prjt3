package com.example.sdsd;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sdsd.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private com.example.sdsd.SearchAdapter adapter;
    private List<com.example.sdsd.SearchItem> searchList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        fillSearchList();
        setUpRecyclerView();
    }

    private void fillSearchList(){
        searchList = new ArrayList<>();
        searchList.add(new com.example.sdsd.SearchItem("계란"));
        searchList.add(new com.example.sdsd.SearchItem("가스용기"));
        searchList.add(new com.example.sdsd.SearchItem("신문지"));
        searchList.add(new com.example.sdsd.SearchItem("우유팩"));
        searchList.add(new com.example.sdsd.SearchItem("유리병"));
        searchList.add(new com.example.sdsd.SearchItem("형광등"));
    }

    private void setUpRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new com.example.sdsd.SearchAdapter(searchList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(SearchActivity.this,HowToRecycle.class);
                intent.putExtra("name", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
}
