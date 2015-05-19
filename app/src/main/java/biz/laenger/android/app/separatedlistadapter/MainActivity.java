package biz.laenger.android.app.separatedlistadapter;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;

import biz.laenger.android.SeparatedListAdapter;

public class MainActivity extends ListActivity implements SeparatedListAdapter.ListItemClickResolveCallback {

    private SeparatedListAdapter separatedListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        separatedListAdapter = new SeparatedListAdapter(this, R.layout.item_header, R.id.item_header_text, R.id.item_divider);

        separatedListAdapter.addSection("section 1", new DemoAdapter1(this, Arrays.asList(42, 4711, 7)));
        separatedListAdapter.addSection("section 2", new DemoAdapter2(this, Arrays.asList("foo", "bar")));
        separatedListAdapter.addSection(null,        new DemoAdapter3(this, Arrays.asList("lorem", "ipsum", "dolor")));
        separatedListAdapter.addSection("section 4", new DemoAdapter1(this, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)));

        setListAdapter(separatedListAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        separatedListAdapter.resolveListItemClick(position, this);
    }

    @Override
    public void onAdapterItemClick(int sectionIndex, Adapter adapter, int adapterPosition) {
        Toast.makeText(this, "value: " + adapter.getItem(adapterPosition), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInvalidItemClick() {
        Toast.makeText(this, "invalid item", Toast.LENGTH_SHORT).show();
    }

}
