import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Home from '@/components/Home'
import projectManage from '@/components/system/projectManage'
import userManage from '@/components/system/userManage'
import roleManage from '@/components/system/roleManage'
import operationManage from '@/components/baseCenter/operationManager'
import operationEdit from '@/components/baseCenter/operationEdit'
import fileManage from '@/components/baseCenter/fileManager'
import paramManage from '@/components/baseCenter/paramsManage'
import functionManage from '@/components/baseCenter/functionManage'
import functionEdit from '@/components/baseCenter/functionEdit'
import engineManage from '@/components/envManager/engineManage'
import deviceManage from '@/components/envManager/deviceManage'
import androidController from '@/components/envManager/device/androidController'
import envManage from '@/components/envManager/envManage'
import apiManage from '@/components/caseManage/apiManage'
import elementManage from '@/components/caseManage/elementManage'
import caseManage from '@/components/caseManage/caseManage'
import controlManage from '@/components/caseManage/controlManage'
import settingManage from '@/components/settingCenter/settingManage'
import caseApiEdit from '@/components/caseManage/caseAdd/apiAdd'
import apiEdit from '@/components/caseManage/apiEdit'
import caseWebEdit from '@/components/caseManage/caseAdd/webAdd'
import caseAppEdit from '@/components/caseManage/caseAdd/appAdd'
const testCollection= () => import('@/components/planCenter/testCollection')
const testPlan= () => import('@/components/planCenter/testPlan')
const collectionEdit=()=> import('@/components/planCenter/collectionEdit')
const planEdit=()=> import('@/components/planCenter/planEdit')
const reportDetail=()=> import('@/components/report/reportDetail')
const testReport=()=> import('@/components/report/testReport')
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: {
        requireAuth: false
      },
    },
    {
      path: '/',
      name: 'home',
      component: Home,
      meta: {
        requireAuth: true
      },
      children: [
        {
          path: '/system/projectManage',
          name: 'projectManage',
          component: projectManage,
          meta: {
            requireAuth: true,
            permission: null
          }
        },
        {
          path: '/system/userManage',
          name: 'userManage',
          component: userManage,
          meta: {
            requireAuth: true,
            permission: null
          }
        },
        {
          path: '/caseCenter/apiManage',
          name: '接口管理',
          component: apiManage,
          meta: {
              requirePerm: "NORMAL_MENU",
              requireAuth: true
          }
        },
        {
          path: '/caseCenter/controlManage',
          name: '控件管理',
          component: controlManage,
          meta: {
              requirePerm: "NORMAL_MENU",
              requireAuth: true
          }
      },
        {
          path: '/caseCenter/elementManage',
          name: '接口管理',
          component: elementManage,
          meta: {
              requirePerm: "NORMAL_MENU",
              requireAuth: true
          }
        },
        {
          path: '/system/roleManage',
          name: 'roleManage',
          component: roleManage,
          meta: {
            requireAuth: true,
            permission: null
          }
        },
        {
          path: '/baseCenter/function/edit/:functionId',
          name: 'functionEdit',
          component: functionEdit,
          meta: {
            requireAuth: true,
            permission: "NORMAL_MENU"
          }

        },
        {
          path: '/baseCenter/function/add',
          name: 'functionEdit',
          component: functionEdit,
          meta: {
            requireAuth: true,
            permission: "NORMAL_MENU"
          }
        },
        {
          path: '/baseCenter/functionManage',
          name: 'functionManage',
          component: functionManage,
          meta: {
            requireAuth: true,
            permission: "NORMAL_MENU"
          }
        },
        {
          path: '/caseCenter/caseManage',
          name: '用例管理',
          component: caseManage,
          meta: {
              requirePerm: "NORMAL_MENU",
              requireAuth: true
          }
      },
      {
        path: '/caseCenter/caseManage/apiCase/edit/:caseId',
        name: 'API用例编辑',
        component: caseApiEdit,
        meta: {
            requirePerm: "NORMAL_MENU",
            requireAuth: true
        }
    },{
        path: '/caseCenter/caseManage/apiCase/add',
        name: 'API用例新增',
        component: caseApiEdit,
        meta: {
            requirePerm: "NORMAL_MENU",
            requireAuth: true
        }
      },{
        path: '/caseCenter/caseManage/webCase/edit/:caseId',
        name: 'WEB用例编辑',
        component: caseWebEdit,
        meta: {
            requirePerm: "NORMAL_MENU",
            requireAuth: true
        }
    },{
        path: '/caseCenter/caseManage/webCase/add',
        name: 'WEB用例新增',
        component: caseWebEdit,
        meta: {
            requirePerm: "NORMAL_MENU",
            requireAuth: true
        }
    },{
          path: '/baseCenter/operationManage',
          name: 'operationManage',
          component: operationManage,
          meta: {
            requireAuth: true,
            permission: null
          }
        },
        {
          path: '/baseCenter/operation/:uiType/add/:operationType',
          name: 'operationEdit',
          component: operationEdit,
          meta: {
            requireAuth: true,
            permission: null
          }
        },
        {
          path: '/baseCenter/fileManage',
          name: 'fileManage',
          component: fileManage,
          meta: {
            requireAuth: true,
            permission: null
          }
        },
        
        {
          path: '/envCenter/engineManage',
          name: 'engineManage',
          component: engineManage,
          meta: {
            requireAuth: true,
            permission: "NORMAL_MENU"
          }
        },
        {
          path: '/envCenter/deviceManage',
          name: 'deviceManage',
          component: deviceManage,
          meta: {
            requireAuth: true,
            permission: "NORMAL_MENU"
          }
        },
        {
          path: '/envCenter/androidController',
          name: 'androidController',
          component: androidController,
          meta: {
            requireAuth: true,
            permission: "NORMAL_MENU"
          }
        },
        {
          path: '/envCenter/envManage',
          name: ' envManage',
          component:  envManage,
          meta: {
            requireAuth: true,
            permission: "NORMAL_MENU"
          }
        },
        {
          path: '/settingCenter/settingManage',
          name: 'settingManage',
          component: settingManage,
          meta: {
            requireAuth: true,
            permission: "SETTING_MENU"
          }
        },
        
        {
          path: '/baseCenter/paramManage',
          name: 'paramManage',
          component: paramManage,
          meta: {
            requireAuth: true,
            permission: null
          }
        },
        {
          path: '/caseCenter/caseManage/appCase/:system/add',
          name: 'APP用例新增',
          component: caseAppEdit,
          meta: {
              requirePerm: "NORMAL_MENU",
              requireAuth: true
          }
      },
        {
          path: '/baseCenter/operation/:uiType/edit/:operationId',
          name: 'operationEdit',
          component: operationEdit,
          meta: {
            requireAuth: true,
            permission: "NORMAL_MENU"
          }
        },
        {
          path: '/caseCenter/apiManage/add/:nodeID?',
          name: 'apiEdit',
          component: apiEdit,
          meta: {
            requireAuth: true,
            permission: "NORMAL_MENU"
          }
        },
        {
          path: '/caseCenter/apiManage/edit/:apiId',
          name: '接口编辑',
          component: apiEdit,
          meta: {
              requirePerm: "NORMAL_MENU",
              requireAuth: true
          }
      },{
        path: '/planManage/testCollection',
        name: '测试集合',
        component: testCollection,
        meta: {
            requirePerm: "NORMAL_MENU",
            requireAuth: true
        }
      },{
        path: '/planManage/testCollection/add',
        name: '测试集合新增',
        component: collectionEdit,
        meta: {
            requirePerm: "NORMAL_MENU",
            requireAuth: true
        }
    },{
      path: '/planManage/testCollection/edit/:collectionId',
      name: '测试集合编辑',
      component: collectionEdit,
      meta: {
          requirePerm: "NORMAL_MENU",
          requireAuth: true
      }
  },
    {
      path: '/planManage/testPlan',
      name: '测试计划',
      component: testPlan,
      meta: {
          requirePerm: "NORMAL_MENU",
          requireAuth: true
      }
  },{
    path: '/planManage/testPlan/add',
    name: '测试计划新增',
    component: planEdit,
    meta: {
        requirePerm: "NORMAL_MENU",
        requireAuth: true
    }
},{
    path: '/planManage/testPlan/edit/:planId',
    name: '测试计划编辑',
    component: planEdit,
    meta: {
        requirePerm: "NORMAL_MENU",
        requireAuth: true
    }
  },{
    path: '/report/testReport',
    name: '测试报告',
    component: testReport,
    meta: {
        requirePerm: "NORMAL_MENU",
        requireAuth: true
    }},{
    path: '/report/testReport/detail/:reportId',
    name: '报告详情',
    component: reportDetail,
    meta: {
        requirePerm: "NORMAL_MENU",
        requireAuth: true
    }
},
      ]
    }
    
  ]
})
