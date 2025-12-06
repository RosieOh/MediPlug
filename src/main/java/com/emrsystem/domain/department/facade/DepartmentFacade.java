package com.emrsystem.domain.department.facade;

import com.emrsystem.domain.department.entity.Department;
import com.emrsystem.domain.department.request.DepartmentRequests.CreateDepartmentRequest;
import com.emrsystem.domain.department.request.DepartmentRequests.UpdateDepartmentRequest;
import com.emrsystem.domain.department.response.DepartmentResponses.DepartmentSummary;
import com.emrsystem.domain.department.service.DepartmentService;
import com.emrsystem.global.common.dto.PageResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentFacade {

    private final DepartmentService departmentService;

    public PageResponse<DepartmentSummary> page(Pageable pageable) {
        Page<com.emrsystem.domain.department.entity.Department> page = departmentService.page(pageable);
        return PageResponse.of(page.map(DepartmentSummary::from));
    }

    public PageResponse<DepartmentSummary> search(Long hospitalId, String name, Pageable pageable) {
        Page<com.emrsystem.domain.department.entity.Department> page = departmentService.search(hospitalId, name, pageable);
        return PageResponse.of(page.map(DepartmentSummary::from));
    }

    public DepartmentSummary get(Long id) {
        Department department = departmentService.get(id);
        return DepartmentSummary.from(department);
    }

    public DepartmentSummary create(CreateDepartmentRequest request) {
        Department department = departmentService.create(request);
        return DepartmentSummary.from(department);
    }

    public DepartmentSummary update(Long id, UpdateDepartmentRequest request) {
        Department department = departmentService.update(id, request);
        return DepartmentSummary.from(department);
    }

    public void delete(Long id) {
        departmentService.delete(id);
    }
}


