package com.elkhelj.karam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.elkhelj.karam.activities_fragments.activity_add_unkowon.AddUnkownActivity;
import com.elkhelj.karam.activities_fragments.activity_home.HomeActivity;
import com.elkhelj.karam.activities_fragments.activity_showall_products.AllProductActivity;
import com.elkhelj.karam.models.Product_Model;
import com.elkhelj.karam.R;
import com.elkhelj.karam.databinding.ProductsRowBinding;

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
    private AllProductActivity allProductActivity;
    private AddUnkownActivity addUnkownActivity;
    public Products_Adapter(List<Product_Model> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
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
        if(context instanceof  HomeActivity){
            activity=(HomeActivity)context;
            activity.showdetials(orderlist.get(eventHolder.getLayoutPosition()).getId());

        }
        else if(context instanceof  AllProductActivity ){
            allProductActivity=(AllProductActivity)context;
            allProductActivity.showdetials(orderlist.get(eventHolder.getLayoutPosition()).getId());
        }
        else if(context instanceof  AddUnkownActivity ){
            addUnkownActivity=(AddUnkownActivity) context;
            addUnkownActivity.showdetials(orderlist.get(eventHolder.getLayoutPosition()).getId());
        }
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
