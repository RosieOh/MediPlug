package com.emrsystem.domain.hospital.controller;

import com.emrsystem.domain.hospital.facade.HospitalFacade;
import com.emrsystem.domain.hospital.request.HospitalRequests.CreateHospitalRequest;
import com.emrsystem.domain.hospital.request.HospitalRequests.UpdateHospitalRequest;
import com.emrsystem.domain.hospital.response.HospitalResponses.HospitalSummary;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.common.dto.PageResponse;
import jakarta.validation.Valid;
import java.util.List;
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
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController extends BaseController {

    private final HospitalFacade hospitalFacade;

    @GetMapping
    @Operation(summary = "병원 목록 조회", description = "페이지네이션 및 이름 부분검색을 지원합니다.")
    public ResponseEntity<ApiResponse<PageResponse<HospitalSummary>>> page(@PageableDefault(size = 20) @Parameter(description = "페이지/정렬 파라미터") Pageable pageable,
                                                                           @RequestParam(required = false)
                                                                           @Parameter(description = "병원명 부분검색", example = "Seoul") String name
    ) {
        PageResponse<HospitalSummary> response = (name == null || name.isBlank())
                ? hospitalFacade.page(pageable)
                : hospitalFacade.search(name, pageable);
        return ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HospitalSummary>> get(@PathVariable Long id) {
        return ok(hospitalFacade.get(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HospitalSummary>> create(@Valid @RequestBody CreateHospitalRequest request) {
        return created(hospitalFacade.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HospitalSummary>> update(@PathVariable Long id,
                                                               @Valid @RequestBody UpdateHospitalRequest request) {
        return ok(hospitalFacade.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        hospitalFacade.delete(id);
        return okMessage("deleted");
    }
}


