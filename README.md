## Vue
### 참고 사이트
> https://vuejs.org/guide/quick-start.html <br>
> https://kdevkr.github.io/spring-boot-integration-vuejs/
  
### 프로젝트 의존성
> @eslint/js@9.15.0 <br>
> @vitejs/plugin-vue@5.2.1 <br>
> @vue/eslint-config-prettier@10.1.0 <br>
> axios@1.7.8 <br>
> concurrently@9.1.0 <br>
> eslint-plugin-vue@9.31.0 <br>
> eslint@9.15.0 <br>
> prettier@3.4.1 <br>
> vite-plugin-vue-devtools@7.6.7 <br>
> vite@5.4.11 <br>
> vue-router@4.5.0 <br>
> vue@3.5.13 <br>
> vuetify@3.7.4 <br>
> @stomp/stompjs@^7.0.0 <br>
> sockjs-client@1.6.1 <br>

## Vue 시작하기

#### Vue CLI 설치 및 시작
```
npm install -g @vue/cli
```

#### Project Create
```
npm create vue@latest
```

#### Project Start
```
npm run serve
npm run dev
```

> **(package.json) Vue CLI**
>```
> "scripts": {
>  "serve": "vue-cli-service serve",
>  "build": "vue-cli-service build",
>  "lint": "vue-cli-service lint"
> }
>```
> **(package.json) Vite**
> ```
> "scripts": {
>  "dev": "vite",
>  "build": "vite build",
>  "preview": "vite preview"
> }
> ```

#### Project Build
```
npm run build
```

#### CDN Vue
```
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
```


- - - - -

## json-server

#### Json-server Start
```
json-server --watch file.json [--port 3001]
```
```
npm run db
```

#### Json-server + vite
```
npm install concurrently --save-dev
```

#### (package.json) 프로젝트와 동작 설정
```
"scripts": {
  "dev": "concurrently \"json-server --watch db.json --port 3001\" \"vite\""
}
```
