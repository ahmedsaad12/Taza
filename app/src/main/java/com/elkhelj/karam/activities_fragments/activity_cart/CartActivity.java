package com.elkhelj.karam.activities_fragments.activity_cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;


import com.elkhelj.karam.activities_fragments.activity_add_ads.CompleteOrderActivity;
import com.elkhelj.karam.R;
import com.elkhelj.karam.adapters.Cart_Adapter;
import com.elkhelj.karam.databinding.ActivityCartBinding;
import com.elkhelj.karam.interfaces.Listeners;
import com.elkhelj.karam.language.LanguageHelper;
import com.elkhelj.karam.models.Add_Order_Model;
import com.elkhelj.karam.models.Orders_Cart_Model;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCartBinding binding;

    private Preferences preferences;
    private UserModel userModel;
    private String lang;
private List<Orders_Cart_Model> order_details;
private Cart_Adapter cart_adapter;
private double totalcost;
    private Add_Order_Model add_order_model;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase,LanguageHelper.getLanguage(newBase)));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        initView();
getorders();

    }

    private void getorders() {
        if(preferences.getUserOrder(this)!=null){
            order_details.clear();
            order_details.addAll(preferences.getUserOrder(this));
            cart_adapter.notifyDataSetChanged();
        gettotal();
        }
        else {
            binding.llNoStore.setVisibility(View.VISIBLE);
            binding.tvTotal.setVisibility(View.GONE);
            binding.btCom.setVisibility(View.GONE);

        }
    }

    private void gettotal() {

        double total=0;
        for(int i=0;i<order_details.size();i++){
            total+=Double.parseDouble(order_details.get(i).getPrice());

        }
        totalcost=total;


        binding.tvTotal.setText(getResources().getString(R.string.total)+totalcost+"");
    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.toolbar.setTitle("");
order_details=new ArrayList<>();
cart_adapter=new Cart_Adapter(order_details,this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.recCart.setLayoutManager(new GridLayoutManager(this,1));
        binding.recCart.setAdapter(cart_adapter);
binding.btCom.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(userModel!=null){
          //  checkdata();
            Intent intent=new Intent(CartActivity.this, CompleteOrderActivity.class);
            startActivity(intent);
        }
        else {
         //   Common.CreateNoSignAlertDialog(CartActivity.this);
        }
    }
});
    }




    public void removeitem(int layoutPosition) {
        order_details.remove(layoutPosition);
        if(order_details.size()>0){

            preferences.create_update_order(this,order_details);
            gettotal();
        }
        else {
            preferences.create_update_order(this,null);
            binding.llNoStore.setVisibility(View.VISIBLE);
            binding.tvTotal.setVisibility(View.GONE);
            binding.btCom.setVisibility(View.GONE);

        }

        cart_adapter.notifyDataSetChanged();

    }
    public void additem(int layoutPosition) {
       Orders_Cart_Model products1 =order_details.get(layoutPosition);
products1.setPrice((Double.parseDouble(products1.getPrice())/ products1.getAmount())*(products1.getAmount()+1)+"");
        products1.setAmount(products1.getAmount()+1);
        order_details.remove(layoutPosition);
        order_details.add(layoutPosition, products1);

        preferences.create_update_order(this,order_details);
        cart_adapter.notifyDataSetChanged();
        gettotal();
    }
    public void minusitem(int layoutPosition) {

     Orders_Cart_Model products1 =order_details.get(layoutPosition);
        if(products1.getAmount()>1){
        products1.setPrice((Double.parseDouble(products1.getPrice())/ products1.getAmount())*(products1.getAmount()-1)+"");
        products1.setAmount(products1.getAmount()-1);
        order_details.remove(layoutPosition);
        order_details.add(layoutPosition, products1);
        preferences.create_update_order(this,order_details);
        cart_adapter.notifyDataSetChanged();
            gettotal();

        }
    }

    @Override
    public void back() {
        finish();
    }





    private void showorders() {

        preferences.create_update_order(CartActivity.this, null);
        order_details.clear();
        cart_adapter.notifyDataSetChanged();
        binding.llNoStore.setVisibility(View.VISIBLE);
        binding.tvTotal.setVisibility(View.GONE);
        binding.btCom.setVisibility(View.GONE);
    }



}
