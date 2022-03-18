package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @author xuweijie
 * @create 2022-03-16 19:39
 */
@Repository
public class AlphaDaoHibernateImpl implements AlphaDao {
    @Override
    public String select() {
        return "Hibernate";
    }
}
