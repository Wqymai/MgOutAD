package com.mgoutad.task;


import com.mgoutad.model.DeviceInfo;

public interface IDeviceInfoListener {
    /*
    * 初始化用户信息完成
    * */
    void deviceInfoLoaded(DeviceInfo deviceInfo);
}
