package com.ctg.mes.config.common.page;

import java.io.Serializable;

/**
 * @author henry
 */
public class PageQuery implements Serializable {
    private static final long serialVersionUID = -8000900575354501298L;
    public static final int DEFAULT_PAGE_SIZE = 10;
    private int page;
    private int pageSize = 10;

    public PageQuery() {
    }

    public PageQuery(int pageSize) {
        this.pageSize = pageSize;
    }

    public PageQuery(PageQuery query) {
        this.page = query.page;
        this.pageSize = query.pageSize;
    }

    public PageQuery(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override public String toString() {
        return "page:" + this.page + ",pageSize:" + this.pageSize;
    }
}