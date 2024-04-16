package hhplus.serverjava.api.support.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {


    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),


    /**
     * 400 : Request, Response 오류
     */
    EMPTY_NAME_USER(false, HttpStatus.BAD_REQUEST.value(), "이름을 입력해주세요."),
    NOT_FIND_USER(false,HttpStatus.NOT_FOUND.value(),"일치하는 유저가 없습니다."),
    FAIL_NEW_TOKEN(false,HttpStatus.NOT_FOUND.value(),"토큰 생성에 실패했습니다."),
    FAIL_FIND_QUEUE(false,HttpStatus.NOT_FOUND.value(),"대기열 확인에 실패하였습니다."),

    NOT_FIND_POINT_LIST(false,HttpStatus.NOT_FOUND.value(),"포인트 내역이 없습니다."),
    NOT_ENOUGH_POINT(false,HttpStatus.NOT_FOUND.value(),"포인트가 부족합니다."),

    EMPTY_JWT(false, HttpStatus.UNAUTHORIZED.value(), "JWT를 입력해주세요."),
    INVALID_JWT(false, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 JWT입니다."),


    CONCERT_EMPTY_DATE(false, HttpStatus.BAD_REQUEST.value(), "콘서트 날짜를 입력해주세요."),
    NOT_FIND_CONCERT(false,HttpStatus.NOT_FOUND.value(),"일치하는 콘서트가 없습니다."),
    EMPTY_CONCERT(false,HttpStatus.NOT_FOUND.value(),"상영중인 콘서트가 없습니다."),

    INVALID_SEAT(false, HttpStatus.NOT_FOUND.value(), "유효하지 않은 좌석 입니다."),
    INVALID_SEAT_RESERVATION(false, HttpStatus.BAD_REQUEST.value(), "예약 가능한 좌석이 아닙니다."),
    INVALID_RESERVATION(false, HttpStatus.NOT_FOUND.value(), "일치하는 예약 정보가 없습니다."),
    EMPTY_SEAT_RESERVATION(false, HttpStatus.NOT_FOUND.value(), "예약 가능한 좌석이 없습니다."),
    RESERVED_SEAT(false, HttpStatus.BAD_REQUEST.value(), "예약된 좌석입니다."),
    FAIL_RESERVAION_SEAT(false, HttpStatus.BAD_REQUEST.value(), "예약에 실패하였습니다."),

    INVALID_DATE(false, HttpStatus.BAD_REQUEST.value(), "형식에 맞지 않는 날짜 양식입니다."),
    NOT_FOUND_CONCERT_OPTION(false, HttpStatus.BAD_REQUEST.value(), "찾을 수 없는 콘서트 일정입니다."),

    /**
     * 500 :  Database, Server 오류
     */
    DATABASE_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 연결에 실패하였습니다."),
    DATABASE_EMPTY(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "저장된 데이터가 없습니다."),
    SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버와의 연결에 실패하였습니다."),

    SCHEDULER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "스케줄러 오류"),




    UNEXPECTED_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "예상치 못한 에러가 발생했습니다.");







    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}