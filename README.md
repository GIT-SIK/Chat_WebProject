# Vue
* #### 참고 사이트
    https://vuejs.org/guide/quick-start.html <br>
    https://kdevkr.github.io/spring-boot-integration-vuejs/
  
* #### Vue CLI 설치 및
```
npm install -g @vue/cli
```
* #### Project Create
```
npm create vue@latest
```
* #### Project Start
```
npm run serve
npm run dev
```
* #### Project Build
```
npm run build
```

* #### CDN Vue
```
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
```

# json-server

* #### Json-server Start
```
json-server --watch file.json [--port 3001]
```

* #### 패키지 (package.json) 내 터미널 실행
```
"scripts": { <br>
  "db": "json-server --watch file.json [--port 3001]" <br>
}
```
```
npm run db
```
  
