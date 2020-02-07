package com.elkhelj.taza.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.elkhelj.taza.R;
import com.elkhelj.taza.activities_fragments.activity_home.fragments.Fragment_Subscrabtions;
import com.elkhelj.taza.databinding.ProductsRowBinding;
import com.elkhelj.taza.databinding.SubscribeRowBinding;
import com.elkhelj.taza.models.Product_Model;

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
    public SubScribe_Adapter(List<Product_Model> orderlist, Context context, Fragment_Subscrabtions fragment_subscrabtions) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
this.fragment_subscrabtions=fragment_subscrabtions;
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
        fragment_subscrabtions.setsubscribe(eventHolder.getLayoutPosition());
    }
});
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
