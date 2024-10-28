package com.example.Views.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Contracts.NotificationContract;
import com.example.Models.Notification;
import com.example.Presenters.NotificationPresenter;
import com.example.btl_libary.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
    private List<Notification> notificationList;
    private Context context;
    private NotificationPresenter notificationPresenter;


    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.title.setText(notification.getTitle());
        holder.content.setText(notification.getContent());
        holder.time.setText(notification.getTime());
        if(notification.getStatus()==0)
            holder.status.setText("Chua doc");
        else
            holder.status.setText("Da doc");

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa thông báo này không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                notificationPresenter = new NotificationPresenter(context, (NotificationContract.View) context);
                                notificationPresenter.deleteNotification(notification.getId());
                                notificationPresenter.loadNotification();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
       return notificationList != null ? notificationList.size() : 0;
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {
        private TextView title, content, time, status;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            content = itemView.findViewById(R.id.tvContent);
            time = itemView.findViewById(R.id.tvTime);
            status=itemView.findViewById(R.id.tvStatus);
        }

    }

}
