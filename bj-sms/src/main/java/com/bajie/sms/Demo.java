package com.bajie.sms;

import com.aliyuncs.exceptions.ClientException;
import com.bajie.sms.utils.SmsUtils;

public class Demo {
    public static void main(String[] args) throws ClientException {
        SmsUtils smsUtils = new SmsUtils();
        smsUtils.sendSms("18234080013", "123654", "个人使用", "SMS_210076718");

    }
}
