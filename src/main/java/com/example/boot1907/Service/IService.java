package com.example.boot1907.Service;

import com.github.pagehelper.PageInfo;

public interface IService<T> {
    T findOne(T pojo);
    void save(T pojo);
    void deleteOne(T pojo);
    void updateOne(T pojo);
    PageInfo<T> pages(T pojo, int pageStart, int pageSize);
}