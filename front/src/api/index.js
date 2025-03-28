// src/api/index.js
import apiClient from '../config/apiClient';
import UserAPI from './UserAPI';

export const registerUser = UserAPI.registerUser; // Исправили экспорт
export const login = UserAPI.login; // Это уже правильно
