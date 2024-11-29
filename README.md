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



### (package.json) Vue CLI
```
"scripts": {
  "serve": "vue-cli-service serve",
  "build": "vue-cli-service build",
  "lint": "vue-cli-service lint"
 }
```
### (package.json) Vite
```
"scripts": {
  "dev": "vite",
  "build": "vite build",
  "preview": "vite preview"
}
```



# json-server

* #### Json-server Start
```
json-server --watch file.json [--port 3001]
```
```
npm run db
```


# json-server + vite
* #### Json-server + vite
```
npm install concurrently --save-dev
```

* #### 패키지 (package.json) 추가
```
"scripts": {
  "dev": "concurrently \"json-server --watch db.json --port 3001\" \"vite\""
}
```
