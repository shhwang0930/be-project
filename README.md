# 기능 요구 사항



## 데이터 수집 및 저장


**CPU 사용률 수집 및 저장**: 서버의 CPU 사용률을 분 단위로 수집합니다.
- CpuService 내의 checkCpuUsage에서 분 단위로 CPU사용률 저장(분 단위 사용률 저장)
- CpuService 내의 saveHourUsage에서 분단위 사용률을 토대로 시간당 최소, 최대, 평균값의 CPU 사용률을 집계
- CpuService 내의 saveDayUsage에서 시간단위 사용률을 토대로 일당 최소, 최대, 평균값의 CPU 사용률을 집계


<br/>


## 데이터 조회 API

**분 단위 조회**: 지정한 시간 구간의 분 단위 CPU 사용률을 조회합니다. : CpuController 내의 getCpuUsageMinute를 통해 지정한 시간 s와 e사이의 분 단위 CPU 사용률을 조회


**시 단위 조회**: 지정한 날짜의 시  단위 CPU 최소/최대/평균 사용률을 조회합니다. : CpuController 내의 getCpuUsageHour 통해 지정한 시간 s와 e사이의 시간 단위 CPU 사용률(최소, 최대, 평균)을 조회



**일 단위 조회**: 지정한 날짜 구간의 일  단위 CPU 최소/최대/평균 사용률을 조회합니다. : CpuController 내의 getCpuUsageDay 통해 지정한 시간 s와 e사이의 일 단위 CPU 사용률(최소, 최대, 평균)을 조회



Swagger를 사용하여 API 문서화를 설정하세요. : http://localhost:8080/swagger-ui/ 에서 확인 가능


<br/>


## 데이터 제공 기한


**분 단위 API** : 최근 1주 데이터 제공 : CpuService 내의 deleteMinute에서  매일 자정 1주전의 분 단위 CPU사용률을 DB에서 삭제(분 단위 사용률 삭제)


**시 단위 API** : 최근 3달 데이터 제공 : CpuService 내의 deleteHour에서  매월 1일 자정 3개월전의 시간 단위 CPU사용률을 DB에서 삭제(분 단위 사용률 삭제)


**일 단위 API** : 최근 1년 데이터 제공 : CpuService 내의 deleteDay에서  매년 1월 1일 자정 1년전의 일 단위 CPU사용률을 DB에서 삭제(분 단위 사용률 삭제)
