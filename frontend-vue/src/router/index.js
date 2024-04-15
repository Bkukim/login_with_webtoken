import { createRouter, createWebHistory } from 'vue-router'

const routes = [
 
  {
    path: '/',
    
    component: () => import( '../views/HomeView.vue')
  },
  {
    path: '/',
    alias: "/dept", // 추가 url을 생성할 수 있다. 이렇게 되면 두 url을 실행해도 하나의 페이지가 나온다.
    component: () => import("../views/basic/dept/DeptList.vue")
  },
 {
    path: '/emp',   
    component: () => import("../views/basic/emp/EmpList.vue")
  },
  {
    path: '/add-dept',   
    component: () => import("../views/basic/dept/AddDept.vue")
  },
  {
    path: '/add-emp',   
    component: () => import("../views/basic/emp/AddEmp.vue")
  },
  {
    path: '/dept/:dno',   
    component: () => import("../views/basic/dept/DeptDetail.vue")
  }, 
  {
    path: '/register',   
    component: () => import("../views/auth/RegisterView.vue")
  }, 
  {
    path: '/login',   
    component: () => import("../views/auth/LoginView.vue")
  }, 
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
