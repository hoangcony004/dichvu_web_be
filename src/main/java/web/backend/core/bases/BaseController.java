package web.backend.core.bases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import web.backend.core.customs.pages.PageModel;
import web.backend.core.customs.pages.PagedResult;
import web.backend.core.customs.responses.ApiResponse;

import java.util.Optional;

public abstract class BaseController<T, ID> {

    protected final BaseService<T, ID> service;

    public BaseController(BaseService<T, ID> service) {
        this.service = service;
    }

    @PostMapping("/insert")
    public ResponseEntity<ApiResponse> create(@RequestBody T entity) {
        try {
            T created = service.create(entity);
            ApiResponse response = new ApiResponse(ApiResponse.Status.SUCCESS, "Thêm mới thành công!", 200, created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable ID id, @RequestBody T entity) {
        try {
            T updated = service.update(id, entity);
            ApiResponse response = new ApiResponse(ApiResponse.Status.SUCCESS, "Chỉnh sửa thành công!", 200, updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable ID id) {
        try {
            service.delete(id);
            ApiResponse response = new ApiResponse(ApiResponse.Status.SUCCESS, "Xóa thành công!", 200, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable ID id) {
        try {
            Optional<T> entity = service.findById(id);
            if (entity.isPresent()) {
                ApiResponse response = new ApiResponse(ApiResponse.Status.SUCCESS, "Lấy thông tin thành công!", 200,
                        entity.get());
                return ResponseEntity.ok(response);
            } else {
                ApiResponse response = new ApiResponse(ApiResponse.Status.ERROR, "Không tìm thấy bản ghi!", 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/search-paging")
    public ResponseEntity<ApiResponse<PagedResult<T>>> getAll(@RequestBody PageModel pageModel) {
        try {
            int currentPage = pageModel.getCurrentPage();
            int pageSize = pageModel.getPageSize();
            String strKey = pageModel.getStrKey();

            Page<T> result = service.findAll(PageRequest.of(currentPage, pageSize), strKey);

            PagedResult<T> pagedData = new PagedResult<>(
                    result.getNumber() + 1,
                    result.getTotalPages(),
                    result.getTotalElements(),
                    result.getSize(),
                    strKey,
                    result.getContent());

            ApiResponse<PagedResult<T>> response = new ApiResponse<>(
                    ApiResponse.Status.SUCCESS, "Thành công!", 200, pagedData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}