<template >
      <div>
    <div class="row justify-content-center">
      <div class="col-xl-10 col-lg-12 col-md-9">
        <div class="card mt-5">
          <div class="card-body p-0">
            <!-- {/* Nested Row within Card Body */} -->
            <div class="row">
              <div class="col-lg-6 bg-login-image"></div>
              <div class="col-lg-6">
                <div class="p-5">
                  <div class="text-center">
                    <h1 class="h4 mb-4">Welcome Back!</h1>
                  </div>
                  <form class="user" @submit.prevent="handleLogin"> <!-- 이번에 sunbmit으로 해보자 , prevent를 사용해서 페이지 이동 등을 억제할 수 있다.-->
                    <div class="form-group">
                      <input
                        type="email"
                        class="form-control form-control-user mb-3"
                        placeholder="Email"
                        name="email"
                        v-model="user.email"
                      />
                    </div>
                    <div class="form-group">
                      <input
                        type="password"
                        class="form-control form-control-user mb-3"
                        id="exampleInputPassword"
                        placeholder="Password"
                        name="password"
                        v-model="user.password"
                      />
                    </div>

                    <button class="btn btn-primary btn-user w-100 mb-3">
                      Login
                    </button>
                    <hr />
                    <a href="/" class="btn btn-google btn-user w-100 mb-2">
                      <i class="fab fa-google fa-fw"></i>&nbsp;Login with Google
                    </a>
                    <a href="/" class="btn btn-naver btn-user w-100 mb-2">
                      <i class="fa-solid fa-n"></i>&nbsp;Login with Naver
                    </a>
                    <a href="/" class="btn btn-kakao btn-user w-100 mb-3">
                      <i class="fa-solid fa-k"></i>&nbsp;Login with Kakao
                    </a>
                  </form>
                  <hr />
                  <div class="text-center">
                    <a class="small" href="/forgot-password">
                      Forgot Password?
                    </a>
                  </div>
                  <div class="text-center">
                    <a class="small" href="/register"> Create an Account! </a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import AuthService from '../../services/auth/AuthService';
export default {
    data() {
      return {
        user:{
          email: "",
          password: "",
        }
      }
    },
    methods: {
      async handleLogin(){
        // 공통 로그인 서비스 함수
        // 로그인 성고 =>
        // 로그인 실해 => 로그인 실패 공유함수 실행
        try {
          let response = await AuthService.login(this.user);
          console.log(response);
          localStorage.setItem("user", JSON.stringify(response.data)); // fh칼호스트는 객체를 저장할 수 없기에 이걸 문자열러 바꿔서 진행해야한다.
          this.$store.commit("loginSuccess");
          this.$$router.push("/");
        } catch (e) {
          // 로그인 실패시 에러가 뜨므로 로그인 실패 공유함수를 실행
          this.$store.commit("loginFailure"); // 공유함수의 mutation함수는commit으로 실행한다.
          console.log(e);
        }
      }
    },
    created() {
      // dept와 emp사이에서는 서로 접근이 불가능하여서(의존성을 낮추기 위해) 속성을 공유해서 사용할 수 없다. 그래서 풀옵스와 공유저장소를 이용해서 통신해야한다.
      // vue의 공유저장소인 vuex를 사용하자, 만약 vuex에 로그인이 true이면 로그인이 되어있는상태이므로 login을 할 필요가 없다. 그래서 강제로 home으로 이동시킨다.
      if (this.$store.state.loggedIn) {
        this.$router.push("/"); // 로그인이 되어있으므로 강제이동
      }
    },
}
</script>
<style >
    @import "@/assets/css/login.css";
</style>