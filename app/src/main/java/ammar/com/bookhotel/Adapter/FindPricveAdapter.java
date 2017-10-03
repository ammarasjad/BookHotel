package ammar.com.bookhotel.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ammar.com.bookhotel.Object.ProfilePriceObject;
import ammar.com.bookhotel.R;

public class FindPricveAdapter extends BaseAdapter
{

    private Activity activity;

    private LayoutInflater inflater;
    private List<ProfilePriceObject> List=new ArrayList<ProfilePriceObject>();

    public FindPricveAdapter(Activity activity, List<ProfilePriceObject> allist) {
        this.activity = activity;
        this.List = allist;
    }

    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public Object getItem(int location) {
        return List.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.hotel_list_single_item,null,true);

        TextView lavelname = (TextView)rowView.findViewById(R.id.lavelname);


        lavelname.setText(List.get(position).getPriceval());

        return rowView;
    }

}
