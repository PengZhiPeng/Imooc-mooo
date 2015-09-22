package com.imooc.mooo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.imooc.mooo.bean.ChatMessage;
import com.imooc.mooo.utils.HttpUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends Activity {

    private ListView mMsgs;
    private ChatMessageAdapter mAdapter;
    private List<ChatMessage> mDatas;
    private EditText mInputMsg;
    private Button mSendMsg;
//    private ImageView iv = null;
//    private TextView tv_myName;
//    private EditText editTextForNewName;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            // 等待接收，子线程完成数据的返回
            ChatMessage fromMessge = (ChatMessage) msg.obj;
            mDatas.add(fromMessge);
            mAdapter.notifyDataSetChanged();
            mMsgs.setSelection(mDatas.size()-1);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        initDatas();
        initListener();
    }

    private void initListener()
    {
        mSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String toMsg = mInputMsg.getText().toString();
                if (TextUtils.isEmpty(toMsg)) {
                    Toast.makeText(MainActivity.this, "发送消息不能为空！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                ChatMessage toMessage = new ChatMessage();
                toMessage.setDate(new Date());
                toMessage.setMsg(toMsg);
                toMessage.setType(ChatMessage.Type.OUTCOMING);
//                toMessage.setName(tv_myName.getText().toString());
                mDatas.add(toMessage);
                mAdapter.notifyDataSetChanged();
                mMsgs.setSelection(mDatas.size() - 1);

                mInputMsg.setText("");

                new Thread() {
                    public void run() {
                        ChatMessage fromMessage = HttpUtils.sendMessage(toMsg);
                        Message m = Message.obtain();
                        m.obj = fromMessage;
                        mHandler.sendMessage(m);
                    }
                }.start();

            }
        });
    }

    private void initDatas()
    {
        mDatas = new ArrayList<ChatMessage>();
        mDatas.add(new ChatMessage("小慕","你好，小慕为您服务", ChatMessage.Type.INCOMING, new Date()));
        mAdapter = new ChatMessageAdapter(this, mDatas);
        mMsgs.setAdapter(mAdapter);
    }

    private void initView()
    {
        mMsgs = (ListView) findViewById(R.id.id_listview_msgs);
        mInputMsg = (EditText) findViewById(R.id.id_input_msg);
        mSendMsg = (Button) findViewById(R.id.id_send_msg);
        getActionBar().setTitle("Tuling Robot");
//        iv = (ImageView) findViewById(R.id.iv_touxiang);
//        tv_myName = (TextView) findViewById(R.id.tv_my_name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_icon:

                break;
            case R.id.menu_name:
                Intent jumpToSetName = new Intent(MainActivity.this, SetNameActivity.class);
                startActivity(jumpToSetName);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    //修改头像功能对话框
//    private void ShowPickDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("设置头像...")
//                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//
//                        Intent intent = new Intent(Intent.ACTION_PICK, null);
//
//                        intent.setDataAndType(
//                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                                "image/*");
//                        startActivityForResult(intent, 1);
//
//                    }
//                })
//                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        dialog.dismiss();
//                        //调用快速拍照功能
//                        Intent intent = new Intent(
//                                MediaStore.ACTION_IMAGE_CAPTURE);
//                        //指定调用相机拍照后的照片存储的路径
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
//                                .fromFile(new File(Environment
//                                        .getExternalStorageDirectory(),
//                                        "xiaoma.jpg")));
//                        startActivityForResult(intent, 2);
//
//                    }
//                }).show();
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            // 如果是直接从相册获取
//            case 1:
//                if(data!=null) {
//                    startPhotoZoom(data.getData());
//                }
//                break;
//            // 如果是调用相机拍照时
//            case 2:
//                if(data!=null) {
//                    File temp = new File(Environment.getExternalStorageDirectory()
//                            + "/xiaoma.jpg");
//                    startPhotoZoom(Uri.fromFile(temp));
//                }
//                break;
//            // 取得裁剪后的图片
//            case 3:
//                /**
//                 * 非空判断大家一定要验证，如果不验证的话，
//                 * 在剪裁之后如果发现不满意，要重新裁剪，丢弃
//                 * 当前功能时，会报NullException
//                 */
//                if(data != null){
//                    setPicToView(data);
//                }
//                break;
//            default:
//                break;
//
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    //裁剪图片方法实现
//    public void startPhotoZoom(Uri uri) {
//		//安卓系统自带图片裁剪功能, 直接调本地库
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, 3);
//    }
//
//    //保存裁剪之后的图片数据
//    private void setPicToView(Intent picdata) {
//        Bundle extras = picdata.getExtras();
//        if (extras != null) {
//            Bitmap photo = extras.getParcelable("data");
//            Drawable drawable = new BitmapDrawable(photo);
//
//            iv.setBackgroundDrawable(drawable);
//        }
//    }

    public class ChatMessageAdapter extends BaseAdapter
    {
        private LayoutInflater mInflater;
        private List<ChatMessage> mDatas;

        public ChatMessageAdapter(Context context, List<ChatMessage> mDatas)
        {
            mInflater = LayoutInflater.from(context);
            this.mDatas = mDatas;
        }

        @Override
        public int getCount()
        {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position)
        {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public int getItemViewType(int position)
        {
            ChatMessage chatMessage = mDatas.get(position);
            if (chatMessage.getType() == ChatMessage.Type.INCOMING)
            {
                return 0;//robot
            }
            return 1;//me
        }

        @Override
        public int getViewTypeCount()
        {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ChatMessage chatMessage = mDatas.get(position);
            ViewHolder viewHolder = null;
            if (convertView == null)
            {
                // 通过ItemType设置不同的布局
                if (getItemViewType(position) == 0)//robot
                {
                    convertView = mInflater.inflate(R.layout.item_from_msg, parent,
                            false);
                    viewHolder = new ViewHolder();
                    viewHolder.mDate = (TextView) convertView
                            .findViewById(R.id.id_form_msg_date);
                    viewHolder.mMsg = (TextView) convertView
                            .findViewById(R.id.id_from_msg_info);

                } else	//me
                {
                    convertView = mInflater.inflate(R.layout.item_to_msg, parent,
                            false);
                    viewHolder = new ViewHolder();
                    TextView mytextname = (TextView) convertView.findViewById(R.id.tv_my_name);
                    SharedPreferences pref = getSharedPreferences("data",
                            MODE_PRIVATE);
                    String name = pref.getString("name", "");   //取出自定义的名字
                    mytextname.setText(name);
                    viewHolder.mDate = (TextView) convertView
                            .findViewById(R.id.id_to_msg_date);
                    viewHolder.mMsg = (TextView) convertView
                            .findViewById(R.id.id_to_msg_info);

                }
                convertView.setTag(viewHolder);
            } else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // 设置数据
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            viewHolder.mDate.setText(df.format(chatMessage.getDate()));
            viewHolder.mMsg.setText(chatMessage.getMsg());

            return convertView;
        }

        private final class ViewHolder
        {
            TextView mDate;
            TextView mMsg;
            TextView mName;
        }

    }

}
