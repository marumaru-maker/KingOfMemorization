package marumaru.v01.kingofmemorization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;
    private ImageButton imageButton_card, imageButton_share;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    AlertDialog.Builder alertBuilder;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 툴바 등록
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        /*
        getSupportActionBar().setIcon(R.drawable.brain);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        */

        // 넘어온 Info 있으면 띄우기 * "intent != null" 이라는 조건으로 하니깐 true 로 지나가서 null 이라는 값을 리턴했다.
        Intent intent = getIntent();
        if(!TextUtils.isEmpty(intent.getStringExtra("info_title"))){
            String info_type = intent.getStringExtra("info_title");
            String info_content = intent.getStringExtra("info_content");

            alertBuilder = new AlertDialog.Builder(MainActivity.this);
            dialog = alertBuilder.setMessage(info_type + ": " + info_content)
                    .setPositiveButton("확인", null)
                    .create();
            dialog.show();
        }


        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        imageButton_card = findViewById(R.id.btn_card);
        imageButton_share = findViewById(R.id.btn_share);

        // Card Regsitering Listener
        imageButton_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserSharedPreference.getUserId(MainActivity.this).length() == 0){
                    // 로그인 안 된 상태
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    // 로그인 된 상태
                    Intent intent = new Intent(getApplicationContext(), CardListActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Card Sharing Listener
        imageButton_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserSharedPreference.getUserId(MainActivity.this).length() == 0) {
                    // 로그인 안 된 상태
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    // 로그인 된 상태
                    Intent intent = new Intent(getApplicationContext(), CardShareActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        if(UserSharedPreference.getUserId(MainActivity.this).length() == 0){
            // 로그인 안 된 상태
            inflater.inflate(R.menu.menu_main_login, menu);

            MenuItem menu_login = menu.findItem(R.id.action_login);
            MenuItem menu_join = menu.findItem(R.id.action_join);

            menu_login.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    Toast.makeText(getApplicationContext(), "Login Clicked", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);

                    return true;
                }
            });

            menu_join.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                    startActivity(intent);

                    return true;
                }
            });

        } else {
            // 로그인 된 상태
            inflater.inflate(R.menu.menu_main_logout, menu);

            MenuItem menu_logout = menu.findItem(R.id.action_logout);
            menu_logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    UserSharedPreference.removeUserId(MainActivity.this);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return new ScreenSlidePageFragment();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }


    }
}
