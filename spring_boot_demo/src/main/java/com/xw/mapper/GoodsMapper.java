package com.xw.mapper;

import com.github.pagehelper.Page;
import com.xw.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/5/20
 */
@Mapper
public interface GoodsMapper {

    void add(Goods goods);

    void delete(Long id);

    void update(Goods goods);

    List<Goods> findAll();

    List<Goods> findById(Long id);

    Page<Goods> findByPage(Goods goods);
}
