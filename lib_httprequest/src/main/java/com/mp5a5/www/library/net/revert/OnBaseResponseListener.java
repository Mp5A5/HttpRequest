package com.mp5a5.www.library.net.revert;

/**
 * @author ：mp5a5 on 2018/11/28 16：30
 * @describe：网络返回参数回调
 * @email：wwb199055@126.com
 */
public interface OnBaseResponseListener<R extends BaseResponseEntity> {

  /**
   * 成功
   *
   * @param response 成功参数
   */
  void onSuccess(R response);

  /**
   * 失败
   *
   * @param response 失败参数
   */
  default void onFailing(R response) {
  }

  /**
   * 错误
   */
  default void onError() {
  }
}
