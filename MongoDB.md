### Mongo DB 설명서 (Docker 기준)

#### Docker 내부
##### Mongo 컨테이너 생성
```
인증 X : docker run --name mongodb-container -d -p 27017:27017 mongo
인증 O : docker run --name mongodb-container -d -p 27017:27017 mongo --auth
```

##### Mongo shell 접속
```
비로그인 및 최초 1회: docker exec -it mongodb-container mongosh
로그인 : docker exec -it mongodb-container mongosh -u [username] -p [password] --authenticationDatabase [DB명]
```

► 인증 설명 <br>
인증 모드가 켜져있을 경우 비로그인 상태로 접속하여 admin 계정을 생성한다. <br>
이후 일반 유저 생성, 데이터 베이스 생성을 한다. <br>
이 경우 비로그인 상태로 접속은 되나 유저를 생성할 경우 권한 오류로 생성할 수 없다. <br>
인증 모드가 꺼져있을 경우 비로그인 상태로 접속하여 일반 유저 및 데이터베이스 생성을 한다. <br>
이 경우 비로그인 상태로 접속을 하더라도 최고 권한을 가지게 된다.

#### Mongo DB 내부 
##### Mongo DB 명령어
- 데이터 베이스 생성 및 이동
```
use DB명
```

- 유저 생성
```
db.createUser({user: "[username]", pwd: "[password]", roles: [{ role: "[권한명]", db: "[DB명]" }]})
```

- 유저 삭제
```
db.dropUser([username])
```

- 유저 목록 및 검색
```
db.getUsers()
db.getUser([username])

```

- 현재 데이터 베이스 접속 확인
```
db.runCommand({ connectionStatus: 1 }) 
```

- 유저 권한 부여 및 제거
```
db.grantRolesToUser("[username]", [{ role: "[권한명]", db: "[DB명]" }])
```
```
db.revokeRolesFromUser("[username]", [{ role: "[권한명]", db: "[DB명]" }])
```

► 유저 권한 관련된 설명 <br>
최초 유저 생성시 use된 DB에 유저가 생성되기 때문에 해당 유저가 다른 데이터 베이스에 권한을 부여하기 위해선 <br>
최초 생성시 use 데이터베이스로 이동하여 권한을 부여야한다. <br>
그러므로 유저에게 권한 부여시 admin은 admin DB에서 작업하도록 한다.
