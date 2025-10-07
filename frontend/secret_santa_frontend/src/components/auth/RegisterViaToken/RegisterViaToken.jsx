import React, { useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import "./RegisterViaToken.css";

export default function RegisterViaToken() {
    const [searchParams] = useSearchParams();
    const token = searchParams.get("token");
    const navigate = useNavigate();

    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [response, setResponse] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        if (password !== confirmPassword) {
            setError("Пароли не совпадают");
            return;
        }

        try {
            setLoading(true);
            const formData = new URLSearchParams();
            formData.append("token", token);
            formData.append("password", password);

            const res = await fetch(`/api/v1/auth/register`, {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: formData.toString(),
            });

            if (!res.ok) {
                const data = await res.json();
                throw new Error(data.message || "Ошибка регистрации");
            }

            const data = await res.json();
            setResponse(data);
            setTimeout(() => navigate("/login"), 2500);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="register-token-page">
            <div className="register-card">
                {!response ? (
                    <>
                        <h2>Регистрация по приглашению</h2>
                        {token ? (
                            <form onSubmit={handleSubmit}>
                                <div className="form-field">
                                    <label>Пароль</label>
                                    <input
                                        type="password"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        required
                                    />
                                </div>
                                <div className="form-field">
                                    <label>Подтверждение пароля</label>
                                    <input
                                        type="password"
                                        value={confirmPassword}
                                        onChange={(e) => setConfirmPassword(e.target.value)}
                                        required
                                    />
                                </div>

                                {error && <p className="error">{error}</p>}

                                <button type="submit" disabled={loading}>
                                    {loading ? "Регистрируем..." : "Зарегистрироваться"}
                                </button>
                            </form>
                        ) : (
                            <p className="error">Некорректная ссылка приглашения</p>
                        )}
                    </>
                ) : (
                    <>
                        <h2>Регистрация успешна!</h2>
                        <p>Ваш логин: <b>{response.login}</b></p>
                        <p>{response.message}</p>
                        <p className="redirect-msg">Сейчас вы будете перенаправлены на страницу входа...</p>
                    </>
                )}
            </div>
        </div>
    );
}
