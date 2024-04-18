## 요구사항 분석 (콘서트 예약 서비스)

- 유저 대기열 토큰 기능
  - 토큰 발급
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


## ERD
![concert erd](https://raw.githubusercontent.com/corncode8/hhplus_Server_3rd/main/images/erd/hhplus_3rd_erd.png)
## API 명세

## Swagger
![Swagger](https://raw.githubusercontent.com/corncode8/hhplus_Server_3rd/main/images/swagger/Swagger%20API.png)

#### 유저 대기열 토큰 기능
![token api](https://raw.githubusercontent.com/corncode8/hhplus_Server_3rd/main/images/sequence_diagram/issue_token_api.png)
<br/>
토큰 발급  API <br/>
| 메서드 | URL |
|--------|-----------|
| GET    |/api/wait  |
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
        "state": "WAITING"
    }
}
```
![queue api](https://raw.githubusercontent.com/corncode8/hhplus_Server_3rd/main/images/sequence_diagram/queue_api.png)
<br/>
대기열 확인 API <br/>
| 메서드 | URL |
|--------|-----------|
| GET    |/api/wait/check  |
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
    }
}
```

#### 예약 기능
![concert api](https://raw.githubusercontent.com/corncode8/hhplus_Server_3rd/main/images/sequence_diagram/concert_api.png)
<br/>
콘서트 조회 API <br/>
| 메서드 | URL |
|--------|-----------|
| GET    |/api/concert  |
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
        "concertInfo": [
            "concertId": 1,
            "concertName": "MAKTUB CONCERT",
            "artist": "MAKTUB"
        ]
    }
}
```
![date api](https://raw.githubusercontent.com/corncode8/hhplus_Server_3rd/main/images/sequence_diagram/reservation_date_seat_api.png)
<br/>
예약 가능한 날짜 조회 API <br/>
| 메서드 | URL |
|--------|-----------|
| GET    |/api/concert/{concertId}/date  |
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
| GET    |/api/concert/{concertId}/date/{date}/seats |
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
        "concertOptionId": 1,
        "seatList": [
            "1",
            "2",
            "5",
            "9",
            "16"
        ]
    }
}
```
![reservation api](https://raw.githubusercontent.com/corncode8/hhplus_Server_3rd/main/images/sequence_diagram/reservation_concert_api.png)
<br/>
콘서트 예약 API <br/>
| 메서드 | URL |
|--------|-----------|
| POST    |/api/reservation  |
- request header
```
token="wer7w-edt-w5g-dsrgdrg-testToken"
```
- request body
```
{
    "concertOptionId": "3",
    "targetDate": "2024-03-01",
    "seatNumber": 2
}
```
- response body
```
{
    "isSuccess": true,
    "code": 200,
    "message": "요청에 성공하였습니다.",
    "result": {
        "reservationId": 1,
        "concertName": "MAKTUB Concert",
        "concertArtist": "MAKTUB",
        "reservationDate": "2024-03-01",
        "reservationSeat": 2,
        "expiredAt": "2024-03-01 15:35",
        "price": 50000
    }
}
```

#### 포인트 기능
![balance api](https://raw.githubusercontent.com/corncode8/hhplus_Server_3rd/main/images/sequence_diagram/balance_api.png)
<br/>
잔액 충전 API <br/>
| 메서드 | URL |
|--------|-----------|
| PATCH    |/api/point/{userId}/charge  |
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
| GET    |/api/point/{userId}/account  |
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
| GET    |/api/point/{userId}/histories  |
- response body
```
{
    "isSuccess": true,
    "code": 200,
    "message": "요청에 성공하였습니다.",
    "result": {
        "pointHistoryList": [
             "id": 1,
            "userId": 1,
            "state": "CHARGE",
            "amount": 500,
            "time": "2024-04-03T13:28:37.4315016"
    ]        
  }
}
```

#### 결제 기능
![payment api](https://raw.githubusercontent.com/corncode8/hhplus_Server_3rd/main/images/sequence_diagram/payment_api.png)
<br/>
결제 API <br/>
| 메서드 | URL |
|--------|-----------|
| POST    |/api/payment  |
- request header
```
token="wer7w-edt-w5g-dsrgdrg-testToken"
```
- request body
```
{
    "reservationId": 1,
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
        "payId": 1,
        "reservationId": 1,
        "payAmount": 50000,
        "payAt": "2024-04-03T13:30:24.4380995"
    }
}
```



