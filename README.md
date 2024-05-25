# 기능 요구 사항



## 데이터 수집 및 저장

**CPU 사용률 수집**: 서버의 CPU 사용률을 분 단위로 수집합니다.


**데이터 저장**: 수집된 데이터를 데이터베이스에 저장합니다.



## 데이터 조회 API

**분 단위 조회**: 지정한 시간 구간의 분 단위 CPU 사용률을 조회합니다.


**시 단위 조회**: 지정한 날짜의 시  단위 CPU 최소/최대/평균 사용률을 조회합니다.


**일 단위 조회**: 지정한 날짜 구간의 일  단위 CPU 최소/최대/평균 사용률을 조회합니다.


Swagger를 사용하여 API 문서화를 설정하세요.



## 데이터 제공 기한


**분 단위 API** : 최근 1주 데이터 제공


**시 단위 API** : 최근 3달 데이터 제공


**일 단위 API** : 최근 1년 데이터 제공