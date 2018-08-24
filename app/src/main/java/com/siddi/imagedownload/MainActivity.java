package com.siddi.imagedownload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    EditText btn_search_edt;
    ImageView search_btn;
    GridListAdaptor gridListAdaptor;
    ArrayList<ImageList> list = new ArrayList<ImageList>();
    TextWatcher editTextwatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getReference();
        hideKeyboard();

        setRequest("kittens");


        btn_search_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                btn_search_edt.addTextChangedListener(editTextwatcher);
            }
        });

        editTextwatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                list.clear();
                String text = btn_search_edt.getText().toString();
                setRequest(text);
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        };

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                hideKeyboard();
                String text = btn_search_edt.getText().toString();
                setRequest(text);
            }
        });
    }

    private void setRequest(String text) {
        new ImagesRequest(this, text, new ImageListener() {
            @Override
            public String imageResponse(String response) {

                setGridData(response);
                return null;
            };
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setGridData(String response) {

        if(response != null) {

            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONObject photos = jsonResponse.getJSONObject("photos");
                JSONArray jsonArray = photos.getJSONArray("photo");

                for(int i = 0; i < jsonArray.length(); i++) {
                    ImageList imageList=new ImageList();
                    JSONObject oj=(JSONObject) jsonArray.get(i);

                    imageList.setId(oj.getString("id"));
                    imageList.setFarm(oj.getString("farm"));
                    imageList.setIsfamily(oj.getString("isfamily"));
                    imageList.setIsfriend(oj.getString("isfriend"));
                    imageList.setIspublic(oj.getString("ispublic"));
                    imageList.setOwner(oj.getString("owner"));
                    imageList.setServer(oj.getString("server"));
                    imageList.setTitle(oj.getString("title"));
                    imageList.setSecret(oj.getString("secret"));

                    list.add(imageList);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //gridListAdaptor = new GridListAdaptor(this, list);
            gridView.setAdapter(new GridListAdaptor(this, list));
        }
    }

    private void getReference() {

        gridView = findViewById(R.id.gridview);
        btn_search_edt = findViewById(R.id.btn_search_edt);
        search_btn = findViewById(R.id.search_btn);
    }

    public void hideKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }


    private class GridListAdaptor extends BaseAdapter{

        Context context;
        ArrayList<ImageList> list;

        public GridListAdaptor(Context context, ArrayList<ImageList> list) {
            this.context = context;
            this.list = list;
        }
        @Override
        public int getCount() {
            return list.size()-1;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
           // view = (LayoutInflater.from(MainActivity.this)).inflate(R.layout.image_items_layout, null); // inflate the layout

            view = LayoutInflater.from(context).inflate(R.layout.image_items_layout, null);

            ImageView icon = (ImageView) view.findViewById(R.id.image);
            TextView text = (TextView) view.findViewById(R.id.tv_title);

            String url = "http://farm" + list.get(i).getFarm() + ".static.flickr.com/" + list.get(i).getServer() + "/" + list.get(i).getId() + "_" + list.get(i).getSecret() + ".jpg";

            Glide.with(context)
                    .load(url)
                    .error(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(icon);
            //icon.setBackground(context.getResources().getDrawable(R.drawable.mindmap_border));
            text.setText(list.get(i).getTitle());



            return view;
        }
    }
}
