package com.example.coursetable;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursetable.utils.BitmapUtils;
import com.example.coursetable.utils.FileUtil;
import com.example.coursetable.view.CircleImageView;

import java.io.File;
import java.util.ArrayList;

import calendarfinal.DBOpenHelper;
import calendarfinal.User;

import static com.example.coursetable.utils.FileUtil.getRealFilePathFromUri;
//目前想的是一进来就打开Syllabus，所以目前主活动是它，往后可能会改

public class SettingActivity extends AppCompatActivity {
    static Activity Setting;
    private calendarfinal.DBOpenHelper dbOpenHelper;
    private RelativeLayout day;
    private DrawerLayout mDrawerLayout;
    private DatabaseHelper databaseHelper = new DatabaseHelper(this, "database.db", null, 1);
    //private static int sCurrentTheme = R.style.theme_default;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("duan", "0" );
        super.onCreate(savedInstanceState);
        setTheme(new DBHelper(this).getTheme("theme"));
        setContentView(R.layout.activity_setting);
        //加载顶部菜单栏（别的活动可能也需要）
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Setting=this;
        //设置界面头像
        DBHelper dbHelper = new DBHelper(this);
        CircleImageView  headImage1 = findViewById(R.id.head_image);
        byte[] data = dbHelper.getBitmapByName("pic");
        if (data != null)   {
            Bitmap bitmap = BitmapUtils.getImage(data);
            headImage1.setImageBitmap(bitmap);
        }

        NavigationView naviView = findViewById(R.id.nav_view);
        View headerView = naviView.getHeaderView(0);


        dbOpenHelper = new DBOpenHelper(this);
        TextView helloText = (TextView) headerView.findViewById(R.id.username);
        String s;
        ArrayList<User> data666 = dbOpenHelper.getAllData();
        s = data666.get(0).getName();
        TextView helloText2 = (TextView) findViewById(R.id.textView);

        helloText2.setText(s);

        helloText.setText(s);
        helloText.setTextColor(Color.WHITE);


       // helloText2.setTextColor(Color.BLACK);


        //抽屉布局
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        NavigationView naviView = findViewById(R.id.nav_view);
////        setContentView(R.layout.rmain);
//        View headerView = naviView.getHeaderView(0);

        de.hdodenhof.circleimageview.CircleImageView  headImage2 = headerView.findViewById(R.id.icon_image);
        byte[] data2 = dbHelper.getBitmapByName("pic");
        if (data2 != null)   {
            Bitmap bitmap = BitmapUtils.getImage(data2);
            headImage2.setImageBitmap(bitmap);
        }

        //navi栏的默认选择
        naviView.setCheckedItem(R.id.nav_setting);
        //对navi栏进行事件监听

        //你们的活动加载写在这个函数里面
        naviView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.nav_syllabus){
                    Intent intent = new Intent( SettingActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.nav_reminder){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(SettingActivity.this, "simplenotepad.MainActivity"));
                    startActivity(intent);
                    finish();
                }else if(id == R.id.nav_calendar) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(SettingActivity.this, "calendarfinal.MainActivity"));
                    startActivity(intent);
                    finish();

                } else if(id == R.id.nav_setting) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);

                }
                return true;
            }
        });

        Button logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,calendarfinal.LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button privacy = (Button)findViewById(R.id.privacy);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,PrivacyActivity.class);
                startActivity(intent);
            }
        });
        Button change_theme = (Button)findViewById(R.id.change_theme);
        change_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,ThemeActivity.class);
                startActivity(intent);
            }
        });
        Button about = (Button)findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });
        headImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                uploadHeadImage();
            }
        });
        Log.d("duan", "3" );
    }





    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //头像1
    private CircleImageView headImage1;
    //调用照相机返回图片文件
    private File tempFile;
    // 1: qq, 2: weixin
    private int type;

    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
     //   TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.smain, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

//        btnCarema.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("duan", "cc" );
//                //权限判断
//                if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    //申请WRITE_EXTERNAL_STORAGE权限
//                    ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
//                } else {
//                    //跳转到调用系统相机
//                    gotoCamera();
//                }
//                popupWindow.dismiss();
//            }
//        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("duan", "cp" );
                //权限判断
                if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.d("duan", "if" );
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    Log.d("duan", "else" );
                    //跳转到相册
                    gotoPhoto();
                }
                Log.d("duan", "end" );
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    /**
     * 外部存储权限申请返回
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCamera();
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto();
            }
        }
    }


    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Log.d("evan", "*****************打开图库********************");
        //跳转到调用系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCamera() {
        Log.d("evan", "*****************打开相机********************");
        //创建拍照存储的图片文件
        tempFile = new File(FileUtil.checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"), System.currentTimeMillis() + ".jpg");

        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
//            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            Uri contentUri = FileProvider.getUriForFile(SettingActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
//        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
      //  }
        startActivityForResult(intent, REQUEST_CAPTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    CircleImageView  headImage1 = findViewById(R.id.head_image);
                    headImage1.setImageBitmap(bitMap);

                    //将bitMap转为二进制上传数据库
                    dbHelper = new DBHelper(this);
                    byte[] b=BitmapUtils.getBytes(bitMap);
                    dbHelper.addBitmap("pic", b);
                    NavigationView naviView = findViewById(R.id.nav_view);
//        setContentView(R.layout.rmain);
                    View headerView = naviView.getHeaderView(0);

                    de.hdodenhof.circleimageview.CircleImageView  headImage2 = headerView.findViewById(R.id.icon_image);
                    byte[] data2 = dbHelper.getBitmapByName("pic");
                    if (data2 != null)   {
                        Bitmap bitmap = BitmapUtils.getImage(data2);
                        headImage2.setImageBitmap(bitmap);
                    }


                }
                break;
        }
    }


    /**
     * 打开截图界面
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }


}
