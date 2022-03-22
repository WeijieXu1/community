package com.nowcoder.community.util;

/**
 * @author xuweijie
 * @create 2022-03-19 15:11
 */
public interface CommunityConstant {
    //激活成功
    int ACTIVATION_SUCCESS=0;

    //重复激活
    int ACTIVATION_REPEAT=1;

    //激活失败
    int ACTIVATION_FAILURE=2;

    //默认状态登陆凭证超时时间
    int DEFAULT_EXPIRED_SECONDS=3600*12;

    //记住我的情况下凭证超时时间
    int REMEMBER_EXPIRED_SECONDS=3600*24*100;

    //实体类型：帖子
    int ENTITY_TYPE_POST=1;
    //评论
    int ENTITY_TYPE_COMMENT=2;

}
