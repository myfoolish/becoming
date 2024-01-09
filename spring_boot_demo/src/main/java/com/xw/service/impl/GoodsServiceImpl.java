package com.xw.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xw.entity.Goods;
import com.xw.entity.PageBean;
import com.xw.mapper.GoodsMapper;
import com.xw.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/5/20
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public void add(Goods goods) {
        goodsMapper.add(goods);
    }

    @Override
    public void delete(Long... ids) {
        for (Long id : ids) {
            goodsMapper.delete(id);
        }
    }

    @Override
    public void update(Goods goods) {
        goodsMapper.update(goods);
    }

    @Override
    public List<Goods> findAll() {
        return goodsMapper.findAll();
    }

    @Override
    public List<Goods> findById(Long id) {
        return goodsMapper.findById(id);
    }

    /**
     * 分页查询-条件查询方法
     * @param goods 查询条件
     * @param pageCode  当前页
     * @param pageSize  每页的记录数
     * @return
     */
    @Override
    public PageBean findByPage(Goods goods, int pageCode, int pageSize) {
        //使用Mybatis分页插件
        PageHelper.startPage(pageCode, pageSize);
        //调用分页查询方法，其实就是查询所有数据，mybatis自动帮我们进行分页计算
        Page<Goods> page = goodsMapper.findByPage(goods);
        return new PageBean(page.getTotal(), page.getResult());
    }
}
