import React, { useEffect, useState } from "react";
import "./Me.css"

export default function Me() {
    const [user, setUser] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch(`${import.meta.env.VITE_API_URL}/api/v1/me`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
        })
            .then(async (res) => {
                if (!res.ok) {
                    const err = await res.json();
                    throw new Error(err.message || "Ошибка при получении данных пользователя");
                }
                return res.json();
            })
            .then((data) => setUser(data))
            .catch((err) => setError(err.message))
    }, []);

    if (error) return <div className="error-message">{error}</div>;
    if (!user) return <div>Загрузка...</div>; // ждем данные

    return (
        <div className="me-page">
            <h2>literally me:</h2>
            <p>
                <strong>Имя:</strong> {user.name}
            </p>
            <p>
                <strong>Роль:</strong> {user.role}
            </p>
        </div>
    );
}
