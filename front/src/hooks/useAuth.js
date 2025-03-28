import { useState, useEffect } from 'react';
import apiClient from '../config/apiClient';

export const useAuth = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false); //isAuthenticated - состояние (true/false), показывающее, авторизован ли пользователь
  const [isLoading, setIsLoading] = useState(true); //isLoading - состояние загрузки проверки

    const checkAuth = async () => {
      try {
        await apiClient.get('/api/auth/check', { withCredentials: true });
        setIsAuthenticated(true);
      } catch {
        setIsAuthenticated(false);
      } finally {
        setIsLoading(false);
      }
    };

    const login = async () => {
      setIsLoading(true);
      try {
        await checkAuth(); // Проверяем статус после входа
      } finally {
        setIsLoading(false);
      }
    };
    

    const logout = async () => {
      try {
        await apiClient.post('/api/auth/logout', {}, { withCredentials: true });
        setIsAuthenticated(false);
        // Принудительно очищаем все данные
        window.location.href = '/login'; // Полный перезаход, можно без него
      } catch (error) {
        console.error('Logout failed:', error);
        throw error;
      }
    };

    useEffect(() => {
    checkAuth();
  }, []);

  return { isAuthenticated, isLoading, checkAuth, login, logout};
};

//    При монтировании компонента отправляется GET-запрос к /api/auth/check
// withCredentials: true означает, что куки будут автоматически прикреплены к запросу
// Если запрос успешен (статус 200) - пользователь авторизован
// Если ошибка (статус 401) - не авторизован
// В любом случае после проверки isLoading становится false