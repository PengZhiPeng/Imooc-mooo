package com.imooc.mooo.test;

import android.test.AndroidTestCase;
import android.util.Log;

import com.imooc.mooo.utils.HttpUtils;

/**
 * Created by acer on 2015/8/19.
 */
public class TestHttpUtils extends AndroidTestCase{
    public void testSendInfo()
    {
        String res = HttpUtils.doGet("���ҽ���Ц��");
        Log.e("TAG", res);
        res = HttpUtils.doGet("���ҽ��������");
        Log.e("TAG", res);
        res = HttpUtils.doGet("���");
        Log.e("TAG", res);
        res = HttpUtils.doGet("������");
        Log.e("TAG", res);
    }
}
