package com.mg.outfire.task;


import com.mg.outfire.model.DeviceInfo;

public interface IDeviceInfoListener {
    /*
    * 初始化用户信息完成
    * */
    void deviceInfoLoaded(DeviceInfo deviceInfo);
}
