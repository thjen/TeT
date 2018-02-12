package io.qthjen_dev.tet.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import io.qthjen_dev.tet.Adapter.AdapterMenu;
import io.qthjen_dev.tet.R;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbarMenu;
    private RecyclerView mRecyclerMenu;
    private ViewGroup mViewGroupMenu;
    private NavigationView mNavigation;
    private TextView mTvAbout;
    private AdView mAdView;

    private AdapterMenu mAdapter;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FindView();
        SetupDrawerLayoutAndToolbar();

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        mRecyclerMenu.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new AdapterMenu(this, mRecyclerMenu);
        mRecyclerMenu.setAdapter(mAdapter);

    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void FindView() {

        mDrawerLayout = findViewById(R.id.drawerll);
        mToolbarMenu = findViewById(R.id.tbar);
        mRecyclerMenu = findViewById(R.id.recyclerViewMenu);
        mViewGroupMenu = findViewById(R.id.viewGroupMenu);
        mTvAbout = findViewById(R.id.about);
        mNavigation = findViewById(R.id.navigationMain);
        mAdView = findViewById(R.id.adView1);

    }

    private void SetupDrawerLayoutAndToolbar() {

        setSupportActionBar(mToolbarMenu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.home));
        mToolbarMenu.getContentInsetEnd();
        mToolbarMenu.setNavigationIcon(R.drawable.mymenu);
        mToolbarMenu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mTvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Thông tin ứng dụng");
                dialog.setMessage("Ứng dụng: ChucTeT" + "\n" +
                        "Phiên bản: 1.0 Beta 1" + "\n" +
                        "Dữ liệu(lời chúc) được lấy từ: http://tin.tuyensinh247.com/nhung-loi-chuc-tet-hay-va-y-nghia-nhat-e48.html"
                        + "\n" + "Nhà phát triển: qThjen" + "\n" + "Mọi phản hồi xin gửi tới gmail: thjenxxxno6@gmail.com"
                        + "\n" + "CẢM ƠN ĐÃ SỬ DỤNG SẢN PHẨM NÀY!");

                dialog.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                dialog.show();

                mDrawerLayout.closeDrawer(GravityCompat.START);

            }
        });

    }
/*
    private void SetupBlurView(BlurView blurView, float radius) {

        Drawable windowBackgroud = getWindow().getDecorView().getBackground();

        blurView.setupWith(mViewGroupMenu)
                .windowBackground(windowBackgroud)
                .blurAlgorithm(new SupportRenderScriptBlur(this))
                .blurRadius(radius);

    }
*/
}
