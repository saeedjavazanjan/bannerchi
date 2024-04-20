package com.burhanrashid52.photoediting.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.burhanrashid52.photoediting.R;
import com.burhanrashid52.photoediting.fragmens.AboutUs;
import com.burhanrashid52.photoediting.fragmens.DownLoadsFragment;
import com.burhanrashid52.photoediting.fragmens.HomeFragment;
import com.burhanrashid52.photoediting.fragmens.ProfileFragment;
import com.burhanrashid52.photoediting.fragmens.SavedItemsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
//TODO manage rtl phones
//todo bottom navigation hiding
public class MainActivity extends AppCompatActivity {
    public BottomNavigationView bottomNavigationView;
    private BadgeDrawable badgeDrawable;
    FrameLayout frameLayout;
    boolean exit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = new Bundle();

        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("TAG", msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

                    }
                });

                        bottomNavigationView = findViewById(R.id.bottom_navigation);
                        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
                         bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);

                        frameLayout = findViewById(R.id.frameLayoutMain);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutMain, new HomeFragment()).commit();

      /*  badgeDrawable=bottomNavigationView.getOrCreateBadge(R.id.downloads);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(5);
        badgeDrawable.setMaxCharacterCount(2);
        badgeDrawable.setBackgroundColor(ContextCompat.getColor(this,R.color.green_color_picker));
        badgeDrawable.setBadgeTextColor(Color.WHITE);
*/
                    }


                    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            // By using switch we can easily get
                            // the selected fragment
                            // by using there id.
                            Fragment selectedFragment = null;
                            switch (item.getItemId()) {
                                case R.id.home:
                                    selectedFragment = new HomeFragment();
                                    break;
                                case R.id.downloads:
                                    selectedFragment = new DownLoadsFragment();

                                    break;
                                case R.id.favorits:
                                    selectedFragment = new SavedItemsFragment();

                                    break;
                                case R.id.account:
                                    selectedFragment = new AboutUs();
                                    break;
                                case R.id.profile:
                                    selectedFragment = new ProfileFragment();
                                    break;
                            }
                            // It will help to replace the
                            // one fragment to other.
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.frameLayoutMain, selectedFragment)
                                    .commit();
                            return true;
                        }
                    };


    @Override
    public void onBackPressed() {
        if(bottomNavigationView.getSelectedItemId()==R.id.home){
            if (exit) {
                System.exit(0);
            }else{
                Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_SHORT).show();
                exit=true;
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit=false;
                    }
                },2000);
            }
        }else{
            bottomNavigationView.setSelectedItemId(R.id.home);
        }

    }
}