package com.ctg.mes.config.common.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @ClassName: BasePage
 * @Description: 基础分页
 */
public class BasePage<E> extends PageRequest<E> implements java.io.Serializable {
    protected static final Logger LOG = LoggerFactory.getLogger(BasePage.class);
    private static final long serialVersionUID = -360860474471966681L;
    public static final int DEFAULT_PAGE_SIZE = 10;

    static {
        LOG.debug("BaseQuery.DEFAULT_PAGE_SIZE=" + DEFAULT_PAGE_SIZE);
    }

    private int posStart = 0;
    /**
     *
     */
    private int id = 0;
    private int linkResMenuId = 0;

    private int rows = DEFAULT_PAGE_SIZE;

    public BasePage() {
        setPageSize(DEFAULT_PAGE_SIZE);
    }

    /**
     * 整理page
     *
     * @param entity
     */
    public void tidyPage(E entity) {
        this.setPageNumber(this.getPosStart() / this.getPageSize() + 1);
        this.setFilters(entity);
    }

    public int getPosStart() {
        return posStart;
    }

    /**
     * 当前起始位置
     *
     * @param posStart
     */
    public void setPosStart(int posStart) {
        this.posStart = posStart;
    }

    public int getId() {
        return id;
    }

    /**
     * id（多用）
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    public int getLinkResMenuId() {
        return linkResMenuId;
    }

    public void setLinkResMenuId(int linkResMenuId) {
        this.linkResMenuId = linkResMenuId;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
        this.setPageSize(rows);
        this.setPosStart((this.getPageNumber() - 1) * rows);
    }

}
