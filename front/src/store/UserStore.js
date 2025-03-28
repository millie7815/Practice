import { atom } from "jotai";

export const isAuth = atom(false);

export async function registerUser(data){
    const response = await UserAPI.register(data);
    console.log(response);
}

//Проверяем аутентификацию, и если все верно(response 200 с чем-то, если данные верны и тп), то возвращаем тру
export async function loginUser(data){
    const response = await UserAPI.login(data);
    if(response.status>=200 && response.status<300){
       return true;
    }
    console.log(response);
}
