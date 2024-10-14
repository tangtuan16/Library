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
        holder.date.setText(notification.getDate().toString().trim());
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
        if (notificationList == null) {
            return 0;
        } else
            return notificationList.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {
        private TextView title, content, date;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            content = itemView.findViewById(R.id.tvContent);
            date = itemView.findViewById(R.id.tvDate);
        }

    }

}
