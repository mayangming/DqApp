package com.dq.im.third_system;

/**
 * 注册成功的
 */
public interface ThirdSystemRegisterResultIml {
    /**
     * @param thirdType 厂商类型
     * @param regId 注册Id
     */
    void registerResult(String thirdType,String regId);

}