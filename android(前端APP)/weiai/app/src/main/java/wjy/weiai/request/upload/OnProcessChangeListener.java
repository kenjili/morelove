package wjy.weiai.request.upload;


/**
 * 进度监听器接口
 */
public interface OnProcessChangeListener {

    /**
     * 进度改变
     * @param bytes 已传输的字节
     */
    void onProcessChange(long bytes);

}
