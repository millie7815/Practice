import React, { useState } from 'react';
import { useNavigate, Link, useLocation } from 'react-router-dom';
import apiClient from '../config/apiClient'; // Используем apiClient вместо login
import '../styles/LoginPage.css';

const LoginPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false); // Добавляем состояние загрузки

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);

    try {
      // Отправляем запрос через apiClient с withCredentials
      const response = await apiClient.post(
        '/api/auth/login',{ username, password }, { withCredentials: true });
      
      // Перенаправляем на предыдущую защищенную страницу, куда юзер хотел или на главную
      const from = location.state?.from?.pathname || '/';
      navigate(from, { replace: true });
      
    } catch (error) {
      // Обрабатываем ошибку
      setError(
        error.response?.data?.message || 
        error.message || 
        'Произошла ошибка при входе'
      );
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleSubmit} className="login-form">
        <h2>Вход</h2>
        {error && <div className="error-message">{error}</div>}

        <input 
          type="text" 
          value={username} 
          onChange={(e) => setUsername(e.target.value)}
          placeholder="Имя пользователя"
          required
          disabled={isLoading}
          autoComplete="username"
        />
        
        <input 
          type="password" 
          value={password} 
          onChange={(e) => setPassword(e.target.value)}
          placeholder="Пароль"
          required
          disabled={isLoading}
          autoComplete="current-password"
        />
        
        <button type="submit" disabled={isLoading}>
          {isLoading ? 'Вход...' : 'Войти'}
        </button>
        
        <div className="register-link">
          <Link to="/register">Зарегистрироваться</Link>
        </div>
      </form>
    </div>
  );
};

export default LoginPage;


