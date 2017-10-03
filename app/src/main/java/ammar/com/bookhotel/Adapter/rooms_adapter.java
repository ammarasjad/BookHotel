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
import ammar.com.bookhotel.Object.rooms_object;
import ammar.com.bookhotel.R;


public class rooms_adapter extends BaseAdapter {

    private Activity activity;

    private LayoutInflater inflater;
    private List<rooms_object> List=new ArrayList<>();
    public rooms_adapter(Activity activity, List<rooms_object> allist) {
        this.activity = activity;
        this.List = allist;
    }

    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public Object getItem(int i) {
        return List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.rooms_single_item,null,true);

        TextView room = (TextView)rowView.findViewById(R.id.room_text);
        TextView price = (TextView) rowView.findViewById(R.id.price);

        room.setText(List.get(i).getRoom_name());
        price.setText("€"+List.get(i).getPrice());

        return rowView;
    }
}
