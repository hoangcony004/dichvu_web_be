package web.backend.core.bases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import web.backend.core.customs.Constants;
import web.backend.core.customs.pages.PageModel;
import web.backend.core.customs.pages.PagedResult;
import web.backend.core.customs.responses.ApiResponseCustom;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class BaseController<T, ID> {

    protected final BaseService<T, ID> service;

    public BaseController(BaseService<T, ID> service) {
        this.service = service;
    }

    @PostMapping("/insert")
    @Operation(summary = "Tạo mới")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tạo mới thành công", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseCustom.class), examples = @ExampleObject(value = """
                        {
                            "status": "SUCCESS",
                            "message": "Thêm mới thành công!",
                            "code": 201,
                            "data": { }
                        }
                    """))),
            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseCustom.class), examples = @ExampleObject(value = """
                        {
                            "status": "ERROR",
                            "message": "Lỗi máy chủ!",
                            "code": 500
                        }
                    """)))
    })
    public ResponseEntity<ApiResponseCustom> create(@RequestBody T entity) {
        try {
            T created = service.create(entity);
            ApiResponseCustom<Object> response = new ApiResponseCustom<>(ApiResponseCustom.Status.SUCCESS,
                    "Thêm mới thành công!",
                    Constants.ApiCode.CREATED,
                    created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponseCustom response = new ApiResponseCustom(ApiResponseCustom.Status.ERROR, "Lỗi: " + e.getMessage(),
                    Constants.ApiCode.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Chỉnh sửa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chỉnh sửa thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseCustom.class), examples = @ExampleObject(value = """
                        {
                            "status": "ERROR",
                            "message": "Lỗi máy chủ!",
                            "code": 500
                        }
                    """)))
    })
    public ResponseEntity<ApiResponseCustom> update(@PathVariable ID id, @RequestBody T entity) {
        try {
            T updated = service.update(id, entity);
            ApiResponseCustom<Object> response = new ApiResponseCustom<>(ApiResponseCustom.Status.SUCCESS,
                    "Chỉnh sửa thành công!",
                    Constants.ApiCode.OK,
                    updated);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ApiResponseCustom response = new ApiResponseCustom(ApiResponseCustom.Status.ERROR, "Lỗi: " + e.getMessage(),
                    Constants.ApiCode.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Xóa bản ghi")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseCustom.class), examples = @ExampleObject(value = """
                        {
                            "status": "ERROR",
                            "message": "Lỗi máy chủ!",
                            "code": 500
                        }
                    """)))
    })
    public ResponseEntity<ApiResponseCustom> delete(@PathVariable ID id) {
        try {
            service.delete(id);
            ApiResponseCustom<Object> response = new ApiResponseCustom<>(ApiResponseCustom.Status.SUCCESS,
                    "Xóa thành công!",
                    Constants.ApiCode.OK);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponseCustom response = new ApiResponseCustom(ApiResponseCustom.Status.ERROR, "Lỗi: " + e.getMessage(),
                    Constants.ApiCode.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/getOne/{id}")
    @Operation(summary = "Lấy thông tin theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseCustom.class), examples = @ExampleObject(value = """
                        {
                            "status": "ERROR",
                            "message": "Lỗi máy chủ!",
                            "code": 500
                        }
                    """)))
    })
    public ResponseEntity<ApiResponseCustom> getById(@PathVariable ID id) {
        try {
            Optional<T> entity = service.findById(id);
            if (entity.isPresent()) {
                ApiResponseCustom<Object> response = new ApiResponseCustom<>(ApiResponseCustom.Status.SUCCESS,
                        "Lấy thông tin thành công!", Constants.ApiCode.OK,
                        entity.get());
                return ResponseEntity.ok(response);
            } else {
                ApiResponseCustom response = new ApiResponseCustom(ApiResponseCustom.Status.ERROR,
                        "Không tìm thấy bản ghi!",
                        Constants.ApiCode.NOT_FOUND);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            ApiResponseCustom response = new ApiResponseCustom(ApiResponseCustom.Status.ERROR, "Lỗi: " + e.getMessage(),
                    Constants.ApiCode.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/search-paging")
    @Operation(summary = "Lấy danh sách có phân trang")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseCustom.class), examples = @ExampleObject(value = """
                        {
                            "status": "ERROR",
                            "message": "Lỗi máy chủ!",
                            "code": 500
                        }
                    """)))
    })
    public ResponseEntity<ApiResponseCustom<PagedResult<T>>> getAll(@RequestBody PageModel pageModel) {
        try {
            int currentPage = pageModel.getCurrentPage();
            int pageSize = pageModel.getPageSize();
            String strKey = pageModel.getStrKey();

            // tôi mới thêm cái này sửa lại cho đúng
            Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

            Page<T> result = service.findAll(pageable, strKey);

            PagedResult<T> pagedData = new PagedResult<>(
                    result.getNumber() + 1,
                    result.getTotalPages(),
                    result.getTotalElements(),
                    result.getSize(),
                    strKey,
                    result.getContent());

            ApiResponseCustom<PagedResult<T>> response = new ApiResponseCustom<>(
                    ApiResponseCustom.Status.SUCCESS, "Thành công!", Constants.ApiCode.OK, pagedData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponseCustom<PagedResult<T>> response = new ApiResponseCustom<>(ApiResponseCustom.Status.ERROR,
                    "Lỗi: " + e.getMessage(),
                    Constants.ApiCode.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }

    @PostMapping("/get-data-by-column")
    @Operation(summary = "Lấy dữ liệu theo cột")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy dữ liệu thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi server", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseCustom.class), examples = @ExampleObject(value = """
                        {
                            "status": "ERROR",
                            "message": "Lỗi máy chủ!",
                            "code": 500
                        }
                    """)))
    })
    public ResponseEntity<ApiResponseCustom> getDataByColumn(@RequestBody Map<String, String> request) {
        try {
            String columnName = request.get("columnName");
            Map<String, List<Object>> data = service.getDataByColumn(columnName);

            ApiResponseCustom<Object> response = new ApiResponseCustom<>(
                    ApiResponseCustom.Status.SUCCESS,
                    "Lấy dữ liệu cột '" + columnName + "' thành công!",
                    Constants.ApiCode.OK,
                    data);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponseCustom response = new ApiResponseCustom(
                    ApiResponseCustom.Status.ERROR,
                    "Lỗi: " + e.getMessage(),
                    Constants.ApiCode.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}