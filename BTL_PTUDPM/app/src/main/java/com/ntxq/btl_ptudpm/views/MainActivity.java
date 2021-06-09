package com.ntxq.btl_ptudpm.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ntxq.btl_ptudpm.R;
import com.ntxq.btl_ptudpm.views.zodiac.ZodiacFragment;

public class MainActivity extends AppCompatActivity {

    public static final int MAIN_FRAGMENT= 1;
    public static final int TAROT_FRAGMENT= 2;
    public static final int ZODIAC_FRAGMENT= 3;
    public static final int NUMBER_FRAGMENT= 4;

    private TarotFragment tarotFragment = new TarotFragment();
    private ZodiacFragment zodiacFragment = new ZodiacFragment();
    private MainFragment mainFragment= new MainFragment();
    private NumberFragment numberFragment= new NumberFragment();
    private int currentFragment = MAIN_FRAGMENT;
    private String headerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transparentStatusBar();
        initView();
        initFragment();
        showFragment(currentFragment);
    }

    private void initView() {
        headerEmail= getIntent().getStringExtra("headerEmail");
    }

    public void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame, mainFragment);
        transaction.add(R.id.frame, tarotFragment);
        transaction.add(R.id.frame, zodiacFragment);
        transaction.add(R.id.frame, numberFragment);
        transaction.commit();
    }

    public void showFragment(int fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.hide(mainFragment);
        transaction.hide(tarotFragment);
        transaction.hide(zodiacFragment);
        transaction.hide(numberFragment);

        switch (fragment){
            case MAIN_FRAGMENT:
                transaction.show(mainFragment);
                currentFragment= MAIN_FRAGMENT;
                break;
            case TAROT_FRAGMENT:
                transaction.show(tarotFragment);
                currentFragment= TAROT_FRAGMENT;
                break;
            case ZODIAC_FRAGMENT:
                transaction.show(zodiacFragment);
                currentFragment= ZODIAC_FRAGMENT;
                break;
            case NUMBER_FRAGMENT:
                transaction.show(numberFragment);
                currentFragment= NUMBER_FRAGMENT;
                break;
        }
        transaction.commit();
    }

    private void transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    public void onBackPressed() {
        switch (currentFragment){
            case MAIN_FRAGMENT:
                super.onBackPressed();
                break;
            case TAROT_FRAGMENT:
            case ZODIAC_FRAGMENT:
            case NUMBER_FRAGMENT:
                showFragment(MAIN_FRAGMENT);
                break;
        }
    }

    public String getHeaderEmail(){
        return headerEmail;
    }
}