## 프론트엔드 화면 설계서 (옵션 A 기준)

### 개요
- 목적: 현재 백엔드 엔티티/DDL과 정합되는 최소 스펙 화면/플로우 및 API 매핑 정의
- 권한:
  - Admin: 시스템 관리(리소스/알림/코드/재고/베드 등)
  - Staff: EMR, 청구, 입퇴원 처리
  - Patient: 포털(후순위)

### 공통 UI/패턴
- 테이블: 서버 페이지네이션, 검색/필터, 고정 헤더, 정렬(후순위)
- 상세: Drawer 또는 Modal
- 폼: Modal/페이지 폼, 필수값 및 타입 검증
- 피드백: 성공/실패 토스트, 폼 필드 에러 메시지
- 참조 선택: Patient/Doctor/Room/DrugMaster Lookup 모달 재사용
- 권장 스택: React + React Query + React Hook Form + Zod + Ant Design(MUI 대체 가능)
- 라우팅(권장): `/emr`, `/billing`, `/pharmacy`, `/beds`, `/resources`, `/notifications`

---

## EMR

### 진료기록 목록 (`/emr/medical-records`)
- 목적: 진료기록 열람/검색
- 필터: `patientId`, `doctorId`, `recordDate[From~To]`
- 테이블: `medicalRecordId`, `patient`, `doctor`, `recordDate`, `createdAt`
- 액션: 상세 보기, 새 기록 생성
- API:
  - GET `/api/emr/medical-records`
  - GET `/api/emr/medical-records/patient/{patientId}`
  - GET `/api/emr/medical-records/doctor/{doctorId}`

와이어프레임
```
[필터]
 Patient ID [____]  Doctor ID [____]  Record Date [____ ~ ____]  (검색)

[목록]
| ID | Patient | Doctor | Record Date      | Created At        | (보기) |
|----|---------|--------|------------------|-------------------|--------|
| 12 | 홍길동   | 김의사  | 2025-10-01 10:00 | 2025-10-01 10:02  | [상세] |

[우측 상단] (+) 새 기록
[하단] < 1 2 3 >
```

### 진료기록 상세/편집 (`/emr/medical-records/:id`)
- 필드: `medicalRecordId`, `patient`, `doctor`, `recordDate`, `content`, `createdAt`, `updatedAt`
- 액션: 수정/저장
- API: GET `/api/emr/medical-records/{id}`, PUT `/api/emr/medical-records/{id}`

와이어프레임
```
[상단] 진료기록 #12
 Patient: 홍길동 (ID: 1001)
 Doctor:  김의사 (ID: 2001)
 Record Date: [2025-10-01 10:00] (편집)

[Content]
 ---------------------------------------------------
 | (멀티라인 TEXT: content)                         |
 ---------------------------------------------------

[우측 상단] (수정 저장) (취소)
```

### 진료기록 생성 (`/emr/medical-records/new`)
- 필드: `patientId`, `doctorId`, `recordDate`, `content`
- API: POST `/api/emr/medical-records`

와이어프레임
```
 Patient   [검색/선택 ▽]
 Doctor    [검색/선택 ▽]
 RecordDate [yyyy-MM-dd HH:mm]
 Content   [멀티라인 TEXT]

 (저장) (취소)
```

### 차트 템플릿 (`/emr/chart-templates`)
- 목록 필드: `chartTemplateId`, `code`, `name`, `version`, `updatedAt`
- 생성/수정: `code(생성만)`, `name`, `content`
- API: GET/POST/PUT/DELETE `/api/emr/chart-templates(/{id})`

와이어프레임
```
| ID | Code     | Name     | Version | Updated At        | (수정/삭제) |
|----|----------|----------|---------|-------------------|-------------|
| 7  | SOAP_GEN | SOAP 기본 | 3       | 2025-10-01 09:00  | [수정][X]   |

[+] 새 템플릿 → 모달: Code, Name, Content
```

### 처방전 (`/emr/prescriptions`)
- 목록 필드: `prescriptionId`, `medicalRecordId`, `drugName`, `dosage`, `frequency`, `duration`, `updatedAt`
- 생성: `medicalRecordId`, `drugName`, `dosage`, `frequency`, `duration`, `notes`
- API:
  - GET (환자/의사/진료기록 기준)
  - POST `/api/emr/prescriptions`

와이어프레임
```
[필터] Patient ID / Doctor ID
| ID | Med.Record | Drug Name | Dosage | Frequency | Duration | Updated At |
[+] 새 처방전 → 모달: MedicalRecord, DrugName, Dosage, Frequency, Duration, Notes
```

### 검사 오더 (`/emr/lab-orders`)
- 목록 필드: `labOrderId`, `medicalRecordId`, `testName`, `orderDate`, `status`, `resultDate`
- 생성: `medicalRecordId`, `testName`, `orderDate`
- 완료: `result`, `resultDate`
- API:
  - GET (환자/의사/진료기록/상태 기준)
  - POST `/api/emr/lab-orders`
  - POST `/api/emr/lab-orders/{id}/complete`

와이어프레임
```
[필터] Patient ID / Doctor ID
| ID | Med.Record | Test Name | Order Date | Status | Result Date | (완료) |
[+] 새 오더 → 모달: MedicalRecord, TestName, OrderDate
[완료] → 모달: Result, ResultDate
```

---

## Billing

### 행위코드 (`/billing/procedure-codes`)
- 필드: `procedureCodeId`, `code`, `name`, `price`, `updatedAt`
- 생성/수정: `code(생성만)`, `name`, `price`
- API: GET/POST/PUT/DELETE `/api/billing/procedure-codes(/{id})`

와이어프레임
```
| ID | Code | Name      | Price    | Updated At        | (수정/삭제) |
|----|------|-----------|----------|-------------------|-------------|
| 5  | A001 | 초진진찰료 | 15000.00 | 2025-10-01 08:30  | [수정][X]   |
[+] 새 코드 → 모달: Code, Name, Price
```

### 청구 (`/billing/billings`)
- 필드: `billingId`, `patient`, `appointmentId?`, `billingDate`, `totalAmount`, `insuranceAmount`, `patientAmount`, `status`, `updatedAt`
- 생성: `patientId`, `appointmentId?`, `billingDate`, `totalAmount`, `insuranceAmount`, `patientAmount`
- 상태변경: `pay`, `cancel`
- API:
  - GET (환자/예약/상태/기간 기준)
  - POST `/api/billing/billings`
  - PUT `/api/billing/billings/{id}`
  - POST `/api/billing/billings/{id}/pay`
  - POST `/api/billing/billings/{id}/cancel`

와이어프레임
```
[필터] Patient ID / Appointment ID / Status / DateRange
| ID | Patient | Appt | BillingDate | Total | Insurance | Patient | Status | (보기) |
[+] 새 청구 → 모달: Patient, Appointment?, BillingDate, Totals
[상세 Drawer] 결제/취소 버튼
```

### 영수증 (`/billing/receipts`)
- 필드: `receiptId`, `billingId`, `receiptDate`, `amountPaid`, `paymentMethod`, `updatedAt`
- 생성: `billingId`, `receiptDate`, `amountPaid`, `paymentMethod`
- API:
  - GET `/api/billing/receipts/{id}`
  - GET `/api/billing/receipts/billing/{billingId}`
  - POST `/api/billing/receipts`

와이어프레임
```
| ID | BillingID | Receipt Date | AmountPaid | PaymentMethod | Updated At |
[+] 새 영수증 → 모달: Billing, ReceiptDate, AmountPaid, PaymentMethod
```

---

## Pharmacy

### 의약품 마스터 (`/pharmacy/drug-masters`)
- 검색: `name` contains
- 필드: `drugMasterId`, `code`, `name`, `manufacturer`, `unitPrice`, `updatedAt`
- 생성/수정: `code(생성만)`, `name`, `manufacturer`, `unitPrice`, `description`
- API:
  - GET `/api/pharmacy/drug-masters`
  - GET `/api/pharmacy/drug-masters/search?name=...`
  - POST/PUT/DELETE `/api/pharmacy/drug-masters(/{id})`

와이어프레임
```
검색: Name [____] (검색)
| ID | Code | Name | Manufacturer | UnitPrice | Updated At | (수정/삭제) |
[+] 새 약물 → 모달: Code, Name, Manufacturer, UnitPrice, Description
```

### 재고 (`/pharmacy/drug-inventories`)
- 필터: `drugMasterId`, `location`, `expiration[From~To]`
- 필드: `drugInventoryId`, `drug(code/name)`, `batchNumber`, `expirationDate`, `quantity`, `location`, `updatedAt`
- 생성/수정: `drugMasterId`, `batchNumber`, `expirationDate`, `quantity`, `location`
- 수량 조정: 선택 재고에 대해 증감
- API:
  - GET (약물/위치/기간 기준)
  - POST/PUT/DELETE `/api/pharmacy/drug-inventories(/{id})`
  - POST `/api/pharmacy/drug-inventories/adjust`

와이어프레임
```
[필터] Drug / Location / Expiration
| ID | Drug | Batch | Expiration | Qty | Location | Updated At | (수정/삭제) |
[+] 재고 추가 → 모달
[수량 조정] → 모달: +/- 수량, Reason
```

---

## Bed Management

### 병상 (`/beds`)
- 필터: `roomId`, `status`, `bedType`
- 필드: `bedId`, `room(code/name)`, `bedNumber`, `bedType`, `status`, `updatedAt`
- 생성/수정: `roomId`, `bedNumber(생성)`, `bedType`, `description`
- 상태: `maintenance`, `available`
- API:
  - GET `/api/beds/room/{roomId}`
  - GET `/api/beds/status/{status}`
  - GET `/api/beds/type/{bedType}`
  - GET `/api/beds/available`, `/api/beds/available/type/{bedType}`
  - POST/PUT/DELETE `/api/beds(/{id})`
  - POST `/api/beds/{id}/maintenance`, `/api/beds/{id}/available`

와이어프레임
```
[필터] Room / Status / BedType
| ID | Room(Code/Name) | BedNumber | BedType | Status       | Updated At        | (보기/수정/상태) |
|----|------------------|-----------|---------|--------------|-------------------|------------------|
| 5  | R101/내과진료실    | 01        | GEN     | AVAILABLE    | 2025-10-01 08:10  | [상세][수정][점검][사용] |

[우측 상단] (+) 새 병상
[모달]
 Room [검색/선택]
 BedNumber [생성시 입력]
 BedType [▽]
 Description [멀티라인]
 (저장) (취소)
```

### 입퇴원 (`/beds/admissions`)
- 필터: `patientId`, `bedId`, `status`
- 테이블: `admissionId`, `patient`, `bedNumber`, `room(code/name)`, `admissionDate`, `dischargeDate`, `status`, `updatedAt`
- 생성: `patientId`, `bedId`, `admissionDate`, `reason`, `notes`
- 수정: `reason`, `notes`
- 퇴원: `dischargeDate`, `reason`, `notes`
- 전실: `newBedId`, `transferDate`, `reason`, `notes`
- API:
  - GET (환자/병상/상태 기준)
  - POST `/api/beds/admissions`
  - PUT `/api/beds/admissions/{id}`
  - POST `/api/beds/admissions/{id}/discharge`
  - POST `/api/beds/admissions/{id}/transfer`

와이어프레임
```
[필터]
 Patient ID [____]  Bed ID [____]  Status [▽] (검색)

[목록]
| ID | Patient | Bed | Room | AdmissionDate      | DischargeDate      | Status | (보기) |
|----|---------|-----|------|--------------------|--------------------|--------|--------|
| 12 | 홍길동   | 01  | R101 | 2025-10-01 09:00   | -                  | ACTIVE | [상세] |

[우측 상단] (+) 입원 등록
[입원 등록 모달]
 Patient [검색/선택]
 Bed     [검색/선택]
 AdmissionDate [datetime]
 Reason  [멀티라인]
 Notes   [멀티라인]
 (저장) (취소)

[상세 Drawer]
 ... 기본 정보 ...
 (정보 수정) (퇴원 처리) (전실 처리)

[퇴원 모달]
 DischargeDate [datetime]
 Reason [멀티라인]
 Notes  [멀티라인]
 (확인) (취소)

[전실 모달]
 NewBed [검색/선택]
 TransferDate [datetime]
 Reason/Notes [멀티라인]
 (확인) (취소)
```

### 가용성/점유 요약
- `/beds/availability`
  - 필터: `bedType`, `roomCode`
  - API: POST `/api/beds/availability`
- `/beds/occupancy-summary`
  - API: GET `/api/beds/occupancy-summary`

와이어프레임
```
/beds/availability
[필터] BedType [▽]  RoomCode [____] (조회)

[카드/표]
| Bed | Room | BedType | Status | LastOccupiedAt | ETA Available |
|-----|------|---------|--------|----------------|---------------|

/beds/occupancy-summary
| Room | Total | Occupied | Available | Maintenance | Occupancy(%) |
```

---

## 보조 모듈

### 리소스 (Rooms/Devices)
- 경로: `/resources/rooms`, `/resources/devices`
- 필드: `id`, `code`, `name`
- API: GET/POST/PUT/DELETE `/api/resources/rooms`, `/api/resources/devices`

### 알림 (Templates/Jobs, Admin)
- 경로: `/notifications/admin/templates`, `/notifications/admin/jobs`
- 템플릿: `id`, `channel`, `code`, `title`, `updatedAt` (생성/수정/삭제)
- 잡: `id`, `channel`, `recipient`, `templateCode`, `status`, `retryCount`, `nextAttemptAt`
- API: `/api/notifications/admin/*`

---

## 기술 가이드(프론트)
- 상태관리: React Query(서버 상태), 폼 상태는 컴포넌트 내부
- 폼: React Hook Form + Zod(숫자/날짜/필수 검증)
- 테이블: 서버 페이징 + 컬럼 정의 공통 훅
- 국제화: i18next(한/영)
- 접근제어: 라우트 가드 + 역할별 메뉴 노출

---

## 구현 우선순위 제안
1) EMR(진료기록/검사/처방) 목록/상세/생성
2) Bed(병상/입퇴원) 운영 플로우
3) Billing(행위코드/청구/영수증)
4) Pharmacy(의약품/재고)
5) 리소스/알림 관리


