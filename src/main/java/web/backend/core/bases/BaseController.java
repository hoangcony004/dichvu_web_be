package web.backend.core.bases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import web.backend.core.customs.Constants;
import web.backend.core.customs.pages.PageModel;
import web.backend.core.customs.pages.PagedResult;
import web.backend.core.customs.responses.ApiResponse;

import java.util.List;
import java.util.Map;
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
            ApiResponse<Object> response = new ApiResponse<>(ApiResponse.Status.SUCCESS, "Thêm mới thành công!",
                    Constants.ApiCode.CREATED,
                    created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(),
                    Constants.ApiCode.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable ID id, @RequestBody T entity) {
        try {
            T updated = service.update(id, entity);
            ApiResponse<Object> response = new ApiResponse<>(ApiResponse.Status.SUCCESS, "Chỉnh sửa thành công!",
                    Constants.ApiCode.OK,
                    updated);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(),
                    Constants.ApiCode.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable ID id) {
        try {
            service.delete(id);
            ApiResponse<Object> response = new ApiResponse<>(ApiResponse.Status.SUCCESS, "Xóa thành công!",
                    Constants.ApiCode.OK);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(),
                    Constants.ApiCode.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable ID id) {
        try {
            Optional<T> entity = service.findById(id);
            if (entity.isPresent()) {
                ApiResponse<Object> response = new ApiResponse<>(ApiResponse.Status.SUCCESS,
                        "Lấy thông tin thành công!", Constants.ApiCode.OK,
                        entity.get());
                return ResponseEntity.ok(response);
            } else {
                ApiResponse response = new ApiResponse(ApiResponse.Status.ERROR, "Không tìm thấy bản ghi!", Constants.ApiCode.NOT_FOUND);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(),
                    Constants.ApiCode.INTERNAL_SERVER_ERROR);
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
                    ApiResponse.Status.SUCCESS, "Thành công!", Constants.ApiCode.OK, pagedData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<PagedResult<T>> response = new ApiResponse<>(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(),
                    Constants.ApiCode.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }

    @PostMapping("/get-data-by-column")
    public ResponseEntity<ApiResponse> getDataByColumn(@RequestBody Map<String, String> request) {
        try {
            String columnName = request.get("columnName");
            Map<String, List<Object>> data = service.getDataByColumn(columnName);

            ApiResponse<Object> response = new ApiResponse<>(
                    ApiResponse.Status.SUCCESS,
                    "Lấy dữ liệu cột '" + columnName + "' thành công!",
                    Constants.ApiCode.OK,
                    data);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(
                    ApiResponse.Status.ERROR,
                    "Lỗi: " + e.getMessage(),
                    Constants.ApiCode.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}