package com.pigbear.hi_andgmusic.ui.collections;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bilibili.magicasakura.widgets.TintToolbar;
import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.common.PhotoUtil;
import com.pigbear.hi_andgmusic.common.RxBus;
import com.pigbear.hi_andgmusic.data.CollectionBean;
import com.pigbear.hi_andgmusic.db.CollectionManager;
import com.pigbear.hi_andgmusic.event.CollectionUpdateEvent;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectionCreateActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    TintToolbar toolbar;
    @Bind(R.id.collection_cover)
    ImageView collectionCover;
    @Bind(R.id.collection_title)
    TextView collectionTitle;
    @Bind(R.id.collection_des)
    EditText collectionDes;

    private ActionBar actionBar;
    private PhotoUtil photoUtil;
    private CollectionBean collectionBean;
    private boolean hasChange;
    private int cid;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_create);
        ButterKnife.bind(this);

        setToolBar();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //相册获取工具类
        photoUtil = new PhotoUtil(this);
        initData();

    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.actionbar_back);
        if(getIntent() != null) {
            cid = getIntent().getIntExtra("cid",-1);
            position = getIntent().getIntExtra("position", -2);
            if(cid == -1) {
                actionBar.setTitle(R.string.collection_create_title);
            }
        }
    }

    private void initData() {
        hasChange = false;
        if(cid != -1) {
            collectionBean = CollectionManager.getInstance().getCollectionById(cid);
            collectionCover.setImageURI(Uri.parse(collectionBean.getCoverUrl()));
            collectionTitle.setText(collectionBean.getTitle());
            collectionDes.setText(collectionBean.getDescription());
        }else{
            collectionBean = new CollectionBean(-1,getString(R.string.collection_title_default),"",0,"");
        }

        collectionDes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hasChange = true;
                String editText = s.toString();
                collectionBean.setDescription(editText);
            }
        });

    }

    @OnClick({R.id.collection_cover, R.id.collection_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.collection_cover://拍照
                new MaterialDialog.Builder(this)
                        .title(R.string.collection_dialog_cover_title)
                        .items(R.array.collection_cover)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                switch (position){
                                    case 0:
                                        photoUtil.takePhoto();
                                        break;
                                    case 1:
                                        photoUtil.picPhoto();
                                        break;
                                }
                            }
                        })
                        .show();
                break;
            case R.id.collection_title:
                new MaterialDialog.Builder(this)
                        .title(R.string.collection_dialog_name)
                        .inputRangeRes(1, 20, R.color.theme_color_PrimaryAccent)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input(collectionBean.getTitle(), "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                if (!TextUtils.isEmpty(input)) {
                                    collectionBean.setTitle(String.valueOf(input));
                                    collectionTitle.setText(input);
                                    hasChange = true;
                                }
                            }
                        }).show();
                break;
        }
    }

    /**
     * 拍照、相册取相片，裁剪回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtil.TAKE_PHOTO:
                //拍照的回调
                if (resultCode == RESULT_OK)
                    photoUtil.cropImageUri(photoUtil.getUri(), 200, 200, false, PhotoUtil.CROP_PICTURE);
                break;
            case PhotoUtil.CHOOSE_PICTURE:
                //从相册中选择照片的回调
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();
                        Uri uri1 = Uri.fromFile(new File(PhotoUtil.getPath(this, uri)));
                        photoUtil.cropImageUri(uri1, 200, 200, false, PhotoUtil.CROP_PICTURE);
                    }
                }
                break;
            case PhotoUtil.CROP_PICTURE:
                //拍照切图的回调
                if (resultCode == RESULT_OK) {
                    Bitmap map = photoUtil.decodeUriAsBitmap(photoUtil.getUri());
                    Drawable drawable = new BitmapDrawable(map);
                    float w = drawable.getIntrinsicWidth();
                    float H = drawable.getIntrinsicWidth();
                    if (w < 50.0 || H < 50.0) {
                        //头像太小
//                        showSnackBar(cover, R.string.collection_edit_cover_small);
                        return;
                    }
                    hasChange = true;
                    collectionCover.setImageBitmap(map);
                    collectionBean.setCoverUrl(photoUtil.getUri().getPath());
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if(item.getItemId() == R.id.action_store) {
            if(position == -2) {
                CollectionManager.getInstance().setCollection(collectionBean);
            }else{
                CollectionManager.getInstance().setCollection(collectionBean,position);
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(this)
                .title(R.string.collection_dialog_update_title)
                .content(R.string.collection_dialog_update_content)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        CollectionManager.getInstance().setCollection(collectionBean);
                        RxBus.getDefault().post(new CollectionUpdateEvent(true));
                        dialog.dismiss();
                        finish();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }
}
