package panda.li.leavemanage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import panda.li.leavemanage.R;
import panda.li.leavemanage.entity.LeaveInfos;

/**
 * Created by xueli on 2016/7/13.
 */
public class LeaveInfoAdapter extends RecyclerView.Adapter<LeaveInfoAdapter.LeaveInfoHolder> {

    List<LeaveInfos> leaveInfos;
    private Context context;
    private int resId;
    private OnItemClickListener listener;

    public void setLeaveInfos(List<LeaveInfos> leaveInfos) {
        this.leaveInfos = leaveInfos;
    }

    public LeaveInfoAdapter(Context context, int rescourseId, List<LeaveInfos> infos) {
        this.context = context;
        resId = rescourseId;
        leaveInfos = infos;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        listener = onItemClickListener;
    }

    @Override
    public LeaveInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(resId, parent, false);
        return new LeaveInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(final LeaveInfoHolder holder, int position) {
        LeaveInfos info = leaveInfos.get(position);///////
        holder.bindLeaveItem(info);
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getLayoutPosition();
                    listener.onItemClick(holder.itemView, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = holder.getLayoutPosition();
                    listener.onItemLongClick(holder.itemView, pos);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return leaveInfos.size();
    }

    class LeaveInfoHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.tv_depart)
        TextView tv_depart;
        @Bind(R.id.tv_start)
        TextView tv_start;
        @Bind(R.id.tv_end)
        TextView tv_end;

        public LeaveInfoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindLeaveItem(LeaveInfos info) {
            tv_name.setText(info.getUserName());
            tv_depart.setText(info.getUserDepart());
            tv_start.setText(info.getStartTime());
            tv_end.setText(info.getEndTime());
        }
    }

}

