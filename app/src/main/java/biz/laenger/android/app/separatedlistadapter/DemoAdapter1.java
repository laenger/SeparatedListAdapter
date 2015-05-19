package biz.laenger.android.app.separatedlistadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DemoAdapter1 extends ArrayAdapter<Integer> {

    private final List<Integer> objects;
    private final LayoutInflater inflater;

    public DemoAdapter1(Context context, List<Integer> objects) {
        super(context, 0, objects);
        this.objects = objects;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Integer currentItem = objects.get(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_adapter1, parent, false);

            final ViewHolder newViewHolder = new ViewHolder();
            newViewHolder.oneText = (TextView) convertView.findViewById(R.id.one);
            newViewHolder.twoText = (TextView) convertView.findViewById(R.id.two);

            convertView.setTag(newViewHolder);
            viewHolder = newViewHolder;
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (currentItem != null) {
            viewHolder.oneText.setText(currentItem.toString());
            viewHolder.twoText.setText(currentItem.toString());
        }

        return convertView;
    }

    private static class ViewHolder {
        protected TextView oneText;
        protected TextView twoText;
    }

}
