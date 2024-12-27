package com.example.adsadjustment.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.adsadjustment.Auth.LoginActivity;
import com.example.adsadjustment.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final float END_SCALE = 0.7f;
    private static final int REQUEST_STORAGE = 212;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView Menu_Icon;
    CoordinatorLayout content;
    private String pathCamera = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (ActivityCompat.checkSelfPermission(DashboardActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            initView();
        } else {
            ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        }
    }

    private void initView() {
        hooks();

        ClickListeners();

        navigationDrawer();
    }

    private void ClickListeners() {

        Menu_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        animateDrawer();
    }

    private void animateDrawer() {
        drawerLayout.setScrimColor(getResources().getColor(R.color.nav_animation));
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;

                content.setScaleX(offsetScale);
                content.setScaleY(offsetScale);

                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = content.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;

                content.setTranslationX(xTranslation);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void hooks() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        Menu_Icon = findViewById(R.id.menu_icon);
        content = findViewById(R.id.content);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_GetHeightOfObject:
                startActivity(new Intent(this, ObjectHeightActivity.class));
                break;
            case R.id.nav_GetDistanceOfObject:
                startActivity(new Intent(this, GetDistanceOfObjectActivity.class));
                break;
            case R.id.nav_GetTextFromImage:
                startActivity(new Intent(this, TextFormImageActivity.class));
                break;
            case R.id.nav_TranslateTextIntoDifferentLanguage:
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.nav_home:
            default:
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
                break;
        }

        return false;
    }

    public void GetHeightOfObject(View view) {
        startActivity(new Intent(this, ObjectHeightActivity.class));
    }

    public void GetDistanceOfObject(View view) {
        startActivity(new Intent(this, GetDistanceOfObjectActivity.class));
    }

    public void GetTextFromImage(View view) {
        startActivity(new Intent(this, TextFormImageActivity.class));
    }

    public void TranslateTextIntoDifferentLanguage(View view) {
        startActivity(new Intent(this, TranslationActivity.class));
    }

    public void DetectObjects(View view) {
        startActivity(new Intent(this, DetectionActivity.class));
    }
}