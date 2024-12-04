<template>
  <div>SubPage1</div>

  <table>
    <tbody>
      <tr>
        <td>
          <span> id : 1 </span>
        </td>
        <td>
          <DesignButton @click="fetchApi(1)">클릭</DesignButton>
        </td>
      </tr>
      <tr>
        <td>
          <input v-model="val" placeholder="id 입력" />
        </td>
        <td>
          <DesignButton @click="fetchApi(val)">클릭</DesignButton>
        </td>
      </tr>
    </tbody>
  </table>

  <div v-if="loading">Loading...</div>
  <div v-else-if="error">{{ error }}</div>
  <div v-else>
    <table>
      <tbody>
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
      </tbody>
    </table>
  </div>
</template>

<style scoped>
span,
input {
  display: inline-block;
  width: 100px;
  height: 30px;
  padding: 0 5px 0 5px;
}
</style>

<script>
export default {
  name: 'SubPage1',

  data() {
    return {
      apiData: null,
      loading: true,
      error: null,
      val: null,
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
        alert('해당 값이 없습니다.')
        console.error('[SubPage1] API ERROR:', e)
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
