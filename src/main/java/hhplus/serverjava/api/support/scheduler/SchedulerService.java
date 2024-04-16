package hhplus.serverjava.api.support.scheduler;

import java.time.LocalDateTime;

public interface SchedulerService {

    /*
   좌석이 만료된 예약 확인
   @param now
   @return Boolean
    */
    Boolean expiredReservations(LocalDateTime now);

   /*
   좌석이 만료된 예약 처리
   좌석 활성화 + 예약 취소 처리
   @param now
   @return void
    */
    void expireReservations(LocalDateTime now);

     /*
   서비스에 입장한 후 10분이 지나도록
   결제를 안하고 있는 유저가 있는지 확인
   @param  now
   @return Boolean
    */
     Boolean userTimeValidation (LocalDateTime now);

     /*
   서비스에 입장한 후 10분이 지나도록
   결제를 안하고 있는 유저 만료 처리
   @param now
   @return void
    */
    void expireUsers(LocalDateTime now);

     /*
   서비스를 이용중인 유저가 100명 미만인지 확인
   @param  now
   @return Boolean
    */
     Boolean workingUserNumValidation (LocalDateTime now);

     /*
   서비스를 이용중인 유저가 N명 미만인 만큼
   대기중인 유저 입장
   @param  now
   @return Boolean
    */
    void enterServiceUser();
}
