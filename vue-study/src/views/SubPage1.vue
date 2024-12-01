<template>
  <div>SubPage1</div>
  <button @click="fetchApi(1)">1</button>
  <button @click="fetchApi(2)">2</button>
  <div v-if="loading"></div>
  <div v-else-if="error">{{ error }}</div>
  <div v-else>
    <table>
      <tr>
        <td>id</td>
        <td>{{ apiData.id }}</td>
      </tr>
      <tr>
        <td>title</td>
        <td>{{ apiData.title }}</td>
      </tr>
      <tr>
        <td>content</td>
        <td>{{ apiData.content }}</td>
      </tr>
    </table>
  </div>
</template>

<script>
export default {
  name: 'SubPage1',

  data() {
    return {
      apiData: null,
      loading: true,
      error: null,
    }
  },

  methods: {
    async fetchApi(id) {
      this.loading = true
      this.error = null
      try {
        const res = await this.$api.get(`/api/${id}`)
        this.apiData = res.data
        console.log(this.apiData)
      } catch (e) {
        console.error('api error : ', e)
      } finally {
        this.loading = false
      }
    },
  },

  mounted() {
    this.fetchApi(1)
  },
}
</script>
