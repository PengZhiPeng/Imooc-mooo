package com.imooc.mooo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SetNameActivity extends Activity {

    Button mSetNameOK;
    Button mSetNameCancel;
    EditText mSetNameText;
    String mMyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);

        initView();
        initListener();
    }


    private void initView() {
        mSetNameOK = (Button) findViewById(R.id.btn_setname_ok);
        mSetNameCancel = (Button) findViewById(R.id.btn_setname_cancel);
        mSetNameText = (EditText) findViewById(R.id.et_set_myname);
    }

    private void initListener() {
        mSetNameOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyName=mSetNameText.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences("data",
                        MODE_PRIVATE).edit();
                editor.putString("name", mMyName);
                editor.commit();
                Intent intent = new Intent(SetNameActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mSetNameCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SetNameActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//可返回上一层
        actionBar.setDisplayShowTitleEnabled(false);//隐藏actionbar标题
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_set_name, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //点击actionbar中的应用图标返回mainactivity
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
