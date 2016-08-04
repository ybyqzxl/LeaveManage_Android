package panda.li.leavemanage.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import panda.li.leavemanage.R;
import panda.li.leavemanage.activity.Base.BaseActivity;
import panda.li.leavemanage.constans.Constants;
import panda.li.leavemanage.entity.AccountManager;
import panda.li.leavemanage.entity.LoginConfig;
import panda.li.leavemanage.service.LoginService;
import panda.li.leavemanage.service.Respon;
import panda.li.leavemanage.service.RetroFactory;
import panda.li.leavemanage.utils.Encrypt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xueli on 2016/7/12.
 */
public class LeaveAddActivity extends BaseActivity implements View.OnTouchListener {

    @Bind(R.id.leave_add_toolbar)
    Toolbar leaveAddToolbar;
    @Bind(R.id.tv_leave_why)
    TextView tvLeaveWhy;
    @Bind(R.id.tv_leave_start)
    TextView tvLeaveStart;
    @Bind(R.id.tv_leave_end)
    TextView tvLeaveEnd;
    @Bind(R.id.et_leave_why)
    EditText etLeaveWhy;
    @Bind(R.id.ll_leaveAdd)
    LinearLayout llLeaveAdd;
    @Bind(R.id.sv_leave_add)
    ScrollView scrollView;
    private String[] items = new String[]{"事假", "病假", "其他"};
    private Calendar calendar;
    private DatePicker datePicker;
    private Date date;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private int year, month, day;
    private LoginConfig loginConfig;
    private AccountManager accountManager;

    ////////滑动返回////////////////
    //手指向右滑动时的最小速度
    private static final int XSPEED_MIN = 200;

    //手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 300;

    //记录手指按下时的横坐标。
    private float xDown;

    //记录手指移动时的横坐标。
    private float xMove;

    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;
    /////////////////////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        scrollView.setOnTouchListener(this);
        leaveAddToolbar.setTitle("我的请假");
        leaveAddToolbar.setNavigationIcon(R.drawable.ic_action_back);
        leaveAddToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        accountManager = AccountManager.getInstance();
        loginConfig = accountManager.getLoginConfig();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_leave_add;
    }

    @OnClick({R.id.ll_leave_why, R.id.ll_leave_start, R.id.tv_leave_end, R.id.btn_submit_leave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_leave_why:
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("请假类型")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                tvLeaveWhy.setText(items[i]);
                            }
                        });
                builder.show();
                break;
            case R.id.ll_leave_start:
                View vStart = getLayoutInflater().inflate(R.layout.date_picker_dialog, null);
                datePicker = (DatePicker) vStart.findViewById(R.id.date_picker);
                datePicker.init(year, month, day, null);
                AlertDialog.Builder startBuilder = new AlertDialog.Builder(this).setView(vStart)
                        .setTitle("请选择开始时间").setPositiveButton
                                ("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        int year = datePicker.getYear();
                                        int month = datePicker.getMonth();
                                        int day = datePicker.getDayOfMonth();
                                        date = new GregorianCalendar(year, month, day).getTime();
                                        String time = dateFormat.format(date);
                                        tvLeaveStart.setText(time);
                                    }
                                }).setNegativeButton("取消", null);
                startBuilder.show();
                break;
            case R.id.tv_leave_end:
                View vEnd = getLayoutInflater().inflate(R.layout.date_picker_dialog, null);
                datePicker = (DatePicker) vEnd.findViewById(R.id.date_picker);
                datePicker.init(year, month, day, null);
                AlertDialog.Builder endBuilder = new AlertDialog.Builder(this).setView(vEnd)
                        .setTitle("请选择结束时间").setPositiveButton
                                ("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        int year = datePicker.getYear();
                                        int month = datePicker.getMonth();
                                        int day = datePicker.getDayOfMonth();
                                        date = new GregorianCalendar(year, month, day).getTime();
                                        String time = dateFormat.format(date);
                                        tvLeaveEnd.setText(time);
                                    }
                                }).setNegativeButton("取消", null);
                endBuilder.show();
                break;
            case R.id.btn_submit_leave:
                if (tvLeaveWhy.getText().toString().equals("请选择")) {
                    Snackbar.make(llLeaveAdd, "请选择请假类型", Snackbar.LENGTH_SHORT).show();
                } else if (tvLeaveStart.getText().toString().equals("请选择")) {
                    Snackbar.make(llLeaveAdd, "请选择开始时间", Snackbar.LENGTH_SHORT).show();
                } else if (tvLeaveEnd.getText().toString().equals("请选择")) {
                    Snackbar.make(llLeaveAdd, "请选择结束时间", Snackbar.LENGTH_SHORT).show();
                } else if (etLeaveWhy.getText().toString().isEmpty()) {
                    Snackbar.make(llLeaveAdd, "请输入请假事由", Snackbar.LENGTH_SHORT).show();
                } else {
                    submitLeaveInfo();
                }
                break;
        }
    }

    private String byte2hex(String s) {
        try {
            return Encrypt.byte2hex(s.getBytes(Encrypt.ENCODING));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void submitLeaveInfo() {
        LoginService.PublishUserLeaveInfo pubLishUserInfo = RetroFactory.getRetrofit(Constants
                .LEAVE_MANAGE_URL).create(LoginService.PublishUserLeaveInfo.class);
        Call<Respon> call = pubLishUserInfo.publishUserLeaveInfo(loginConfig.getUserName(),
                byte2hex(loginConfig.getUserRealName()), byte2hex(loginConfig.getUserDept())
                , byte2hex(tvLeaveWhy.getText().toString())
                , byte2hex(tvLeaveStart.getText().toString()), byte2hex(tvLeaveEnd.getText()
                        .toString()), byte2hex(etLeaveWhy.getText().toString()));
        call.enqueue(new Callback<Respon>() {
            @Override
            public void onResponse(Call<Respon> call, Response<Respon> response) {
                if (response.body().getResponState().equals(Constants.HttpResponseState
                        .REQ_SUCCESS.name())) {
                    Snackbar.make(llLeaveAdd, "提交成功", Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent("panda.li.submit.success");
                    intent.putExtra("submit", true);
                    sendBroadcast(intent);
                    finish();
                } else {
                    Log.i("LeaveAdd", call.request().toString());
                    Snackbar.make(llLeaveAdd, "提交失败", Snackbar.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Respon> call, Throwable t) {

            }
        });
    }

    //////////////////////////////////


    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        createVelocityTracker(motionEvent);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = motionEvent.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (xDown < 90) {
                    xMove = motionEvent.getRawX();
                    //活动的距离
                    int distanceX = (int) (xMove - xDown);
                    //获取顺时速度
                    int xSpeed = getScrollVelocity();
                    //当滑动的距离大于我们设定的最小距离且滑动的瞬间速度大于我们设定的速度时，返回到上一个activity
                    if (distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
                        finish();
                        //设置切换动画，从右边进入，左边退出
                        overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return true;
    }
}
