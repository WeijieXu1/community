package com.nowcoder.community.util;

/**
 * @author xuweijie
 * @create 2022-03-24 19:24
 */
public class RedisKeyUtil {
    private static final String SPLIT=":";
    private static final String PREFIX_ENTITY_LIKE="like:entity";

    //某个实体的赞
    //like:entity:entityType:entityId->set(userId)
    public static String getEntityLikeKey(int entityType,int entityId){
        return PREFIX_ENTITY_LIKE+SPLIT+entityType+SPLIT+entityId;
    }

}
