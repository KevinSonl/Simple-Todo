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

    public interface OnClickListener {
        void onItemClicked(int position);
    }
    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    List<String> items;
    //都是在上面定义的interface
    OnLongClickListener onLongClickListener;
    OnClickListener onClickListener;

    //constructor, 里面主要是用来和 MainActivity进行的操作进行沟通的！！！！
    public ItemsAdapter(List<String> items, OnLongClickListener onLongClickListener, OnClickListener onClickListener) {
        this.items = items;
        this.onLongClickListener = onLongClickListener;
        this.onClickListener = onClickListener;
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
        //在viewHolder里添加点击listener事件
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

            //添加点击转移到新activity事件,同样添加一个interface
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }

}

