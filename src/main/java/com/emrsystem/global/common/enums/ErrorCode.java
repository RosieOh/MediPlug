package com.emrsystem.global.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 400(Bad Request)
     *   이 응답은 잘못된 문법으로 인하여 서버가 요청을 이해할 수 없음을 의미합니다.
     * */
    WRONG_ENTRY_POINT(40000, HttpStatus.BAD_REQUEST, "잘못된 접근입니다."),
    MISSING_REQUEST_PARAMETER(40001, HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
    INVALID_PARAMETER_FORMAT(40002, HttpStatus.BAD_REQUEST, "요청에 유효하지 않은 인자 형식입니다."),
    BAD_REQUEST_JSON(40003, HttpStatus.BAD_REQUEST, "잘못된 JSON 형식입니다."),
    DATA_INTEGRITY_VIOLATION(40004, HttpStatus.BAD_REQUEST, "데이터 무결성 위반입니다. 필수 값이 누락되었거나 유효하지 않습니다."),
    APPOINTMENT_TIME_OVERLAP(40005, HttpStatus.BAD_REQUEST, "해당 시간에 이미 예약이 존재합니다."),
    ROOM_NOT_FOUND(40405, HttpStatus.NOT_FOUND, "해당 진료실을 찾을 수 없습니다."),
    DEVICE_NOT_FOUND(40406, HttpStatus.NOT_FOUND, "해당 장비를 찾을 수 없습니다."),
    NOTIFICATION_TEMPLATE_NOT_FOUND(40407, HttpStatus.NOT_FOUND, "해당 알림 템플릿을 찾을 수 없습니다."),
    
    // EMR 관련 에러 코드
    MEDICAL_RECORD_NOT_FOUND(40408, HttpStatus.NOT_FOUND, "해당 진료기록을 찾을 수 없습니다."),
    CHART_TEMPLATE_NOT_FOUND(40409, HttpStatus.NOT_FOUND, "해당 차트 템플릿을 찾을 수 없습니다."),
    PRESCRIPTION_NOT_FOUND(40410, HttpStatus.NOT_FOUND, "해당 처방전을 찾을 수 없습니다."),
    LAB_ORDER_NOT_FOUND(40411, HttpStatus.NOT_FOUND, "해당 검사 오더를 찾을 수 없습니다."),
    
    // Billing 관련 에러 코드
    PROCEDURE_CODE_NOT_FOUND(40412, HttpStatus.NOT_FOUND, "해당 행위코드를 찾을 수 없습니다."),
    BILLING_NOT_FOUND(40413, HttpStatus.NOT_FOUND, "해당 청구 내역을 찾을 수 없습니다."),
    RECEIPT_NOT_FOUND(40414, HttpStatus.NOT_FOUND, "해당 영수증을 찾을 수 없습니다."),
    
    // Pharmacy 관련 에러 코드
    DRUG_MASTER_NOT_FOUND(40415, HttpStatus.NOT_FOUND, "해당 약물 마스터를 찾을 수 없습니다."),
    DRUG_INVENTORY_NOT_FOUND(40416, HttpStatus.NOT_FOUND, "해당 약물 재고를 찾을 수 없습니다."),
    INSUFFICIENT_STOCK(40017, HttpStatus.BAD_REQUEST, "재고가 부족합니다."),
    
    // Bed Management 관련 에러 코드
    BED_NOT_FOUND(40418, HttpStatus.NOT_FOUND, "해당 병상을 찾을 수 없습니다."),
    ADMISSION_NOT_FOUND(40419, HttpStatus.NOT_FOUND, "해당 입원 정보를 찾을 수 없습니다."),
    BED_OCCUPIED(40020, HttpStatus.BAD_REQUEST, "병상이 이미 사용 중입니다."),
    PATIENT_ALREADY_ADMITTED(40021, HttpStatus.BAD_REQUEST, "환자가 이미 입원 중입니다."),
    /**
     * 401(Unauthorized)
     *   비록 HTTP 표준에서는 "미승인(unauthorized)"를 명확히 하고 있지만,
     *   의미상 이 응답은 "비인증(unauthenticated)"을 의미합니다.
     *   클라이언트는 요청한 응답을 받기 위해서는 반드시 스스로를 인증해야 합니다.
     * */

    /**
     * 403(Forbidden)
     * 클라이언트는 콘텐츠에 접근할 권리를 가지고 있지 않습니다.
     * 예를들어 그들은 미승인이어서 서버는 거절을 위한 적절한 응답을 보냅니다. 401과 다른 점은 서버가 클라이언트가 누구인지 알고 있습니다.
     */


    /**

     * 404(Not Found)
     *   서버는 요청받은 리소스를 찾을 수 없습니다. 브라우저에서는 알려지지 않은 URL을 의미합니다.
     *   이것은 API에서 종점은 적절하지만 리소스 자체는 존재하지 않음을 의미할 수도 있습니다.
     *   서버들은 인증받지 않은 클라이언트로부터 리소스를 숨기기 위하여 이 응답을 403 대신에 전송할 수도 있습니다.
     *   이 응답 코드는 웹에서 반복적으로 발생하기 때문에 가장 유명할지도 모릅니다.
     * */
    HOSPITAL_NOT_FOUND(40400, HttpStatus.NOT_FOUND, "해당 병원을 찾을 수 없습니다."),
    DEPARTMENT_NOT_FOUND(40401, HttpStatus.NOT_FOUND, "해당 진료과를 찾을 수 없습니다."),
    DOCTOR_NOT_FOUND(40402, HttpStatus.NOT_FOUND, "해당 의사를 찾을 수 없습니다."),
    PATIENT_NOT_FOUND(40403, HttpStatus.NOT_FOUND, "해당 환자를 찾을 수 없습니다."),
    APPOINTMENT_NOT_FOUND(40404, HttpStatus.NOT_FOUND, "해당 예약을 찾을 수 없습니다."),
    /**
     * 500(Internal Server Error)
     *   서버가 처리 방법을 모르는 상황이 발생했습니다. 서버는 아직 처리 방법을 알 수 없습니다.
     * */
    INTERNAL_SERVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");


    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
