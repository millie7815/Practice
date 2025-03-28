import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/Header.css';
import apiClient from '../config/apiClient'; 
import { useAuth } from '../hooks/useAuth'; //потому что проверка нужна авторизированного ползователя

const Header = () => {
  const navigate = useNavigate(); //Получает из хука useAuth:
  const { isAuthenticated, isLoading, logout} = useAuth(); //isAuthenticated (булево значение - авторизован ли пользователь), isLoading (идет ли проверка авторизации)


  const handleLogout = async () => {
    try {
      await apiClient.post('/api/auth/logout', {}, { withCredentials: true }); //Отправляет запрос на сервер для выхода
      navigate('/');
      window.location.reload(); // Полностью обновляем страницу
    } catch (error) {
      console.error('Ошибка при выходе:', error);
    }
  };

  if (isLoading) {
    return <div className="header-loading">Загрузка...</div>; // Лучше показывать заглушку 
  }

  return (
    <header className="header">
      <div className="logo">Логотип</div>
      <nav>
        <ul>
          <li><Link to="/">Главная</Link></li>
          
          {!isAuthenticated ? (
            <>
              <li><Link to="/register">Регистрация</Link></li>
              <li><Link to="/login">Авторизация</Link></li>
            </>
          ) : (
            <>
              <li><Link to="/profile">Профиль</Link></li>
              <li>
                <button onClick={handleLogout} className="logout-button">
                  Выйти
                </button>
              </li>
            </>
          )}
        </ul>
      </nav>
    </header>
  );
};

export default Header;