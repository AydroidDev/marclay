package com.fourty_eight_dps.marclay;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fourty_eight_dps.marclay.core.firebase.SyncedNotification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

  private SortedList<SyncedNotification> notifications;

  public NotificationAdapter() {
    notifications = new SortedList<>(SyncedNotification.class,
        new SortedList.Callback<SyncedNotification>() {

          @Override public int compare(SyncedNotification o1, SyncedNotification o2) {
            return o1.compareTo(o2);
          }

          @Override public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
          }

          @Override public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
          }

          @Override public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
          }

          @Override public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
          }

          @Override
          public boolean areContentsTheSame(SyncedNotification oldItem,
              SyncedNotification newItem) {
            return oldItem.getKey().equals(newItem.getKey());
          }

          @Override
          public boolean areItemsTheSame(SyncedNotification item1, SyncedNotification item2) {
            return item1.getKey().equals(item2.getKey());
          }
        });
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificaton, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    SyncedNotification notification = notifications.get(position);
    holder.message.setText(notification.getMessage());
  }

  @Override public int getItemCount() {
    return notifications.size();
  }

  public void add(SyncedNotification notification) {
    notifications.add(notification);
  }

  public void remove(SyncedNotification notification) {
    notifications.remove(notification);
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView message;

    public ViewHolder(View itemView) {
      super(itemView);
      message = (TextView) itemView.findViewById(R.id.message);
    }
  }
}
