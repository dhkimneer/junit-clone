# junit-clone
springboot, junit project

중간에 bridge가 하나 필요(테스트 서버)
테스트 서버는 실제 배포 서버와 환경이 같아야 함.

[테스트 서버 환경에서 하는 것]
1. 테스트하고
2. jar file로 변경

[과정]
1. project를 github upload
2. project를 test server로 던지기
3. test server(1, 2번 과정 실행). 이 때 작동해야 될 profile은 dev
 - 테스트 통과 못 하면 로컬에서 수정 후 테스트하면 됨.
4. jar file을 실제 배포 서버에 던진다.
5. 실행할 때는 java -jar prod filename
 - spring datasource RDS 부분이 prod 기준으로 설정됨.
 - 연결될 DB를 미리 생성해놔야 함.