package com.elkhelj.karam.activities_fragments.activity_my_orders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.elkhelj.karam.models.Order_Model;
import com.elkhelj.karam.R;
import com.elkhelj.karam.activities_fragments.activity_my_order_detials.OrderDetialsActivity;
import com.elkhelj.karam.activities_fragments.activity_my_orders.fragments.Fragment_Cancel_Order;
import com.elkhelj.karam.activities_fragments.activity_my_orders.fragments.Fragment_Current_Order;
import com.elkhelj.karam.activities_fragments.activity_my_orders.fragments.Fragment_Finshied_Order;
import com.elkhelj.karam.activities_fragments.activity_my_orders.fragments.Fragment_Onging_Order;
import com.elkhelj.karam.adapters.ViewPagerAdapter;
import com.elkhelj.karam.databinding.ActivityMyOrdersBinding;
import com.elkhelj.karam.interfaces.Listeners;
import com.elkhelj.karam.language.LanguageHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

//import com.elkhelj.taza.adapters.My_Orders_Adapter;

public class MyOrdersActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityMyOrdersBinding binding;
    private String lang;
    private List<Fragment> fragmentList;
    private List<String> titles;
    private ViewPagerAdapter adapter;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase,LanguageHelper.getLanguage(newBase)));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_orders);
        initView();


    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        fragmentList = new ArrayList<>();
        titles = new ArrayList<>();
        binding.setBackListener(this);
        binding.tab.setupWithViewPager(binding.pager);
        addFragments_Titles();
        binding.pager.setOffscreenPageLimit(fragmentList.size());

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragments(fragmentList);
        adapter.addTitles(titles);
        binding.pager.setAdapter(adapter);







    }

    private void addFragments_Titles() {
        fragmentList.add(Fragment_Current_Order.newInstance());
        fragmentList.add(Fragment_Onging_Order.newInstance());
        fragmentList.add(Fragment_Cancel_Order.newInstance());

        fragmentList.add(Fragment_Finshied_Order.newInstance());


        titles.add(getString(R.string.current_order));
        titles.add(getString(R.string.Onging));
        titles.add(getString(R.string.canceled));

        titles.add(getString(R.string.finish_order));



    }


    @Override
    public void back() {
        finish();
    }

    public void Display(Order_Model order_model) {
        Intent intent=new Intent(MyOrdersActivity.this, OrderDetialsActivity.class);
        intent.putExtra("productid",order_model);
        startActivity(intent);
    }
}
