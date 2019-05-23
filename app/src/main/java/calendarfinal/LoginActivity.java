package calendarfinal;
/**
 * 纯粹实现登录注册功能，其它功能都被注释掉了
 * 起作用的代码（连带着packag、import算上） 共 73 行
 * 不多吧？
 */

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursetable.DBHelper;
import com.example.coursetable.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by littlecurl 2018/6/24
 */
public class LoginActivity extends AppCompatActivity {

    private DBOpenHelper mDBOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(new DBHelper(this).getLockState("lockState")==0){
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(LoginActivity.this, "com.example.coursetable.MainActivity"));
            startActivity(intent);
            finish();//销毁此Activity
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mDBOpenHelper = new DBOpenHelper(this);
    }

    @BindView(R.id.tv_loginactivity_register)
    TextView mTvLoginactivityRegister;
    @BindView(R.id.rl_loginactivity_top)
    RelativeLayout mRlLoginactivityTop;
    @BindView(R.id.et_loginactivity_username)
    EditText mEtLoginactivityUsername;
    @BindView(R.id.et_loginactivity_password)
    EditText mEtLoginactivityPassword;
    @BindView(R.id.ll_loginactivity_two)
    LinearLayout mLlLoginactivityTwo;

    @OnClick({
           // R.id.iv_loginactivity_back,
            R.id.tv_loginactivity_register,
            //R.id.tv_loginactivity_forget,
            //R.id.tv_loginactivity_check,
            R.id.bt_loginactivity_login,
            //R.id.tv_loginactivity_else
    })

    public void onClick(View view) {
        switch (view.getId()) {
            //case R.id.iv_loginactivity_back:
                //TODO 返回箭头功能
            //    finish();//销毁此Activity
            //    break;
            case R.id.tv_loginactivity_register:
                //TODO 注册界面跳转
                ArrayList<User> data0 = mDBOpenHelper.getAllData();
                if(data0.isEmpty()){
                    startActivity(new Intent(this, RegisterActivity.class));
                    finish();
                    break;
                }
                else{
                    startActivity(new Intent(this, ResetActivity.class));
                    finish();
                    break;
                }

            //case R.id.tv_loginactivity_forget:   //忘记密码
                //TODO 忘记密码操作界面跳转
            //    startActivity(new Intent(this, FindPasswordActivity.class));
            //    break;
            //case R.id.tv_loginactivity_check:    //短信验证码登录
                // TODO 短信验证码登录界面跳转

            case R.id.bt_loginactivity_login:
                String name = mEtLoginactivityUsername.getText().toString().trim();
                String password = mEtLoginactivityPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    ArrayList<User> data = mDBOpenHelper.getAllData();
                    boolean match = false;
                    for(int i=0;i<data.size();i++) {
                        User user = data.get(i);
                        if (name.equals(user.getName()) && password.equals(user.getPassword())){
                            match = true;
                            break;
                        }else{
                            match = false;
                        }
                    }
                    if (match) {
                        Toast.makeText(this, "Accessed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(LoginActivity.this, "com.example.coursetable.MainActivity"));
                        //Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();//销毁此Activity
                    }else {
                        Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please type in", Toast.LENGTH_SHORT).show();
                }
                break;
            //case R.id.tv_loginactivity_else:
                //TODO 第三方登录，时间有限，未实现
            //    break;
        }
    }
}



