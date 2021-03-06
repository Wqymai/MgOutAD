package com.mxOrkle.lmUdcf.model;

import java.io.Serializable;


public class AdModel implements Serializable {

    private String id;

    private String name;

    private String title;

    private String desc;

    private String image;

    private String pkName;

    private String category;

    private long size;

    private int type;                   //广告点击后续操作类型    1、apk下载     2、web网页  4、H5
    private String page;//H5代码（type=4,值有效）

    private String icon;

    private String url;

    private AdReport reportBean;

    private String apkFilePath;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    private int pt;                     //1: banner，2: 全屏， 3: 半屏， 4: 信息流，5：广告墙 ，6：push

    private int et;                     //广告过期时间，单位 秒，超过过期时间请重新获取广告。为0则该广告不能做缓存。

    private int displayTime;            //广告展示时间

    private int delayTime;              //插屏广告延迟展示时间

    private boolean hasJumpButton;      //是否有跳过按钮

    private int jumpFunction;           //点击跳过按钮对应的功能

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    private int toggleSence;            //广告的触发场景

    private int flag;       //点击广告是跳转还是关闭
    private String bp;//banner广告的位置


    private String sourceMark;
    private String deepLink;
    private String clickid;
    private String downx;
    private String downy;
    private String upx;
    private String upy;

    public String getClickid() {
        return clickid;
    }

    public void setClickid(String clickid) {
        this.clickid = clickid;
    }

    public String getDownx() {
        return downx;
    }

    public void setDownx(String downx) {
        this.downx = downx;
    }

    public String getDowny() {
        return downy;
    }

    public void setDowny(String downy) {
        this.downy = downy;
    }

    public String getUpx() {
        return upx;
    }

    public void setUpx(String upx) {
        this.upx = upx;
    }

    public String getUpy() {
        return upy;
    }

    public void setUpy(String upy) {
        this.upy = upy;
    }

    public String getSourceMark() {
        return sourceMark;
    }

    public void setSourceMark(String sourceMark) {
        this.sourceMark = sourceMark;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public int getToggleSence() {
        return toggleSence;
    }

    public void setToggleSence(int toggleSence) {
        this.toggleSence = toggleSence;
    }

    public int getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(int displayTime) {
        this.displayTime = displayTime;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public boolean isHasJumpButton() {
        return hasJumpButton;
    }

    public void setHasJumpButton(boolean hasJumpButton) {
        this.hasJumpButton = hasJumpButton;
    }

    public int getJumpFunction() {
        return jumpFunction;
    }

    public void setJumpFunction(int jumpFunction) {
        this.jumpFunction = jumpFunction;
    }

    public int getPt() {
        return pt;
    }

    public void setPt(int pt) {
        this.pt = pt;
    }

    public int getEt() {
        return et;
    }

    public void setEt(int et) {
        this.et = et;
    }

    public String getApkFilePath() {
        return apkFilePath;
    }

    public void setApkFilePath(String apkFilePath) {
        this.apkFilePath = apkFilePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AdReport getReportBean() {
        return reportBean;
    }

    public void setReportBean(AdReport reportBean) {
        this.reportBean = reportBean;
    }

//    @Override
//    public String toString() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("\n name="+name+"\n");
//        sb.append("title="+title+"\n");
//        sb.append("icon="+icon+"\n");
//        sb.append("img="+image+"\n");
//        sb.append("url="+url + "\n");
//        sb.append("pt="+pt+"\n");
//        sb.append("et="+et+"\n");
//        sb.append("displayTime="+displayTime+"\n");
//        sb.append("delayTime="+delayTime+"\n");
//        sb.append("hasJumpButton="+hasJumpButton+"\n");
//        sb.append("jumpFunction="+jumpFunction+"\n");
//        sb.append("type="+type+"\n");
//        sb.append("flag="+flag);
//
//        return sb.toString();
//    }



}
