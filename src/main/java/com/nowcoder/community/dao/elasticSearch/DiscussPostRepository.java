package com.nowcoder.community.dao.elasticSearch;

import com.nowcoder.community.entity.DiscussPost;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author xuweijie
 * @create 2022-03-28 10:25
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost,Integer> {
}
