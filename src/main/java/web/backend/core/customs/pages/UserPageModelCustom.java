package web.backend.core.customs.pages;

public class UserPageModelCustom {

    private int currentPage;
    private int pageSize;
    private String strKey;
    private String role;

    public UserPageModelCustom() {
    }

    public UserPageModelCustom(int currentPage, int pageSize, String strKey, String role) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.strKey = strKey;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
