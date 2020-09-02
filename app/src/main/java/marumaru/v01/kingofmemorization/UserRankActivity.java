package marumaru.v01.kingofmemorization;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import marumaru.v01.kingofmemorization.Fragment.PeriodRankFragment;
import marumaru.v01.kingofmemorization.Fragment.ShareRankFragment;
import marumaru.v01.kingofmemorization.Fragment.StarRankFragment;

public class UserRankActivity extends AppCompatActivity {

    FragmentTransaction fragmentTransaction;
    TabItem tab_star_rank, tab_share_rank, tab_period_rank;
    TabLayout tab_rank;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userrank);

        // 툴바 설정
        Toolbar toolbar = findViewById(R.id.toolbar_rank);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 탭 설정
        tab_rank = findViewById(R.id.tab_rank);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        StarRankFragment starRankFragment = new StarRankFragment();
        fragmentTransaction.replace(R.id.fl_rank, starRankFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        tab_rank.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                switch (position){
                    case 0 :
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        StarRankFragment starRankFragment = new StarRankFragment();
                        fragmentTransaction.replace(R.id.fl_rank, starRankFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case 1:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        ShareRankFragment shareRankFragment = new ShareRankFragment();
                        fragmentTransaction.replace(R.id.fl_rank, shareRankFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case 2:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        PeriodRankFragment periodRankFragment = new PeriodRankFragment();
                        fragmentTransaction.replace(R.id.fl_rank, periodRankFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /*
        tab_star_rank = findViewById(R.id.tab_star_rank);
        tab_share_rank = findViewById(R.id.tab_share_rank);
        tab_period_rank = findViewById(R.id.tab_period_rank);

        tab_star_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                StarRankFragment starRankFragment = new StarRankFragment();
                fragmentTransaction.replace(R.id.fl_rank, starRankFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        tab_star_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                ShareRankFragment shareRankFragment = new ShareRankFragment();
                fragmentTransaction.replace(R.id.fl_rank, shareRankFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        tab_star_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                PeriodRankFragment periodRankFragment = new PeriodRankFragment();
                fragmentTransaction.replace(R.id.fl_rank, periodRankFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share_get, menu);

        MenuItem menu_logout = menu.findItem(R.id.action_logout);
        MenuItem menu_home = menu.findItem(R.id.action_home);

        menu_logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                UserSharedPreference.removeUserId(getApplicationContext());

                finish();

                return true;
            }
        });

        menu_home.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                finish();

                return true;
            }
        });

        return true;
    }
}
