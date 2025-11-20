import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import './Login.css';

export default function Login() {
    const navigate = useNavigate();
    const [name, setName] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState(null);

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name, password }),
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('token', data.token);
                localStorage.setItem('name', name);
                setMessage({ text: 'Вход выполнен успешно!', type: 'success' });
                navigate('/home')
            } else {
                const errorData = await response.json();
                setMessage({ text: errorData.message || 'Ошибка входа', type: 'error' });
            }
        } catch  {
            setMessage({ text: 'Ошибка входа', type: 'error' });
        }
    };

    return (
        <div className="login-page">
        <div className="login-container">
            <div className="login-card">
                <div className="login-header">Тайный Санта</div>

                <form onSubmit={handleLogin}>
                    <label htmlFor="name">Логин:</label>
                    <input
                        type="text"
                        id="name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />

                    <label htmlFor="password">Пароль:</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />

                    <button type="submit">Войти</button>
                </form>

                {message && (
                    <div className={`message ${message.type}`}>
                        {message.text}
                    </div>
                )}
            </div>
        </div>
        </div>
    );
}
