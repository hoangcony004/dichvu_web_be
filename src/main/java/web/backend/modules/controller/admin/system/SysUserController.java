package web.backend.modules.controller.admin.system;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import web.backend.core.bases.BaseController;
import web.backend.core.customs.Constants;
import web.backend.core.customs.pages.PagedResult;
import web.backend.core.customs.pages.UserPageModelCustom;
import web.backend.core.customs.responses.ApiResponseCustom;
import web.backend.core.entitys.systems.SysUser;
import web.backend.modules.service.admin.system.sys_user.SysUserService;

@RestController
@RequestMapping("/api/private/users")
public class SysUserController extends BaseController<SysUser, Long> {

    @Autowired
    private SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        super(sysUserService);
    }

    @PostMapping("/search-paging-custom")
    @Operation(summary = "Lấy danh sách SysUser có phân trang + tìm kiếm custom")
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
    public ResponseEntity<ApiResponseCustom<PagedResult<SysUser>>> getAll(
            @RequestBody UserPageModelCustom pageModelCustom) {
        try {
            int currentPage = pageModelCustom.getCurrentPage();
            int pageSize = pageModelCustom.getPageSize();
            String strKey = pageModelCustom.getStrKey();
            String role = pageModelCustom.getRole();

            Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

            Page<SysUser> result = sysUserService.findAllCustom(pageable, strKey, role);

            if (result == null || result.isEmpty()) {
                ApiResponseCustom<PagedResult<SysUser>> response = new ApiResponseCustom<>(
                        ApiResponseCustom.Status.ERROR,
                        "Không tìm thấy dữ liệu phù hợp!",
                        Constants.ApiCode.NOT_FOUND,
                        new PagedResult<>(
                                currentPage,
                                0,
                                0,
                                pageSize,
                                strKey,
                                Collections.emptyList()));
                return ResponseEntity.ok(response);
            }

            PagedResult<SysUser> pagedData = new PagedResult<>(
                    result.getNumber() + 1,
                    result.getTotalPages(),
                    result.getTotalElements(),
                    result.getSize(),
                    strKey,
                    result.getContent());

            ApiResponseCustom<PagedResult<SysUser>> response = new ApiResponseCustom<>(
                    ApiResponseCustom.Status.SUCCESS,
                    "Thành công!",
                    Constants.ApiCode.OK,
                    pagedData);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponseCustom<PagedResult<SysUser>> response = new ApiResponseCustom<>(
                    ApiResponseCustom.Status.ERROR,
                    "Lỗi: " + e.getMessage(),
                    Constants.ApiCode.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
