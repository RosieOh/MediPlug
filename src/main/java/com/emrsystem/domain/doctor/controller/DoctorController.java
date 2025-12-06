package com.emrsystem.domain.doctor.controller;

import com.emrsystem.domain.doctor.facade.DoctorFacade;
import com.emrsystem.domain.doctor.request.DoctorRequests.CreateDoctorRequest;
import com.emrsystem.domain.doctor.request.DoctorRequests.UpdateDoctorRequest;
import com.emrsystem.domain.doctor.response.DoctorResponses.DoctorSummary;
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
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController extends BaseController {

    private final DoctorFacade doctorFacade;

    @GetMapping
    @Operation(summary = "의사 목록 조회", description = "페이지네이션 및 부서/이름 필터를 지원합니다.")
    public ResponseEntity<ApiResponse<PageResponse<DoctorSummary>>> page(@PageableDefault(size = 20) @Parameter(description = "페이지/정렬 파라미터") Pageable pageable,
                                                                         @RequestParam(required = false) @Parameter(description = "부서 ID", example = "3") Long departmentId,
                                                                         @RequestParam(required = false) @Parameter(description = "의사명 부분검색", example = "kim") String name
    ) {
        PageResponse<DoctorSummary> response = (departmentId == null && (name == null || name.isBlank()))
                ? doctorFacade.page(pageable)
                : doctorFacade.search(departmentId, name, pageable);
        return ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorSummary>> get(@PathVariable Long id) {
        return ok(doctorFacade.get(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorSummary>> create(@Valid @RequestBody CreateDoctorRequest request) {
        return created(doctorFacade.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorSummary>> update(@PathVariable Long id,
                                                             @Valid @RequestBody UpdateDoctorRequest request) {
        return ok(doctorFacade.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        doctorFacade.delete(id);
        return okMessage("deleted");
    }
}


