package com.xw.service;

import com.xw.entity.Goods;
import com.xw.entity.PageBean;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/5/20
 */
public interface GoodsService extends BaseService<Goods> {

    /**
     * 分页查询
     * @param goods 查询条件
     * @param pageCode  当前页
     * @param pageSize  每页的记录数
     * @return
     */
    PageBean findByPage(Goods goods, int pageCode, int pageSize);
}
