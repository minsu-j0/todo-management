>
# 원래 사용하던 github 계정의 2FA 보조인증키가 맥북 발열로 인해 부팅이 안되던 탓에 초기화하면서 사라져 새로 만든 임시 레포지토리로 공유드립니다.
![본 계정 커밋내역.png](https://github.com/minsu-j0/todo-management/blob/main/images/%EB%B3%B8%20%EA%B3%84%EC%A0%95%20%EC%BB%A4%EB%B0%8B%EB%82%B4%EC%97%AD.png?raw=true)
### ✔️ Checklist

> ⭕️ : 완료
> 
>🔺 : 보완 필요
> 
> ❌ : 미수행


> 
##### **Required**

| 완수 여부 | 내용       | 설명                                                                                                     |
|:-----:|:---------|:-------------------------------------------------------------------------------------------------------|
|  ⭕️   | 기능 명세 추출 | [이동](https://github.com/minsu-j0/todo-management/blob/main/%EA%B8%B0%EB%8A%A5%20%EB%AA%85%EC%84%B8.md) |
|  ⭕️   | 스키마 설계   | [이동](https://github.com/minsu-j0/todo-management/blob/main/Schema.md)                                  |
|  ⭕️   | 기술스텍 설정  | [이동](https://github.com/minsu-j0/todo-management/blob/main/%EC%82%AC%EC%9A%A9%20%EA%B8%B0%EC%88%A0.md) |
|  ⭕️   | 아키텍처 구성  | [이동](https://github.com/minsu-j0/todo-management/blob/main/images/Layer%20Flow.png)                    |
|  ⭕️   | 기능 개발    | [이동](https://github.com/minsu-j0/todo-management/tree/main/src)                                        |


##### **Optional**

| 완수 여부 | 내용                                          | 설명                                                                                                           |
|:-----:|:--------------------------------------------|:-------------------------------------------------------------------------------------------------------------|
|  ⭕️   | 간단한 설계문서                                    |                                                                                                              |
|  ⭕️   | ㄴ                                           | 동일 디렉토리에 있는 'api spec.html' 파일으로 다운로드 후 열어주시길 부탁드립니다.                                                        |
|  ⭕️   | ㄴ                                           | [코드 구조](https://github.com/minsu-j0/todo-management/blob/main/%EC%BD%94%EB%93%9C%20%EA%B5%AC%EC%84%B1.md)    |
|  ⭕️   | ㄴ                                           | [서비스 흐름도](https://github.com/minsu-j0/todo-management/blob/main/%ED%9D%90%EB%A6%84%EB%8F%84.md)              |
|  ⭕️   | ㄴ                                           | [추가 고려 사항](https://github.com/minsu-j0/todo-management/blob/main/%EA%B8%B0%EB%8A%A5%20%EB%AA%85%EC%84%B8.md) |
|   ❌   | (서버를 구성했다면) 해당 서버 사용 방법                     | 프로젝트 실행 방법을 아래에 작성 해 놨습니다. 확인 부탁드립니다.                                                                        |
|  ⭕️   | 주요 서비스 흐름에 따른 로깅                            | 사용자의 계정 활성화, 비활성화 여부, Todo 항목에 대한 생성 및 변경에 대한 내역을 DB 에 Log Table 을 만들어 저장                                    |
|  ⭕️   | 단위 테스트                                      | [링크](https://github.com/minsu-j0/todo-management/blob/main/images/unit%20test.png?raw=true)                  |
|   ❌   | 확장 가능한 코드 구성 또는 서버 아키텍처 구성 (간단한 설계문서 제출 필요) |                                                                                                              |
|   ❌   | TODO List 서비스를 사용할 수 있는 간단한 View 생성         |                                                                                                              |


 
##### 서버 실행 방법

1. source code clone 
2. 접근 할 외부 혹은 로컬 DB 에 'moais_todo_management' schema 생성
   - charset : utf8mb4
   - collation : utf8mb4_unicode_ci 
3. 환경변수 설정 후 Application 실행
   - Environment Variable(ex) url=localhost:3306;username=root;password=qwe123!@#;salt=moais ) 
     - url = db 주소 
     - username = db username
     - password = user 의 password
     - salt = password encoding 시 필요한 salt 키 임의 설정
