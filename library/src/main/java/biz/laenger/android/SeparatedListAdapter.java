package biz.laenger.android;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class SeparatedListAdapter extends BaseAdapter {

    private final static int TYPE_SECTION_HEADER = 0;

    private final ArrayAdapter<String> headerAdapter;
    private final List<Pair<String, Adapter>> sections;
    private final int itemDividerResId;

    private final ItemFinder<Object> itemFinder = new ItemFinder<Object>() {
        @Override
        Object handleFoundSectionHeader(String sectionTitle, int sectionIndex) {
            return headerAdapter.getItem(sectionIndex);
        }

        @Override
        Object handleFoundAdapterItem(int sectionIndex, Adapter adapter, int adapterPosition) {
            return adapter.getItem(adapterPosition);
        }

        @Override
        Object handleNotFound() {
            return null;
        }
    };

    private final ItemFinder<Integer> itemViewTypeFinder = new ItemFinder<Integer>() {
        @Override
        Integer handleFoundSectionHeader(String sectionTitle, int sectionIndex) {
            return TYPE_SECTION_HEADER;
        }

        @Override
        Integer handleFoundAdapterItem(int sectionIndex, Adapter adapter, int adapterPosition) {
            int itemViewType = 1;
            for (int i = 0; i < sectionIndex; i++) {
                itemViewType += sections.get(i).second.getViewTypeCount();
            }
            itemViewType += sections.get(sectionIndex).second.getItemViewType(adapterPosition);
            return itemViewType;
        }

        @Override
        Integer handleNotFound() {
            return IGNORE_ITEM_VIEW_TYPE;
        }
    };

    public SeparatedListAdapter(Context context, int headerResId, int headerTextViewResId, int itemDividerResId) {
        this.headerAdapter = new ArrayAdapter<>(context, headerResId, headerTextViewResId);
        this.sections = new ArrayList<>();
        this.itemDividerResId = itemDividerResId;
    }

    public void addSection(String sectionTitle, Adapter adapter) {
        headerAdapter.add(sectionTitle);
        sections.add(new Pair<>(sectionTitle, adapter));
    }

    @Override
    public int getCount() {
        int count = 0;
        for (Pair<String, Adapter> pair : sections) {
            count += pair.second.getCount() + (TextUtils.isEmpty(pair.first) ? 0 : 1);
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return itemFinder.find(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {
        return new ItemFinder<View>() {
            @Override
            View handleFoundSectionHeader(String sectionTitle, int sectionIndex) {
                final View view = headerAdapter.getView(sectionIndex, convertView, parent);
                view.setVisibility(TextUtils.isEmpty(sectionTitle) ? View.GONE : View.VISIBLE);
                return view;
            }

            @Override
            View handleFoundAdapterItem(int sectionIndex, Adapter adapter, int adapterPosition) {
                final View view = adapter.getView(adapterPosition, convertView, parent);
                if (itemDividerResId != 0) {
                    final View divider = view.findViewById(itemDividerResId);
                    if (divider != null) {
                        final boolean nextSectionHasHeader = sectionIndex + 1 < sections.size() && TextUtils.isEmpty(headerAdapter.getItem(sectionIndex + 1));
                        final boolean isLastItemInAdapter = adapterPosition + 1 == adapter.getCount();
                        divider.setVisibility(isLastItemInAdapter && !nextSectionHasHeader ? View.INVISIBLE : View.VISIBLE);
                    }
                }
                return view;
            }

            @Override
            View handleNotFound() {
                return null;
            }
        }.find(position);
    }

    @Override
    public int getItemViewType(int position) {
        return itemViewTypeFinder.find(position);
    }

    @Override
    public int getViewTypeCount() {
        int viewTypeCount = headerAdapter.getViewTypeCount();
        for (Pair<String, Adapter> pair : sections) {
            viewTypeCount += pair.second.getViewTypeCount();
        }
        return viewTypeCount;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) > TYPE_SECTION_HEADER;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);

        for (Pair<String, Adapter> pair : sections) {
            pair.second.registerDataSetObserver(observer);
        }
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);

        for (Pair<String, Adapter> pair : sections) {
            pair.second.unregisterDataSetObserver(observer);
        }
    }

    public void resolveListItemClick(int position, final ListItemClickResolveCallback callback) {
        new ItemFinder<Void>() {
            @Override
            Void handleFoundSectionHeader(String sectionTitle, int sectionIndex) {
                callback.onInvalidItemClick();
                return null;
            }

            @Override
            Void handleFoundAdapterItem(int sectionIndex, Adapter adapter, int adapterPosition) {
                callback.onAdapterItemClick(sectionIndex, adapter, adapterPosition);
                return null;
            }

            @Override
            Void handleNotFound() {
                callback.onInvalidItemClick();
                return null;
            }
        }.find(position);
    }

    /**
     * Helper class that solves the general problem of resolving a global list position to the corresponding sub adapter and the relative
     * position to that adapter.
     *
     * @param <T> an arbitrary type required for further processing
     */
    private abstract class ItemFinder<T> {

        T find(int position) {
            int sectionIndex = 0;
            for (Pair<String, Adapter> pair : sections) {
                if (TextUtils.isEmpty(pair.first)) {
                    position++;
                }

                if (position == 0) {
                    return handleFoundSectionHeader(pair.first, sectionIndex);
                }

                final Adapter adapter = pair.second;
                final int count = adapter.getCount();
                if (position <= count) {
                    return handleFoundAdapterItem(sectionIndex, adapter, position - 1);
                }

                sectionIndex++;
                position -= count + 1;
            }
            return handleNotFound();
        }

        abstract T handleFoundSectionHeader(String sectionTitle, int sectionIndex);

        abstract T handleFoundAdapterItem(int sectionIndex, Adapter adapter, int adapterPosition);

        abstract T handleNotFound();

    }

    public interface ListItemClickResolveCallback {
        void onAdapterItemClick(int sectionIndex, Adapter adapter, int adapterPosition);

        void onInvalidItemClick();
    }

}
