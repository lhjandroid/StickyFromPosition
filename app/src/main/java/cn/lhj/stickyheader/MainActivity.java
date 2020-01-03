package cn.lhj.stickyheader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private View mStickyView;
    private TestAdapter mTestAdapter;
    private StickLayoutManager mStickLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rcv);
        mStickyView = findViewById(R.id.tv_sticky);

        mStickLayoutManager = new StickLayoutManager(this);
        mStickLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mStickLayoutManager.setStickPosition(16);
        mStickLayoutManager.setStickView(mStickyView,6);
        mRecyclerView.setLayoutManager(mStickLayoutManager);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < 40; i ++) {
            data.add("item position;" + i);
        }
        mTestAdapter = new TestAdapter(this,data);
        mRecyclerView.setAdapter(mTestAdapter);

    }
}
