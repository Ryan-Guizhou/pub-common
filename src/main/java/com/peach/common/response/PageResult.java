package com.peach.common.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 04 3月 2025 21:22
 */
@Data
public class PageResult<T>{

    private Long total;

    private List<T> result = new ArrayList<T>();

    private Long topTotal;

    public PageResult() {

    }

    public PageResult<T> setTopTotal(Long topTotal){
        this.topTotal = topTotal;
        return this;
    }

    public PageResult<T> setTotal(Long total){
        this.total = total;
        return this;
    }

    public PageResult<T> setResult(List<T> result){
        this.result = result;
        return this;
    }
}
