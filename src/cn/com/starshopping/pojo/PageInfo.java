package cn.com.starshopping.pojo;

import java.util.List;

public class PageInfo<T> {
    private List<T> list;    //保存页面数据
    private int totalNum;    //查询到的总记录数
    private int currentPage;       //用户当前看的页数
    private int pageSize;        //每页多少条显示数据
    private int totalPages;        //总页数
    private int previousPage;    //上一页
    private int nextPage;        //下一页

    @Override
    public String toString() {
        return "PageInfo{" +
                "list=" + list +
                ", totalNum=" + totalNum +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", totalPages=" + totalPages +
                ", previousPage=" + previousPage +
                ", nextPage=" + nextPage +
                '}';
    }

    public PageInfo(int currentPage, int pageSize,int totalNum) {
        this.currentPage=currentPage;
        this.pageSize=pageSize;
        this.totalNum=totalNum;
    }


    public static PageInfo startPage(int currentPage,int pageSize,int totalNum){
        return  new PageInfo(currentPage,pageSize,totalNum);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(int previousPage) {
        this.previousPage = previousPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
}
