# 카카오 뿌리기 API 구현

## 사용 환경
Spring Boot, Redis (embedded) , H2 , JPA

## API 명세

뿌리기
```  
POST api/handouts    
Content-type : application/json;charset=utf-8  
X-ROOM-ID: {{채팅방 고유 번호}}  
X-USER-ID: {{유저 고유 번호}}  

{  
  "participants" : {{ 참가자 수 }},  
  "amount" : {{ 뿌릴 금액 }}  
}

응답 - 성공

HTTP/1.1 204 No content
Date: Wed, 21 Oct 2015 07:28:00 GMT
X-TOKEN-ID : {{발급 토큰}}
```

받기  
```
POST api/handouts/take    
Content-type : application/json;charset=utf-8  
X-ROOM-ID: {{채팅방 고유 번호}}  
X-USER-ID: {{유저 고유 번호}}  
X-TOKEN-ID : {{발급 토큰}}

응답 - 성공

HTTP/1.1 200 OK
Date: Wed, 21 Oct 2015 07:28:00 GMT

{{ 받은 금액 }}
```

조회
```
GET api/handouts    
Content-type : application/json;charset=utf-8  
X-ROOM-ID: {{채팅방 고유 번호}}  
X-USER-ID: {{유저 고유 번호}}  
X-TOKEN-ID : {{발급 토큰}}

응답 - 성공

HTTP/1.1 200 OK
Date: Wed, 21 Oct 2015 07:28:00 GMT

{
  "amount": {{총 뿌린 금액}},
  "paidAmount": {{받아 간 금액}},
  "createdAt": {{뿌리기 생성 날짜}},
  "benefits": [
    {
      "receiverId": {{받아간 사람 아이디}},
      "amount": {{받은 금액}},
      "receiveTime": {{받은 시간}},
    }
  ]
}

```

## 구현 목표

### 공통
Spring Boot 를 통하여 간소화된 API Server를 구축한다.

### 키
Redis를 통해 7일동안 유지하며 handout:{roomid}:token : { 뿌리기 번호 } 로 구성함  
알파벳 대소문자 및 숫자로 3자리 난수를 생성할 경우 62^3 = 238,328‬개로 많은 사용량을 커버하기 위해  
ROOMID 와 조합하여 사용함.  

### 동시성 제어
데이터 베이스에 위임하여 동시성 제어함. 
 
동일 사용 방지  
뿌리기 아이디와 받은 사람 아이디를 UNIQUE Constraint로 활용하여 중복 사용을 방지함.  
받기  
낙관적 잠금을 사용하여 동일한 혜택(뿌리기 중 나눠진 한건)에 대해서 중복 사용을 방지함.



## 상세 구현 요건 및 제약사항 

1. 뿌리기 API ● 다음 조건을 만족하는 뿌리기 API를 만들어 주세요.  
○ 뿌릴 금액, 뿌릴 인원을 요청값으로 받습니다.  
○ 뿌리기 요청건에 대한 고유 token을 발급하고 응답값으로 내려줍니다.   
○ 뿌릴 금액을 인원수에 맞게 분배하여 저장합니다. (분배 로직은 자유롭게 구현해 주세요.)  
○ token은 3자리 문자열로 구성되며 예측이 불가능해야 합니다.  
   

2. 받기 API ● 다음 조건을 만족하는 받기 API를 만들어 주세요.  
○ 뿌리기 시 발급된 token을 요청값으로 받습니다.   
○ token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를 API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다.   
○ 뿌리기 당 한 사용자는 한번만 받을 수 있습니다.  
○ 자신이 뿌리기한 건은 자신이 받을 수 없습니다.   
○ 뿌린기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.   
○ 뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기 실패 응답이 내려가야 합니다.   

3. 조회 API ● 다음 조건을 만족하는 조회 API를 만들어 주세요.  
○ 뿌리기 시 발급된 token을 요청값으로 받습니다.   
○ token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다. 현재 상태는 다음의 정보를 포함합니다.   
○ 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은 사용자 아이디] 리스트)   
○ 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지 않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.   
○ 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.   
 
 
