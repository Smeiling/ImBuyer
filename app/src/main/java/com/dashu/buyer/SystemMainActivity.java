package com.dashu.buyer;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dashu.buyer.frg.RecordHouseFrg;
import com.dashu.buyer.frg.RecordRantFragment;
import com.dashu.buyer.frg.RecordTableFragment;
import com.dashu.buyer.util.StaticUtil;

public class SystemMainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    TextView userTV;
    Toolbar mToolbar;

    int role;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.id_nv_menu);

        View view = mNavigationView.getChildAt(0);
        String text = getIntent().getStringExtra("username");
        role = getIntent().getIntExtra("roleType", StaticUtil.ROLE_USER);
        Log.e("tag","username=="+text);

        userTV = (TextView) view.findViewById(R.id.id_username);
//        userTV.setText(text);
//        userTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SystemMainActivity.this,UpdateUserActivity.class);
//                startActivity(intent);
//            }
//        });

        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        setSupportActionBar(mToolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("代你美");

        setupDrawerContent(mNavigationView);

        switchRecordHouse();

    }

    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(

                new NavigationView.OnNavigationItemSelectedListener()
                {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)
                    {
                        menuItem.setChecked(true);
                        Log.i("TAG", "onNavigationItemSelected: "+menuItem.getTitle());
                        mToolbar.setTitle(menuItem.getTitle());
                        mDrawerLayout.closeDrawers();
                        switch (menuItem.getItemId()){
                            case R.id.build_customer:
                                switchRecordHouse();
                                break;
                            case R.id.build_seller:
                                switchRecordRant();
                                break;
                            case R.id.person_complain:
                                switchRecordTable();
                                break;

                        }

                        invalidateOptionsMenu();
                        return true;
                    }
                });
    }

    private void switchRecordHouse(){
        getSupportFragmentManager().beginTransaction().replace(R.id.content,new RecordHouseFrg()).commit();

    }

    private void switchRecordRant(){
        getSupportFragmentManager().beginTransaction().replace(R.id.content,new RecordRantFragment()).commit();
    }

    private void switchRecordTable(){
        getSupportFragmentManager().beginTransaction().replace(R.id.content,new RecordTableFragment()).commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }

}