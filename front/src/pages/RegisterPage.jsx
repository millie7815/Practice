import React, { useState } from "react";
import { useNavigate } from 'react-router-dom';
import UserAPI from '../api/UserAPI';
import '../styles/RegisterPage.css'

const RegisterPage = () => {
  const navigate = useNavigate(); // Используем useNavigate для перенаправления
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    confirmPassword: '',
    email: ''
  });

  const [errors, setErrors] = useState({});

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (formData.password !== formData.confirmPassword) {
        setErrors({ confirmPassword: 'Пароли не совпадают' });
        return;
      }
      await UserAPI.registerUser(formData); 
      alert('Пользователь зарегистрирован');
      navigate('/login', { replace: true }); // Перенаправляем на страницу входа
      setFormData({
        username: '',
        password: '',
        confirmPassword: '',
        email: ''
      });
    } catch (error) {
      if (error.response) {
        setErrors(error.response.data);
      } else {
        console.error('Ошибка регистрации:', error);
      }
    }
  };

  return (
    <form onSubmit={handleSubmit} className="register-form">
      <h2>Регистрация</h2>
      <div className="form-group">
        <input
          type="text"
          value={formData.username}
          onChange={(e) => setFormData({ ...formData, username: e.target.value })}
          placeholder="Введите логин"
          className="form-control"
        />
        {errors.username && <div className="error-message">{errors.username}</div>}
      </div>

      <div className="form-group">
        <input
          type="password"
          value={formData.password}
          onChange={(e) => setFormData({ ...formData, password: e.target.value })}
          placeholder="Введите пароль"
          className="form-control"
        />
        {errors.password && <div className="error-message">{errors.password}</div>}
        <div className="password-hint">
        Пароль должен содержать минимум 8 символов, включая заглавную и строчную буквы, цифру и спец. символ (@#$%^&+=!)
        </div>
      </div>

      <div className="form-group">
        <input
          type="password"
          value={formData.confirmPassword}
          onChange={(e) => setFormData({ ...formData, confirmPassword: e.target.value })}
          placeholder="Подтвердите пароль"
          className="form-control"
        />
        {errors.confirmPassword && <div className="error-message">{errors.confirmPassword}</div>}
      </div>

      <div className="form-group">
        <input
          type="email"
          value={formData.email}
          onChange={(e) => setFormData({ ...formData, email: e.target.value })}
          placeholder="Введите email"
          className="form-control"
        />
        {errors.email && <div className="error-message">{errors.email}</div>}
      </div>

      <button type="submit" className="btn btn-primary">Зарегистрироваться</button>
    </form>
  );
};

export default RegisterPage;
