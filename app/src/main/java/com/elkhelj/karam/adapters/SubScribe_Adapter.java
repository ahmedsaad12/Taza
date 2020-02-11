package com.elkhelj.karam.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.elkhelj.karam.activities_fragments.activity_home.HomeActivity;
import com.elkhelj.karam.activities_fragments.activity_home.fragments.Fragment_Subscrabtions;
import com.elkhelj.karam.activities_fragments.activity_subscribe.SubscribeActivity;
import com.elkhelj.karam.models.Product_Model;
import com.elkhelj.karam.R;
import com.elkhelj.karam.databinding.SubscribeRowBinding;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class SubScribe_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product_Model> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = 0;
    private Fragment_Subscrabtions fragment_subscrabtions;
    private SubscribeActivity subscribeActivity;
    public SubScribe_Adapter(List<Product_Model> orderlist, Context context, Fragment_Subscrabtions fragment_subscrabtions) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if(context instanceof HomeActivity){
this.fragment_subscrabtions=fragment_subscrabtions;}
        else {
            subscribeActivity=(SubscribeActivity)context;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        SubscribeRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.subscribe_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final EventHolder eventHolder = (EventHolder) holder;
      eventHolder.binding.setModel(orderlist.get(position));
eventHolder.binding.setLang(lang);
eventHolder.binding.btnsubscride.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.e("kkkkk","lllll");
        if(context instanceof HomeActivity){

            fragment_subscrabtions.setsubscribe(eventHolder.getLayoutPosition());}
        else {
            subscribeActivity.setsubscribe(eventHolder.getLayoutPosition());
        }
    }
});
        if(context instanceof HomeActivity){

if(orderlist.get(position).getType()==1){
    eventHolder.binding.btnsubscride.setText(context.getResources().getString(R.string.subscribe));
}
else if(orderlist.get(position).getType()==2){
    eventHolder.binding.btnsubscride.setText(context.getResources().getString(R.string.unsubscribe));}
else {
    eventHolder.binding.btnsubscride.setText(context.getResources().getString(R.string.unsubscribe));
}

}
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public SubscribeRowBinding binding;

        public EventHolder(@NonNull SubscribeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
