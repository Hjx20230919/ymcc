import Login from './views/itsource/Login.vue'
import Register from './views/itsource/Register.vue'
import NotFound from './views/404.vue'
import Home from './views/Home.vue'
import Systemdictionary from './views/itsource/Systemdictionary.vue'
import Systemdictionaryitem from './views/itsource/Systemdictionaryitem.vue'
import CourseVideo from './views/itsource/CourseVideo.vue'
import CourseType from './views/itsource/CourseType.vue'
import CourseChapter from './views/itsource/CourseChapter.vue'
import Department from './views/itsource/department.vue'
import Course from './views/itsource/Course.vue'
import Pager from './views/itsource/Pager.vue'
import Role from './views/itsource/role.vue'
import echarts from './views/charts/echarts.vue'
import Teacher from './views/itsource/Teacher.vue'
import CouseOrder from './views/itsource/CouseOrder.vue'
import PayFlow from './views/itsource/PayFlow.vue'
import AliPay from './views/itsource/AliPay.vue'
import SMSMessage from './views/itsource/SMSMessage.vue'
import SMSBlack from "./views/itsource/SMSBlack";
import StationMessage from './views/itsource/StationMessage.vue'
import EmailMessage from './views/itsource/EmailMessage'
import Employee from './views/itsource/Employee'
import Table from './views/itsource/Table'
import Permission from './views/itsource/Permission'
import CourseKill from './views/itsource/CourseKill'
import KillActivity from './views/itsource/KillActivity'


let routes = [
    {
        path: '/login',
        component: Login,
        name: '',
        hidden: true
    },
    {
        path: '/register',
        component: Register,
        name: '',
        hidden: true
    },
    {
        path: '/404',
        component: NotFound,
        name: '',
        hidden: true
    },
    {
        path: '/table',
        component: Table,
        name: '',
        hidden: true
    },
    //{ path: '/main', component: Main },
    {
        path: '/',
        component: Home,
        name: '首页',
        leaf: true,//只有一个节点
        iconCls: 'el-icon-s-home',
        children: [
            { path: '/echarts', component: echarts, name: '首页' }
        ]
    },
    {
        path: '/',
        component: Home,
        name: '组织机构管理',
        iconCls: 'el-icon-s-grid',//图标样式class
        children: [
            { path: '/employee', component: Employee, name: '员工管理' },
            { path: '/department', component: Department, name: '部门管理' },
            { path: '/role', component: Role, name: '角色管理' },
            { path: '/permission', component: Permission, name: '权限管理' },
        ]
    }
    ,
    {
        path: '/',
        component: Home,
        name: '系统基础设置',
        iconCls: 'el-icon-s-tools',//图标样式class
        children: [
            { path: '/systemdictionary', component: Systemdictionary, name: '数据字典' },
            { path: '/systemdictionaryitem', component: Systemdictionaryitem, name: '数据字典明细' },
        ]
    },
    {
        path: '/',
        component: Home,
        name: '课程相关',
        iconCls: 'el-icon-video-camera-solid',
        children: [
            { path: '/courseType', component: CourseType, name: '课程类型' },
            { path: '/teacher', component: Teacher, name: '讲师管理' },
            { path: '/course', component: Course, name: '课程管理' },
            { path: '/courseChapter', component: CourseChapter, name: '课程章节' },
            { path: '/courseVideo', component: CourseVideo, name: '课程视频' }

        ]
    },
    {
        path: '/',
        component: Home,
        name: '订单中心',
        iconCls: 'el-icon-s-order',
        children: [
            { path: '/couseOrder', component: CouseOrder, name: '课程订单' },
            { path: '/payFlow', component: PayFlow, name: '支付账单' },
            { path: '/AliPay', component: AliPay, name: '支付宝设置' },

        ]
    },
    {
        path: '/',
        component: Home,
        name: '秒杀中心',
        iconCls: 'el-icon-s-goods',
        children: [
            { path: '/killActivity', component: KillActivity, name: '秒杀活动' },
            { path: '/kourseKill', component: CourseKill, name: '秒杀课程' }

        ]
    },
    {
        path: '/',
        component: Home,
        name: '消息中心',
        iconCls: 'el-icon-message-solid',
        children: [
            { path: '/SMSMessage', component: SMSMessage, name: '短信管理' },
            { path: '/SMSBlack', component: SMSBlack, name: '黑名单管理' },
            { path: '/emailMessage', component: EmailMessage, name: '邮件管理' },
            { path: '/stationMessage', component: StationMessage, name: '站内信管理' },

        ]
    },
    {
        path: '/',
        component: Home,
        name: '页面管理',
        iconCls: 'el-icon-s-home',
        children: [
            { path: '/pager', component: Pager, name: '静态化页面管理' },
            { path: '/site', component: Pager, name: '站点管理' }
        ]
    },

    {
        path: '*',
        hidden: true,
        redirect: { path: '/404' }
    }
];

export default routes;