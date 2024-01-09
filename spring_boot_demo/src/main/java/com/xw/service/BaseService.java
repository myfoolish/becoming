package com.xw.service;

import java.util.List;

/**
 * @author liuxiaowei
 * @description 通用 Service 接口
 * @date 2021/5/20
 */
public interface BaseService<T> {

    /**
     * 添加
     * @param t
     */
    void add(T t);

    /**
     * 删除（批量）
     * @param ids
     */
    void delete(Long... ids);

    /**
     * 修改
     * @param t
     */
    void update(T t);

    /**
     * 查询所有
     * @return
     */
    List<T> findAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    List<T> findById(Long id);
}
