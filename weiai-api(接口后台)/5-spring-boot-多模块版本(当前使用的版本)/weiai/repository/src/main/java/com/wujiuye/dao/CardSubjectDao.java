package com.wujiuye.dao;

import com.wujiuye.pojo.CardSubject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wujiuye
 * @version 1.0 on 2019/5/11 {描述：}
 */
public interface CardSubjectDao {

    Integer insertOne(CardSubject subject);

    Integer deleteOne(@Param("loveId") Integer loveId, @Param("subject") String subject);

    List<CardSubject> getAllSubject(@Param("loveId") Integer loverId);

    CardSubject findOne(@Param("loveId") Integer loverId, @Param("subject") String subject);
}
