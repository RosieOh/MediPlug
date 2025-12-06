package com.emrsystem.domain.department.controller;

import com.emrsystem.domain.department.facade.DepartmentFacade;
import com.emrsystem.domain.department.request.DepartmentRequests.CreateDepartmentRequest;
import com.emrsystem.domain.department.request.DepartmentRequests.UpdateDepartmentRequest;
import com.emrsystem.domain.department.response.DepartmentResponses.DepartmentSummary;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.common.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController extends BaseController {

    private final DepartmentFacade departmentFacade;

    @GetMapping
    @Operation(summary = "진료과 목록 조회", description = "페이지네이션 및 병원/이름 필터를 지원합니다.")
    public ResponseEntity<ApiResponse<PageResponse<DepartmentSummary>>> page(@PageableDefault(size = 20) @Parameter(description = "페이지/정렬 파라미터") Pageable pageable,
                                                                             @RequestParam(required = false) @Parameter(description = "병원 ID", example = "1") Long hospitalId,
                                                                             @RequestParam(required = false) @Parameter(description = "진료과명 부분검색", example = "내과") String name
    ) {
        PageResponse<DepartmentSummary> response = (hospitalId == null && (name == null || name.isBlank()))
                ? departmentFacade.page(pageable)
                : departmentFacade.search(hospitalId, name, pageable);
        return ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentSummary>> get(@PathVariable Long id) {
        return ok(departmentFacade.get(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentSummary>> create(@Valid @RequestBody CreateDepartmentRequest request) {
        return created(departmentFacade.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentSummary>> update(@PathVariable Long id,
                                                                 @Valid @RequestBody UpdateDepartmentRequest request) {
        return ok(departmentFacade.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        departmentFacade.delete(id);
        return okMessage("deleted");
    }
}


