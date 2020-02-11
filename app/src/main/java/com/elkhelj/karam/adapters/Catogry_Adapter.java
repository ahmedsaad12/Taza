package com.elkhelj.karam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.elkhelj.karam.R;
import com.elkhelj.karam.databinding.CatogryRowBinding;
import com.elkhelj.karam.models.Catogries_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Catogry_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int LOAD = 2;
    private List<Catogries_Model> orderModelList;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    int index = -1;

    public Catogry_Adapter(List<Catogries_Model> orderModelList, Context context, Fragment fragment) {
        this.orderModelList = orderModelList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            CatogryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.catogry_row, parent, false);
            return new EventHolder(binding);



    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Catogries_Model order_orderModel = orderModelList.get(position);
        if (holder instanceof EventHolder)
        {
        EventHolder eventHolder = (EventHolder) holder;
        eventHolder.binding.setAdmodel(order_orderModel);

        }
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public CatogryRowBinding binding;

        public EventHolder(CatogryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


    @Override
    public int getItemViewType(int position) {
        Catogries_Model order_Model = orderModelList.get(position);
        if (order_Model!=null)
        {
            return ITEM_DATA;
        }else
        {
            return LOAD;
        }

    }


}
