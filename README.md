## 요구사항 분석 (콘서트 예약 서비스)

- 유저 대기열 토큰 기능
  - 토큰 발급 : UUID로 토큰 생성
  - 폴링으로 본인의 대기열을 확인한다고 가정

- 예약 가능 날짜 / 좌석 API
  - 예약 가능한 날짜 목록을 조회
  - 날짜 정보를 입력받아 예약가능한 좌석정보를 조회
  - 좌석 정보는 1 ~ 50 까지의 좌석번호로 관리됨  

- 좌석 예약 요청 API
  - 좌석 예약과 동시에 해당 좌석은 그 유저에게 약 5분간 임시 배정됨
  - 배정 시간 내에 결제가 완료되지 않았다면 좌석에 대한 임시 배정은 해제
  - 배정 시간 내에는 다른 사용자는 예약할 수 없어야 한다.

- 잔액 충전 / 조회 API
  - 토큰과 충전할 금액을 받아 잔액을 충전
  - 토큰을 통해 해당 사용자의 잔액을 조회

- 결제API
  - 결제가 완료되면 해당 좌석의 소유권을 유저에게 배정하고 대기열 토큰을 만료<br/>

#### Description
- 대기열 테이블을 이용하여 대기열 시스템 구현
- 예약 서비스는 작업가능한 유저만 수행할 수 있도록 해야함.
- 사용자는 좌석예약 시에 미리 충전한 잔액을 이용
- 좌석 예약 요청시에, 결제가 이루어지지 않더라도 일정 시간동안 다른 유저가 해당 좌석에 접근할 수 없도록 해야함.
- 동시성 이슈를 고려하여 구현

#### Key Point
- 유저간 대기열을 요청 순서대로 정확하게 제공할 방법
- 동시에 여러 사용자가 예약 요청을 했을 때, 좌석이 중복으로 배정 가능하지 않도록 해야함


### 시퀀스 다이어그램
![콘서트 예약 서비스 시퀀스 다이어그램](https://github.com/corncode8/hhplus_Server_3rd/assets/127717982/a6d5eb55-eaa3-4ed2-af33-0a0a5c88bbe8)


### ERD
![hhplus_3rd_erd](https://github.com/corncode8/hhplus_Server_3rd/assets/127717982/403f6054-b244-4c83-8cc7-76593b3f7b78)


### API 명세

#### 유저 대기열 토큰 기능
토큰 생성 API <br/>
| 메서드 | URL |
|--------|-----------|
| POST    |/api/getToken  |
- request body
```
{
    "username":"testUser"
}
```
- response body
```
{
    "isSuccess": true,
    "code": 200,
    "message": "요청에 성공하였습니다.",
    "result": {
        "token": "wer7w-edt-w5g-dsrgdrg-testToken",
        "listNum": 1,
        "expectedTime": "2024-04-03T13:19:03.6565297"
    }
}
```

대기열 확인 API <br/>
| 메서드 | URL |
|--------|-----------|
| GET    |/api/check  |
- request header
```
token="wer7w-edt-w5g-dsrgdrg-testToken"
```
- response body
```
{
    "isSuccess": true,
    "code": 200,
    "message": "요청에 성공하였습니다.",
    "result": {
        "listNum": 1,
        "expectedTime": "2024-04-03T13:20:55.1693036"
    }
}
```

#### 예약 기능
예약 가능한 날짜 조회 API <br/>
| 메서드 | URL |
|--------|-----------|
| GET    |/api/booking/{concertId}/list/time  |
- request header
```
token="wer7w-edt-w5g-dsrgdrg-testToken"
```
- response body
```
{
    "isSuccess": true,
    "code": 200,
    "message": "요청에 성공하였습니다.",
    "result": {
        "availableDates": [
            "2024-04-08",
            "2024-04-09",
            "2114-04-03"
        ]
    }
}
```

예약 가능한 좌석 정보 조회 API <br/>
| 메서드 | URL |
|--------|-----------|
| GET    |/booking/{concertId}/dates/{date}/list/seats |
- request header
```
token="wer7w-edt-w5g-dsrgdrg-testToken"
```
- response body
```
{
    "isSuccess": true,
    "code": 200,
    "message": "요청에 성공하였습니다.",
    "result": {
        "availableSeatsList": [
            "1",
            "2",
            "5",
            "9",
            "16"
        ]
    }
}
```

좌석 예약 API <br/>
| 메서드 | URL |
|--------|-----------|
| POST    |/api/booking/{concertId}/seat  |
- request header
```
token="wer7w-edt-w5g-dsrgdrg-testToken"
```
- request body
```
{
    "concertAt": "2024-03-01",
    "reservationAmount": 50000,
    "seatNumbers": [
            "1",
            "2",
            "5",
            "9",
            "16"
        ]
}
```
- response body
```
{
    "isSuccess": true,
    "code": 200,
    "message": "요청에 성공하였습니다.",
    "result": {
        "concertName": "MAKTUB Concert",
        "reservationDate": "2024-03-01",
        "reservationSeat": "2"
    }
}
```

#### 포인트 기능
잔액 충전 API <br/>
| 메서드 | URL |
|--------|-----------|
| PATCH    |/api/{userId}/charge  |
- request body
```
{
    "amount": 50000
}
```
- response body
```
{
    "isSuccess": true,
    "code": 200,
    "message": "요청에 성공하였습니다.",
    "result": {
        "userId": 1,
        "point": 50000
    }
}
```

잔액 조회 API <br/>
| 메서드 | URL |
|--------|-----------|
| GET    |/api/{userId}  |
- response body
```
{
    "isSuccess": true,
    "code": 200,
    "message": "요청에 성공하였습니다.",
    "result": {
        "userId": 1,
        "point": 65050
    }
}
```

잔액 리스트 조회 API <br/>
| 메서드 | URL |
|--------|-----------|
| GET    |/api/{userId}/histories  |
- response body
```
{
    "isSuccess": true,
    "code": 200,
    "message": "요청에 성공하였습니다.",
    "result": [
        {
            "id": 1,
            "userId": 1,
            "state": "CHARGE",
            "amount": 500,
            "time": "2024-04-03T13:28:37.4315016"
        }
    ]
}
```

#### 결제 기능
결제 API <br/>
| 메서드 | URL |
|--------|-----------|
| POST    |/api/{reservationId}/payment  |
- request body
```
{
    "payAmount": 50000
}
```
- response body
```
{
    "isSuccess": true,
    "code": 200,
    "message": "요청에 성공하였습니다.",
    "result": {
        "id": 1,
        "reservationId": 1,
        "payAmount": 50000,
        "payAt": "2024-04-03T13:30:24.4380995"
    }
}
```



