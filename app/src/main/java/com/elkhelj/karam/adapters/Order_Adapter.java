package com.elkhelj.karam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.elkhelj.karam.activities_fragments.activity_my_orders.MyOrdersActivity;
import com.elkhelj.karam.models.Order_Model;
import com.elkhelj.karam.R;
import com.elkhelj.karam.databinding.OrderRowBinding;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Order_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Order_Model> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = 0;
    private MyOrdersActivity myOrdersActivity;
    public Order_Adapter(List<Order_Model> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        myOrdersActivity=(MyOrdersActivity)context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        OrderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.order_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final EventHolder eventHolder = (EventHolder) holder;
    eventHolder.binding.setModel(orderlist.get(position));
eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        myOrdersActivity.Display(orderlist.get(eventHolder.getLayoutPosition()));
    }
});
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public OrderRowBinding binding;

        public EventHolder(@NonNull OrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
