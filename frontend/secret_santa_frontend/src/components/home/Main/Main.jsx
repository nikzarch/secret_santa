import React, {useEffect, useState} from "react";
import "./Main.css";

export default function Main() {

    const [stats, setStats] = useState({
        users: "хз",
        activeEvents: "хз",
        totalEvents: "хз",
        groups: "хз",
    });

    const token = localStorage.getItem("token");

    useEffect(() => {
        const fetchStats = async () => {
            try {
                const res = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/stats`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                if (!res.ok) {
                    throw new Error("Ошибка загрузки статистики");
                }
                const data = await res.json();

                setStats({
                    users: data.number_of_registered_users,
                    activeEvents: data.number_of_active_events,
                    totalEvents: data.number_of_events,
                    groups: data.number_of_groups,
                });
            } catch (e) {
                console.error(e);
            }
        };

        fetchStats();
    }, [token]);

    return (
        <div className="main-page">
            <div className="main-header">
                <h1>Добро пожаловать на сайтик для игры в тайного санту! </h1>
                <p className="main-description">
                    Тайный санта - это игра, в которой вы дарите подарки случайным людям и получаете свой 🎅🏻
                </p>
            </div>

            <div className="main-instructions">
                <h2>Как начать</h2>
                <ul>
                    <li>Зайдите в "Группы", создайте её там и наприглашайте друзьяшек</li>
                    <li>Когда все приглашения будут приняты, зайдите в "События", создайте новое и нажмите "сгенерировать пары"</li>
                    <li>Всем участникам на вкладке события будет показан тот, кому они дарят и их пожелания</li>
                    <li>По окончанию события(дата указанная при создании), ваше событие будет деактивировано и пропадёт из списка</li>
                </ul>
            </div>

            <div className="main-stats">
                <div className="stat-card">
                    <div className="stat-number">{stats.users}</div>
                    <div className="stat-label">Зарегистрированных пользователей</div>
                </div>
                <div className="stat-card">
                    <div className="stat-number">{stats.activeEvents}</div>
                    <div className="stat-label">Активных событий</div>
                </div>
                <div className="stat-card">
                    <div className="stat-number">{stats.totalEvents}</div>
                    <div className="stat-label">Событий всего</div>
                </div>
                <div className="stat-card">
                    <div className="stat-number">{stats.groups}</div>
                    <div className="stat-label">Групп</div>
                </div>
            </div>
        </div>
    );
}
