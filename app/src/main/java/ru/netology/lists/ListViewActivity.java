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

    /*private void init() {
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
    }*/

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


}
