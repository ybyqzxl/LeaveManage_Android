package panda.li.leavemanage.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import panda.li.leavemanage.R;
import panda.li.leavemanage.activity.Base.BaseActivity;
import panda.li.leavemanage.entity.LeaveInfos;

public class LeaveInfoActivity extends BaseActivity implements View.OnTouchListener {

    @Bind(R.id.leave_info_toolbar)
    Toolbar leaveInfoToolbar;
    @Bind(R.id.tv_name_person)
    TextView tvNamePerson;
    @Bind(R.id.tv_depart_person)
    TextView tvDepartPerson;
    @Bind(R.id.tv_start_person)
    TextView tvStartPerson;
    @Bind(R.id.tv_end_person)
    TextView tvEndPerson;
    @Bind(R.id.tv_info_person)
    TextView tvInfoPerson;
    @Bind(R.id.tv_state_person)
    TextView tvStatePerson;
    @Bind(R.id.ll_btn)
    LinearLayout llBtn;
    @Bind(R.id.btn_xj)
    Button btnXj;
    @Bind(R.id.ll_sv)
    ScrollView scrollView;
    private LeaveInfos info;

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
        initToolBar();
        info = (LeaveInfos) getIntent().getSerializableExtra("info");
        initView();
        scrollView.setOnTouchListener(this);
    }

    private void initToolBar() {
        leaveInfoToolbar.setTitle("请假详情");
        leaveInfoToolbar.setNavigationIcon(R.drawable.ic_action_back);
        leaveInfoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        tvNamePerson.setText(info.getUserName());
        tvDepartPerson.setText(info.getUserDepart());
        tvStartPerson.setText(info.getStartTime());
        tvEndPerson.setText(info.getEndTime());
        tvInfoPerson.setText(info.getLeaveInfo());
        tvStatePerson.setText("已完成");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_leave_info;
    }

    @OnClick({R.id.btn_agree, R.id.btn_refuse, R.id.btn_xj})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_agree:
                break;
            case R.id.btn_refuse:
                break;
            case R.id.btn_xj:
                break;
        }
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
