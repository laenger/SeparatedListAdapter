package biz.laenger.android.app.separatedlistadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DemoAdapter3 extends ArrayAdapter<String> {

    private final List<String> objects;
    private final LayoutInflater inflater;

    public DemoAdapter3(Context context, List<String> objects) {
        super(context, 0, objects);
        this.objects = objects;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String currentItem = objects.get(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_adapter3, parent, false);

            final ViewHolder newViewHolder = new ViewHolder();
            newViewHolder.oneText = (TextView) convertView.findViewById(R.id.one);

            convertView.setTag(newViewHolder);
            viewHolder = newViewHolder;
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (currentItem != null) {
            viewHolder.oneText.setText(currentItem);
        }

        return convertView;
    }

    private static class ViewHolder {
        protected TextView oneText;
    }

}
