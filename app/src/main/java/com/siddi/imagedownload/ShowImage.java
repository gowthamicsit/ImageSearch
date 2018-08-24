package com.siddi.imagedownload;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by siddi on 24/08/18.
 */

public class ShowImage extends Activity {

    String imagename, imagelink;
    ImageView imageView, image_close;
    TextView textView;
    RelativeLayout image_main_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_dialog);

        imagename = getIntent().getStringExtra("imagename");
        imagelink = getIntent().getStringExtra("imagelink");

        getReferences();

        setScreenDimens();

        setData(imagename, imagelink);

        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setScreenDimens() {

        int width = image_main_layout.getWidth();
        ViewGroup.LayoutParams videoLayoutParams = image_main_layout.getLayoutParams();
        videoLayoutParams.width = (int) (Resources.getSystem().getDisplayMetrics().widthPixels/1.5);
        videoLayoutParams.height = (int) (Resources.getSystem().getDisplayMetrics().heightPixels/2.5);
        image_main_layout.setLayoutParams(videoLayoutParams);
    }

    private void setData(String imagename, String imagelink) {

        textView.setText(imagename);

        Glide.with(this)
                .load(imagelink)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }

    private void getReferences() {
        imageView = findViewById(R.id.image);
        image_close = findViewById(R.id.image_close);
        textView = findViewById(R.id.title);
        image_main_layout = findViewById(R.id.image_main_layout);
    }
}
