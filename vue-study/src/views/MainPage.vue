<template>
  <div>
    <!-- 카드 -->
    <div class="container" style="margin: 50px; text-align: center">
      <div class="row row-cols-1 row-cols-md-3 g-4">
        <div class="col">
          <div class="card shadow-sm h-100">
            <div class="card-body">
              <h5 class="card-title">Card1</h5>
              <p class="card-text">Text1</p>
            </div>
          </div>
        </div>
        <div class="col">
          <div class="card shadow-sm h-100">
            <div class="card-body">
              <h5 class="card-title">Card2</h5>
              <p class="card-text">Text2</p>
            </div>
          </div>
        </div>
        <div class="col">
          <div class="card shadow-sm h-100">
            <div class="card-body">
              <h5 class="card-title">Card3</h5>
              <p class="card-text">Text3</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 슬라이드 -->
    <div id="carouselExample" class="carousel slide mb-5" data-bs-ride="carousel">
      <div class="carousel-inner">
        <div class="carousel-item active">
          <!-- <img
            src="https://via.placeholder.com/1920x500/f8f9fa/000000"
            class="d-block w-100"
            alt="Slide 1"
          /> -->
          <div class="d-flex justify-content-center align-items-center">
            <h1 style="font-size: 30px">슬라이드 텍스트1</h1>
          </div>
        </div>
        <div class="carousel-item">
          <div class="d-flex justify-content-center align-items-center">
            <h1 style="font-size: 30px">슬라이드 텍스트2</h1>
          </div>
        </div>
        <div class="carousel-item">
          <div class="d-flex justify-content-center align-items-center">
            <h1 style="font-size: 30px">슬라이드 텍스트3</h1>
          </div>
        </div>
      </div>
      <button
        class="carousel-control-prev"
        type="button"
        data-bs-target="#carouselExample"
        data-bs-slide="prev"
      >
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
      </button>
      <button
        class="carousel-control-next"
        type="button"
        data-bs-target="#carouselExample"
        data-bs-slide="next"
      >
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MainPage',
}
</script>

<style scoped>
.carousel-item div {
  height: 500px;
  background-color: #f8f9fa;
}

.card-title {
  font-size: 1.25rem;
  font-weight: 600;
}
.card-text {
  font-size: 1rem;
  color: #6c757d;
}
</style>
