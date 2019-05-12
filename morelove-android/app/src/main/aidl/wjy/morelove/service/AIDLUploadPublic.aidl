package wjy.morelove.service;

//上传进度改变通知
interface AIDLUploadPublic {

    //返回process，单位为百分之几，值范围0.00～1.00
    float getUploadProcess();

}
