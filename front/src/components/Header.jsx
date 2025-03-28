// components/Header.js
import React, { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/Header.css';
import useUserStore from '../store/UserStore';

const Header = () => {
  const navigate = useNavigate();
  // Берем нужные данные из хранилища
  const { isAuth, isLoading, checkAuth, logout } = useUserStore();

  // Проверяем авторизацию при загрузке
  useEffect(() => {
    checkAuth();
  }, [checkAuth]);

  const handleLogout = async () => {
    try {
      await logout();
      navigate('/'); // Переходим на главную
    } catch (error) {
      console.error('Ошибка при выходе:', error);
    }
  };

  // Показываем заглушку при загрузке
  if (isLoading) {
    return <div className="header-loading">Загрузка...</div>;
  }

  return (
    <header className="header">
      <div className="logo">Логотип</div>
      <nav>
        <ul>
          <li><Link to="/">Главная</Link></li>
          
          {/* Если НЕ авторизован - показываем регистрацию/вход */}
          {!isAuth ? (
            <>
              <li><Link to="/register">Регистрация</Link></li>
              <li><Link to="/login">Вход</Link></li>
            </>
          ) : (
            <>
              {/* Если авторизован - показываем профиль и кнопку выхода */}
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