// MainPage.jsx
import React from 'react';
import '../styles/Main.css';

const MainPage = () => {
  return (
    <div className="main-page">
      <h1>Добро пожаловать в личный кабинет!</h1>
      <p>Вы успешно авторизованы</p>
      {/* Добавьте сюда контент для авторизованных пользователей */}
    </div>
  );
};

export default MainPage;