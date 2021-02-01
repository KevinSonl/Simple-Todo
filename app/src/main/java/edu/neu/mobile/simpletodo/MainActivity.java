package edu.neu.mobile.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.edText);
        rvItems = findViewById(R.id.rvItems);

        //load the saved items
        loadItems();

        //创建一个onLongClickListener实例，创建interfacce实例时，需要对其中的抽象方法进行implement
        //implement要在这里写因为，但是position需要在itemAdaptor里获取
        // 其中方法也定义一下（删除并且toast提示）
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //这个class写这里的原因是要从model里删除这个
                // delete the item from model（items）
                items.remove(position);
                //notify the adapter，删除
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                //save the updated items in arrayList
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position: " + position);
                //点击以后，用intent跳转activity

                //1. create new activity (Intent!), 指名从哪到哪
                Intent i = new Intent(MainActivity.this, EditActivity.class);

                //2. pass data being edit, 传数据过去
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);

                //3. display activity,接受intent，和定义的一个code
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        //点击事件
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                // add to the model
                items.add(todoItem);
                //Notify adapter that item is inserted
                itemsAdapter.notifyItemInserted(items.size()-1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                //save the updated items in arrayList
                saveItems();
            }
        });
    }

    //handle result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //确认requestCode时对上的并且返回result是ok的
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            //get updated text value
            String updatedText = data.getStringExtra(KEY_ITEM_TEXT);
            //extract original position of edited item
            int updatedPosition = data.getExtras().getInt(KEY_ITEM_POSITION);

            //update the model at the right position with new item text
            items.set(updatedPosition, updatedText);
            //notify adaptor
            itemsAdapter.notifyItemChanged(updatedPosition);

            //persist the changes
            Toast.makeText(getApplicationContext(), "Item has been updated", Toast.LENGTH_LONG).show();
            saveItems();

        } else {
            Log.w("MainActivity", "Unknown call to onActivity result");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // load items by reading everyline of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error file reading", e);
            items = new ArrayList<>();
        }
    }

    //save itrems by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error write file", e);
        }
    }

}