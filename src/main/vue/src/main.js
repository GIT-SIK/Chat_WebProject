
import Vue from 'vue'
import App from './App.vue'

new Vue({
    // render: createElement => createElement(App)
    render: h => h(App),
}).$mount('#app')

/* 부트스트랩 */
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(BootstrapVue)

/* 단일 이름 오류 무시 */
/*
vue는 파일 작성할 때 한 단어(ex index)가 아닌 두 단어 (ex myIndex) 합성어를 써야한다.
그 이유는 html하고 혼용하는 것을 막기 위해
하지만 공부하기 위해 단일 이름 오류 무시를 하도록 한다.
 */

//
// const { defineConfig } = require('@vue/cli-service')
// module.exports = defineConfig({
//     lintOnSave:false
// })
