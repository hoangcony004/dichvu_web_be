package web.backend.core.customs.pages;

public class PageModel {

    private int currentPage;
    private int pageSize;
    private String strKey;

    public PageModel() {
    }

    public PageModel(int currentPage, int pageSize, String strKey) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.strKey = strKey;
    }

    // Getters and Setters
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

    public String getStrKey() {
        return strKey;
    }

    public void setStrKey(String strKey) {
        this.strKey = strKey;
    }
}
