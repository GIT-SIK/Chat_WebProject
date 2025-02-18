#### Mongo DB 설명서 (Docker 기준)

##### Mongo 컨테이너 생성
인증 X : docker run --name mongodb-container -d -p 27017:27017 mongo
인증 O : docker run --name mongodb-container -d -p 27017:27017 mongo --auth


##### Mongo shell 접속
비로그인 및 최초 1회: docker exec -it mongodb-container mongosh
로그인 : docker exec -it mongodb-container mongosh -u [username] -p [password] --authenticationDatabase [DB명]

* 인증 설명
인증 모드가 켜져있을 경우 비로그인 상태로 접속하여 admin 계정을 생성한다.
이후 일반 유저 생성, 데이터 베이스 생성을 한다.
이 경우 비로그인 상태로 접속은 되나 유저를 생성할 경우 권한 오류로 생성할 수 없다.

인증 모드가 꺼져있을 경우 비로그인 상태로 접속하여 일반 유저 및 데이터베이스 생성을 한다.
이 경우 비로그인 상태로 접속을 하더라도 최고 권한을 가지게 된다.

##### Mongo DB 명령어
- 데이터 베이스 생성 및 변경
```
use DB명
```

- 유저 생성
```
db.createUser({user: "[Uuername]", pwd: "[password]", roles: [{ role: "readWrite", db: "[DB명]" }]})
```

- 현재 데이터 베이스 권한 확인
```
db.runCommand({ connectionStatus: 1 }) 
```

