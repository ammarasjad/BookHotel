package ammar.com.bookhotel.Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ammar.com.bookhotel.Cart;
import ammar.com.bookhotel.Object.ProfilePriceObject;
import ammar.com.bookhotel.Object.cart_object;
import ammar.com.bookhotel.R;
import ammar.com.bookhotel.Utils;

public class cart_list_adapter extends BaseAdapter {

    private Activity activity1;
    private Cart inflater;
    private List<cart_object> cartlist= new ArrayList<>();

    public cart_list_adapter(Activity activity, List<cart_object> list) {
        this.activity1=activity;
        cartlist = list;
    }

    @Override
    public int getCount() {
        return cartlist.size();
    }

    @Override
    public Object getItem(int i) {
        return cartlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity1.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.cart_single_item, null, true);


        ImageView cancel = (ImageView) rowView.findViewById(R.id.cancel);
        ImageView hotel_scenery = (ImageView) rowView.findViewById(R.id.hotel_image);
        TextView rooms = (TextView) rowView.findViewById(R.id.room_type);
        TextView rooms_quantity = (TextView) rowView.findViewById(R.id.room_quantity);
        TextView adult = (TextView) rowView.findViewById(R.id.adult);
        TextView child = (TextView) rowView.findViewById(R.id.child);
        TextView descount = (TextView) rowView.findViewById(R.id.descount);
        TextView to_pay = (TextView) rowView.findViewById(R.id.to_pay);
        to_pay.setText("€"+cartlist.get(i).getPayment_value());
        descount.setText("-€"+cartlist.get(i).getDescount_percent());
        rooms.setText("Room: "+cartlist.get(i).getRoom());
        rooms_quantity.setText("Quantity: "+cartlist.get(i).getRoom_quantity());
        adult.setText("Adult: "+cartlist.get(i).getAdult());
        child.setText("Kids: "+cartlist.get(i).getKids());
        cancel.setTag(i);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.crt_obj.remove(i);
                Utils.Total_hotels_book_deleted++;
                notifyDataSetChanged();
            }
        });


        int position = cartlist.get(i).getHotel_no();
        if (position==0){
            Glide.with(activity1).load(R.mipmap.mercury).into(hotel_scenery);
        }
        else if (position==1){
            Glide.with(activity1).load(R.mipmap.insel_hotel_heilbronn).into(hotel_scenery);
        }
        else if (position==2){
            Glide.with(activity1).load(R.mipmap.hotel_newton_heilbronn).into(hotel_scenery);
        }
        else if (position==3){
            Glide.with(activity1).load(R.mipmap.city_hotel_gerny).into(hotel_scenery);
        }
        else if (position==4){
            Glide.with(activity1).load(R.mipmap.hogh_hotel_heilbronn).into(hotel_scenery);
        }
        else if (position==5){
            Glide.with(activity1).load(R.mipmap.hotel_gasthof_zum_rossle).into(hotel_scenery);
        }
        return rowView;
    }


}
