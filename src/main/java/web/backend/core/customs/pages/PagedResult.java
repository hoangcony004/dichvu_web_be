package web.backend.core.customs.pages;

import java.util.List;

public class PagedResult<T> {
    /**
     * Kết quả phân trang
     *
     * @param <T> Kiểu dữ liệu của các phần tử trong trang
     */
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private String strKey;
    private List<T> data;

    public PagedResult(int currentPage, int totalPages, long totalElements, int pageSize, String strKey, List<T> data) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageSize = pageSize;
        this.strKey = strKey;
        this.data = data;
    }

    // Getters và Setters
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getStrKey() {
        return strKey;
    }

    public void setStrKey(String strKey) {
        this.strKey = strKey;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
