package com.example.to_do;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.transition.Slide;
import android.support.transition.TransitionInflater;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Tabbed extends AppCompatActivity
{
    String catname;
    TabLayout tabLayout;
    ViewPager viewPager;
    DatabaseHelper mydb;
    TabItem tabtask, tabdelayed, tabdone, tabfavourite;
    ImageView btnsearch,btnadd;
    EditText input;
    TextView btnaddpop_up, btncanpop_up;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        // setupWindowAnimations();

        tabLayout = findViewById(R.id.tabs);
        tabtask = findViewById(R.id.tabItemtask);
        tabdelayed = findViewById(R.id.tabItem2delayed);
        tabdone = findViewById(R.id.tabItem3done);
        tabfavourite = findViewById(R.id.tabItem4fav);
        viewPager = findViewById(R.id.container);
        btnsearch=(ImageView)findViewById(R.id.iconsearch);
        btnadd=(ImageView)findViewById(R.id.addicon);


//        btnaddpop_up=(TextView)findViewById(R.id.btnaddpop_up);
//        btncanpop_up=(TextView)findViewById(R.id.btncancelpop_up);

        mydb = new DatabaseHelper(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        input=(EditText)findViewById(R.id.txtgroup_name);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Tabbed.this);
                //alertDialog.setTitle("Task Group");
                //alertDialog.setMessage("Enter Group name");
                //alertDialog.setIcon(R.drawable.icon);
                alertDialog.setView(R.layout.add_group_layout);
                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    { }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    { }

                    @Override
                    public void afterTextChanged(Editable s) {
                        catname = input.getText().toString();
                        if(catname.length()> 30)
                        {
//                                          Toast.makeText(Tabbed.this, "Please enter only 30 characters!!!", Toast.LENGTH_SHORT).show();
                            input.setError("Please enter only 30 characters!!!");
                            input.setFocusable(true);
                        }
                        else if(input.getText().toString().length() == 0)
                        {
//                                          Toast.makeText(Tabbed.this, "Please enter Group name, dont leave it empty!!!", Toast.LENGTH_SHORT).show();
                            input.setError("Please enter Group name!!!");
                            input.setFocusable(true);
                        }
                        else {
                            boolean isInserted = mydb.insertData(catname);
                            if (isInserted == true) {
                                Toast.makeText(Tabbed.this, "Task Group Successfully added.", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Tabbed.this,Tabbed.class);
                                finish();
                                startActivity(intent);
//                              dialog.cancel();
                            } else {
                                Toast.makeText(Tabbed.this, "Category is not added.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });



                alertDialog.setPositiveButton("Add",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(final DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                            }
                        });

                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }

        });

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Tabbed.this,search.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tabbed, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public static class PlaceholderFragment extends Fragment
    {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment()
        { }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            View rootView=null;
            return rootView;
        }
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    frag_group_category tab1 = new frag_group_category();
                    return tab1;
                case 1:
                    frag_delayed  tab2 = new frag_delayed();
                    return tab2;
                case 2:
                    frag_done tab3 = new frag_done();
                    return tab3;
                case 3:
                    frag_fav tab4 = new frag_fav();
                    return tab4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount()
        {
            return 4;
        }
    }

    /*private void setupWindowAnimations() {
        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.activity_slide);
        getWindow().setExitTransition(slide);
    }*/
}
