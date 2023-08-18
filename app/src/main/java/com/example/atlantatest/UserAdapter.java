package com.example.atlantatest;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atlantatest.response.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements Filterable {

    private List<User> userList;
    private List<User> filterUserList;

    public UserAdapter(List<User> userList) {
        if (this.userList == null) {
            this.userList = userList;
            this.filterUserList = userList;
            notifyItemChanged(0, filterUserList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return UserAdapter.this.userList.size();
                }

                @Override
                public int getNewListSize() {
                    return userList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return UserAdapter.this.userList.get(oldItemPosition).getName() == userList.get(newItemPosition).getName();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    User newDoc = UserAdapter.this.userList.get(oldItemPosition);
                    User oldDoc = userList.get(newItemPosition);
                    return newDoc.getName() == oldDoc.getName();
                }
            });
            this.userList = userList;
            result.dispatchUpdatesTo(this);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.user_item_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.tvId.setText(filterUserList.get(position).getId());
        holder.tvName.setText(filterUserList.get(position).getName());
        holder.tvUserName.setText(filterUserList.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        if (userList != null){
            return filterUserList.size();
        }else {
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()){
                    filterUserList = userList;
                }else {
                    List<User> filteredList = new ArrayList<>();
                    for (User user : userList){
                        if (user.getName().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(user);
                        }
                    }
                    filterUserList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterUserList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterUserList =(ArrayList<User>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId, tvName, tvUserName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvName = itemView.findViewById(R.id.tvName);
            tvUserName = itemView.findViewById(R.id.tvUserName);
        }
    }
}

