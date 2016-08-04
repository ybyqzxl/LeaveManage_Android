package panda.li.leavemanage.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import panda.li.leavemanage.R;
import panda.li.leavemanage.activity.Base.BaseActivity;
import panda.li.leavemanage.adapter.LeaveInfoAdapter;
import panda.li.leavemanage.constans.Constants;
import panda.li.leavemanage.entity.AccountManager;
import panda.li.leavemanage.entity.LeaveInfos;
import panda.li.leavemanage.entity.LoginConfig;
import panda.li.leavemanage.json.LeaveInfoJson;
import panda.li.leavemanage.service.LoginService;
import panda.li.leavemanage.service.Respon;
import panda.li.leavemanage.service.RetroFactory;
import panda.li.leavemanage.utils.JsonUtil;
import panda.li.leavemanage.view.DividerItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int ADD = 1;

    @Bind(R.id.main_toolbar)
    Toolbar mainToolbar;
    @Bind(R.id.main_recyclerview)
    RecyclerView mainRecyclerView;
    @Bind(R.id.main_drawer)
    DrawerLayout mainDrawer;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.set_head)
    ImageView setHead;
    @Bind(R.id.set_name)
    TextView setName;
    @Bind(R.id.main_ll)
    LinearLayout mainLl;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private LeaveInfoAdapter adapter;
    private List<LeaveInfos> infos = new ArrayList<>();
    private List<LeaveInfos> loadMoreInfos = new ArrayList<>();
    private int lastVisibleItemPosition;
    private LoginConfig loginConfig;
    private AccountManager accountManager;
    private long time;
    //private boolean isScroll;
    private int scrolled;
    private long mExitTime = 0;

    private MyReceiver myReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        IntentFilter filter = new IntentFilter("panda.li.submit.success");
        myReceiver = new MyReceiver();
        registerReceiver(myReceiver, filter);
        mainToolbar.setTitle("请假管理");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mainDrawer, mainToolbar, R
                .string.open, R.string.close);
        mainDrawer.addDrawerListener(actionBarDrawerToggle);
        mainToolbar.inflateMenu(R.menu.leave_add_menu);
        mainToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_add_leave) {
                    startActivityForResult(new Intent(MainActivity.this, LeaveAddActivity.class),
                            ADD);
                }
                return true;
            }
        });
        initRecyclerView();
        accountManager = AccountManager.getInstance();
        loginConfig = accountManager.getLoginConfig();
        initView();
    }

    private void initView() {
        if (loginConfig.getUserGender() == 1) {
            setHead.setImageResource(R.drawable.head_male);
        } else {
            setHead.setImageResource(R.drawable.head_female);
        }
        setName.setText(loginConfig.getUserRealName());
    }

    private void loadMyLeaveInfo() {
        time = System.currentTimeMillis();
        infos.clear();
        adapter.setLeaveInfos(infos);
        adapter.notifyDataSetChanged();
        LoginService.LoadUserLeaveInfo loadUserLeaveInfo = RetroFactory.getRetrofit(Constants
                .LEAVE_MANAGE_URL).create(LoginService.LoadUserLeaveInfo.class);
        Call<Respon> call = loadUserLeaveInfo.loadUserLeaveInfo(loginConfig.getUserName(), time);
        call.enqueue(new Callback<Respon>() {
            @Override
            public void onResponse(Call<Respon> call, Response<Respon> response) {
                infos = JsonUtil.toObj(new TypeToken<LeaveInfoJson>() {
                }, response.body().getResponResult()).getLeaveInfos();
                if (infos.size() > 0) {
                    adapter.setLeaveInfos(infos);
                    adapter.notifyDataSetChanged();
                } else {
                    Snackbar.make(mainLl, "暂无新的请假信息", Snackbar.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Respon> call, Throwable t) {
                Snackbar.make(mainLl, "加载失败", Snackbar.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void loadMoreLeaveInfo(int position) {
        time = infos.get(position - 1).getTimeStamp();
        LoginService.LoadUserLeaveInfo loadUserLeaveInfo = RetroFactory.getRetrofit(Constants
                .LEAVE_MANAGE_URL).create(LoginService.LoadUserLeaveInfo.class);
        Call<Respon> call = loadUserLeaveInfo.loadUserLeaveInfo(loginConfig.getUserName(), time);
        call.enqueue(new Callback<Respon>() {
            @Override
            public void onResponse(Call<Respon> call, Response<Respon> response) {
                loadMoreInfos.clear();
                loadMoreInfos = JsonUtil.toObj(new TypeToken<LeaveInfoJson>() {
                }, response.body().getResponResult()).getLeaveInfos();
                if (loadMoreInfos.size() > 0) {
                    for (LeaveInfos leaveInfos : loadMoreInfos) {
                        infos.add(leaveInfos);
                        adapter.setLeaveInfos(infos);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Snackbar.make(mainLl, "无更多请假信息", Snackbar.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Respon> call, Throwable t) {
                Snackbar.make(mainLl, "加载失败", Snackbar.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initRecyclerView() {

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadMyLeaveInfo();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mainRecyclerView.setLayoutManager(layoutManager);
        mainRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL_LIST));
        adapter = new LeaveInfoAdapter(this, R.layout.item_leave_info, infos);
        mainRecyclerView.setAdapter(adapter);

        mainRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                //isScroll = Math.abs(dy) > 5;
                scrolled = dy;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (lastVisibleItemPosition + 1 == adapter.getItemCount() && newState ==
                        RecyclerView.SCROLL_STATE_IDLE) {
                    //if (isScroll) {
                    if (scrolled > 0) {
                        swipeRefreshLayout.setRefreshing(true);
                        loadMoreLeaveInfo(adapter.getItemCount());
                        // }
                    }
                }
            }
        });


        adapter.setOnItemClickListener(new LeaveInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LeaveInfos info = infos.get(position);
                Intent intent = new Intent(MainActivity.this, LeaveInfoActivity.class);
                intent.putExtra("info", info);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setTitle
                        ("确定要删除该条目吗？").setPositiveButton("确定", new DialogInterface
                        .OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        infos.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                }).setNegativeButton("取消", null);
                builder.show();
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (mainDrawer.isDrawerOpen(GravityCompat.START)) {
            mainDrawer.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.ll_my_leave, R.id.ll_my_shenpi, R.id.btn_exit_app})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_my_leave:
                loadMyLeaveInfo();
                mainDrawer.closeDrawers();
                break;
            case R.id.ll_my_shenpi:
                break;
            case R.id.btn_exit_app:
                accountManager.logout();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
        }
    }

    @Override
    public void onRefresh() {
        loadMyLeaveInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isSubmit = intent.getBooleanExtra("submit", false);
            if (isSubmit) {
                loadMyLeaveInfo();
            }
        }
    }

    /**
     * 双击返回退出
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mainDrawer.isDrawerOpen(GravityCompat.START)) {
                mainDrawer.closeDrawers();
            } else {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Snackbar.make(mainLl, "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}