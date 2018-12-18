package com.services.testmeli.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.services.testmeli.Model.Attributes;
import com.services.testmeli.R;

import java.util.List;


public class AttributesItemAdapter extends BaseAdapter {

    private Context context;
    private List<Attributes> listAttributes;

    public AttributesItemAdapter(Context mContext, List<Attributes> attributes) {
        this.context = mContext;
        this.listAttributes = attributes;
    }

    @Override
    public int getCount() {
        return listAttributes.size();
    }

    @Override
    public Object getItem(int position) {
        return listAttributes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Attributes attributes = (Attributes)  listAttributes.get(position);

        view = LayoutInflater.from(context).inflate(R.layout.layout_attributes_item_format, null);

        TextView textViewNameAttribute = (TextView) view.findViewById(R.id.textViewAttributeName);
        TextView textViewValueNameAttribute = (TextView) view.findViewById(R.id.textViewAttributeValueName);

        textViewNameAttribute.setText(attributes.getName());
        textViewValueNameAttribute.setText(attributes.getValue_name());

        return view;
    }


    //Delete funcion scroll on ViewList
    public static void justifyListViewHeightBasedOnChildren (ListView listView) {
        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
}
