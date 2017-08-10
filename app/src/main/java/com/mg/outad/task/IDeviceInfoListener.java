package com.mg.outad.task;


import com.mg.outad.model.DeviceInfo;

public interface IDeviceInfoListener {
    /*
    * 初始化用户信息完成
    * */
    void deviceInfoLoaded(DeviceInfo deviceInfo);
}
