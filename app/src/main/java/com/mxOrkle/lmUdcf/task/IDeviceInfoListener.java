package com.mxOrkle.lmUdcf.task;


import com.mxOrkle.lmUdcf.model.DeviceInfo;

public interface IDeviceInfoListener {
    /*
    * 初始化用户信息完成
    * */
    void deviceInfoLoaded(DeviceInfo deviceInfo);
}
