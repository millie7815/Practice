//config
import RegisterPage from "../pages/RegisterPage";
import HomePage from "../pages/HomePage";
import LoginPage from "../pages/LoginPage"; 
export const publicRoutes= [
    {
        title:"Главная",
        path: "/",
        page: HomePage
    }
];

export const authRoutes= [
    {
        title:"Регистрация",
        path: "/register",
        page: RegisterPage
    },
    {
        title:"Вход",
        path: "/login",
        page: LoginPage
    }
];

export const privateRoutes= [
    {
        title:"Профиль",
        path: "/profile",
        page: HomePage
    }
];