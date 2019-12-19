package ru.netology.lists;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListViewActivity extends AppCompatActivity {
    private SharedPreferences textList;
    private List<Map<String, String>> content = new ArrayList();
    private BaseAdapter listContentAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Integer> deleteList = new ArrayList<>();

    private final String TEXT = "SAVED_TEXT";
    private final String EMPTY = ":(";
    private final int DELAY = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        saveText();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        ListView list = findViewById(R.id.list);


        list.setOnItemClickListener(listViewOnItemClickListener);

        prepareContent();

        listContentAdapter = createAdapter(content);
        list.setAdapter(listContentAdapter);
    }


    @NonNull
    private BaseAdapter createAdapter(List<Map<String, String>> values) {
        return new SimpleAdapter(this, values,
                R.layout.list, new String[]{"text", "count"}, new int[]{R.id.first, R.id.second});

    }

    @NonNull
    private void prepareContent() {
        String[] arrayContent = textList.getString(TEXT, EMPTY).split("\n\n");

        for (int i = 0; i < arrayContent.length; i++) {
            Map<String, String> map = new HashMap();
            map.put("text", arrayContent[i]);
            map.put("count", Integer.toString(arrayContent[i].length()));
            content.add(map);
        }
    }

    private void saveText() {
        textList = getPreferences(MODE_PRIVATE);
        if (!textList.equals(getString(R.string.large_text))) {
            SharedPreferences.Editor editor = textList.edit();
            editor.putString(TEXT, getString(R.string.large_text));
            editor.commit();
        }
    }

    AdapterView.OnItemClickListener listViewOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            deleteList.add(i);
            content.remove(i);
            listContentAdapter.notifyDataSetChanged();
        }
    };

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, DELAY);
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putIntegerArrayList("list", deleteList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        deleteList = savedInstanceState.getIntegerArrayList("list");
        deleteStrings(deleteList);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void deleteStrings(ArrayList<Integer> deleteList){
        for (int i = 0; i < deleteList.size(); i++){
            int j = deleteList.get(i);
            content.remove(j);
            listContentAdapter.notifyDataSetChanged();
        }
    }
}
