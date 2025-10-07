import React, { useEffect, useState } from "react";
import "./Events.css";

export default function Events() {
    const token = localStorage.getItem("token");
    const username = localStorage.getItem("name");

    const [events, setEvents] = useState([]);
    const [groups, setGroups] = useState([]);
    const [loading, setLoading] = useState(true);
    const [toast, setToast] = useState(null);

    const [modalType, setModalType] = useState(null); // "view" | "create"
    const [selectedEvent, setSelectedEvent] = useState(null);

    const [newEvent, setNewEvent] = useState({ name: "", description: "", date: "", groupId: "" });

    const showToast = (message, type = "error") => {
        setToast({ message, type });
        setTimeout(() => setToast(null), 3000);
    };

    const fetchEvents = async () => {
        setLoading(true);
        try {
            const params = new URLSearchParams({ page: 0, size: 50 });
            const res = await fetch(`/api/v1/events?${params.toString()}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            if (!res.ok) throw new Error("Ошибка загрузки событий");
            const data = await res.json();
            console.log(data.content);
            setEvents(data.content || []);
        } catch (err) {
            showToast(err.message);
        } finally {
            setLoading(false);
        }
    };

    const fetchGroups = async () => {
        try {
            const params = new URLSearchParams({ page: 0, size: 50 });
            const res = await fetch(`/api/v1/groups?${params.toString()}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            if (!res.ok) throw new Error("Ошибка загрузки групп");
            const data = await res.json();
            setGroups(data.content || []);
        } catch (err) {
            showToast(err.message);
        }
    };

    useEffect(() => {
        fetchEvents();
        fetchGroups();
    }, []);

    const handleOpenEvent = async (event) => {
        try {
            const res = await fetch(`/api/v1/events/${event.id}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            if (!res.ok) throw new Error("Ошибка загрузки события");
            const data = await res.json();
            setSelectedEvent(data);
            setModalType("view");
        } catch (err) {
            showToast(err.message);
        }
    };

    const handleCreateEvent = async (e) => {
        e.preventDefault();
        if (!newEvent.name || !newEvent.date || !newEvent.groupId) {
            showToast("Заполните все обязательные поля", "error");
            return;
        }
        try {
            const res = await fetch("/api/v1/events", {
                method: "POST",
                headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
                body: JSON.stringify({
                    name: newEvent.name,
                    description: newEvent.description,
                    date: newEvent.date,
                    group_id: newEvent.groupId,
                }),
            });
            if (!res.ok) {
                const data = await res.json();
                throw new Error(data.message || "Ошибка создания события");
            }
            showToast("Событие создано", "success");
            setNewEvent({ name: "", description: "", date: "", groupId: "" });
            setModalType(null);
            fetchEvents();
        } catch (err) {
            showToast(err.message);
        }
    };


    const handleGeneratePairs = async (eventId) => {
        try {
            const res = await fetch(`/api/v1/events/${eventId}/generate-assignments`, {
                method: "POST",
                headers: { Authorization: `Bearer ${token}` },
            });
            if (!res.ok) throw new Error("Ошибка генерации пар");
            showToast("Пары сгенерированы", "success");
            fetchEvents();
        } catch (err) {
            showToast(err.message);
        }
    };

    const handleMyPair = async (eventId) => {
        try {
            const res = await fetch(`/api/v1/events/${eventId}/my-pair`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            if (!res.ok) throw new Error("Её еще нёт");
            const data = await res.json();
            showToast(`Ваша пара: ${data.name}`, "success");
        } catch (err) {
            showToast(err.message);
        }
    };

    if (loading) return <div>Загрузка...</div>;

    return (
        <div className="events-page">
            <div className="events-header">
                <h2>События</h2>
                <button onClick={() => setModalType("create")}>Создать событие</button>
            </div>

            <div className="events-list">
                {events.map((e) => (
                    <div key={e.id} className="event-card">
                        <h3>{e.name}</h3>
                        <p>{e.description}</p>
                        <p>Дата: {e.event_date}</p>
                        <p>Активно: {e.is_active ? "Да" : "Нет"}</p>
                        <p className="assignments-status">
                            Пары сгенерированы: {e.assignments_generated ? "Да" : "Нет"}
                        </p>
                        <div className="event-footer">
                            <button className="btn-details" onClick={() => handleOpenEvent(e)}>
                                Подробнее
                            </button>

                            {e.owner === username && (
                                <button className="btn-generate" onClick={() => handleGeneratePairs(e.id)}>
                                    Сгенерировать пары
                                </button>
                            )}

                            <button className="btn-my-pair" onClick={() => handleMyPair(e.id)}>
                                Узнать мою пару
                            </button>
                        </div>
                    </div>
                ))}
            </div>

            {modalType === "view" && selectedEvent && (
                <div className="modal">
                    <h3>{selectedEvent.name}</h3>
                    <p>{selectedEvent.description}</p>
                    <p>Дата: {selectedEvent.event_date}</p>
                    <p>Активно: {selectedEvent.is_active ? "Да" : "Нет"}</p>
                    <h4>Участники:</h4>
                    <ul>
                        {selectedEvent.participants.map((p) => (
                            <li key={p.id}>
                                {p.name} ({p.role})
                            </li>
                        ))}
                    </ul>
                    <button onClick={() => setModalType(null)}>Закрыть</button>
                </div>
            )}

            {modalType === "create" && (
                <div className="modal">
                    <h3>Создать событие</h3>
                    <form onSubmit={handleCreateEvent}>
                        <div className="form-field">
                            <label>Название</label>
                            <input
                                type="text"
                                value={newEvent.name}
                                onChange={(e) => setNewEvent({ ...newEvent, name: e.target.value })}
                                required
                            />
                        </div>
                        <div className="form-field">
                            <label>Описание</label>
                            <input
                                type="text"
                                value={newEvent.description}
                                onChange={(e) =>
                                    setNewEvent({ ...newEvent, description: e.target.value })
                                }
                            />
                        </div>
                        <div className="form-field">
                            <label>Дата</label>
                            <input
                                type="date"
                                value={newEvent.date}
                                onChange={(e) => setNewEvent({ ...newEvent, date: e.target.value })}
                                required
                            />
                        </div>
                        <div className="form-field">
                            <label>Группа</label>
                            <select
                                value={newEvent.groupId}
                                onChange={(e) => setNewEvent({ ...newEvent, groupId: e.target.value })}
                                required
                            >
                                <option value="">Выберите группу</option>
                                {groups
                                    .filter((g) => g.owner.name === username)
                                    .map((g) => (
                                        <option key={g.id} value={g.id}>
                                            {g.name}
                                        </option>
                                    ))}
                            </select>
                        </div>
                        <button type="submit">Создать</button>
                        <button type="button" onClick={() => setModalType(null)}>
                            Отмена
                        </button>
                    </form>
                </div>
            )}

            {toast && <div className={`toast ${toast.type}`}>{toast.message}</div>}
        </div>
    );
}
