package edu.neu.mobile.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//responsible for display data from model into row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    List<String> items;
    OnLongClickListener onLongClickListener;

    //constructor
    public ItemsAdapter(List<String> items, OnLongClickListener onLongClickListener) {
        this.items = items;
        this.onLongClickListener = onLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //use layout inflater to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        //Wrap it inside a view holder and return it
        return new ViewHolder(todoView);
    }

    // responsible to binding data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //grab item at position
        String item = items.get(position);

        //Bind the item into the specified view holder，bind方法在后面自己定义了
        holder.bind(item);
    }

    //tell how many item in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //viewholder 是什么？？创建一个viewHolder class，一个viewHolder就是一条model数据，把model包装一下
    // container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //update view inside of the view holder with this data
        public void bind(String item) {
            tvItem.setText(item);

            //view 里添加一个长点击事件
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Notify the listener which position was long pressed.
                    onLongClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }

}

