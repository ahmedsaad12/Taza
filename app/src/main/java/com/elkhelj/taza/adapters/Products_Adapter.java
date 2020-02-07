package com.elkhelj.taza.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.elkhelj.taza.R;
import com.elkhelj.taza.activities_fragments.activity_home.HomeActivity;
import com.elkhelj.taza.databinding.ProductsRowBinding;
import com.elkhelj.taza.models.Product_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Products_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product_Model> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = 0;
    private HomeActivity activity;
    public Products_Adapter(List<Product_Model> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
this.activity=(HomeActivity)context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ProductsRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.products_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final EventHolder eventHolder = (EventHolder) holder;
      eventHolder.binding.setProductsmodel(orderlist.get(position));
eventHolder.binding.setLang(lang);
eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        activity.showdetials(orderlist.get(eventHolder.getLayoutPosition()).getId());
    }
});
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public ProductsRowBinding binding;

        public EventHolder(@NonNull ProductsRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
