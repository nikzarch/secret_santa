import React, { useEffect, useState } from "react";
import "./Chat.css";
import { useLocation } from "react-router-dom";

export default function Chat() {
    const token = localStorage.getItem("token");
    const username = localStorage.getItem("name");

    const location = useLocation();
    const event = location.state?.event;
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState("");
    const [loading, setLoading] = useState(true);
    const [toast, setToast] = useState(null);
    const [chatType, setChatType] = useState("receiver");

    const showToast = (message, type = "error") => {
        setToast({ message, type });
        setTimeout(() => setToast(null), 3000);
    };

    const fetchMessages = async () => {
        if (!event) return;
        setLoading(true);
        try {
            const params = new URLSearchParams({ page: 0, size: 50, chat: chatType });
            const res = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/chat?event_id=${event.id}&${params.toString()}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            if (!res.ok) throw new Error("Ошибка загрузки сообщений");
            const data = await res.json();
            setMessages(data.content || data);
        } catch (err) {
            showToast(err.message || "Ошибка загрузки сообщений");
        } finally {
            setLoading(false);
        }
    };

    const sendMessage = async () => {
        if (!newMessage.trim()) return;
        try {
            const res = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/chat?chat=${chatType}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify({ event_id: event.id, message: newMessage }),
            });
            if (!res.ok) {
                const errBody = await res.json().catch(() => null);
                throw new Error(errBody?.message ||  "Ошибка отправки сообщения");
            }
            setNewMessage("");
            fetchMessages();
        } catch (err) {
            showToast(err.message || "Ошибка отправки сообщения");
        }
    };

    useEffect(() => {
        fetchMessages();
    }, [event, chatType]);

    if (!event) return <div>Нет выбранного события</div>;
    if (loading) return <div>Загрузка сообщений...</div>;

    const otherLabel = chatType === "santa" ? "Санта" : (event.receiverName || "Получатель");

    return (
        <div className="chat-page">
            <h2>Чат события: {event.name}</h2>

            <div className="chat-tabs">
                <button
                    className={`chat-tab ${chatType === "receiver" ? "active" : ""}`}
                    onClick={() => setChatType("receiver")}
                >
                    С получателем
                </button>
                <button
                    className={`chat-tab ${chatType === "santa" ? "active" : ""}`}
                    onClick={() => setChatType("santa")}
                >
                    С Сантой
                </button>
            </div>

            <div className="chat-window">
                {messages.length === 0 ? (
                    <div className="chat-empty">Нет сообщений</div>
                ) : (
                    messages.map((m, idx) => {
                        const fromIsYou = (m.from === "YOU" || m.from === "You" || m.from === "you");
                        return (
                            <div
                                key={idx}
                                className={`chat-message ${fromIsYou ? "chat-sent" : "chat-received"}`}
                            >
                                <div className="chat-sender">
                                    {fromIsYou ? username : otherLabel}
                                </div>
                                <div className="chat-text">{m.message}</div>
                                <div className="chat-time">{new Date(m.sent_at).toLocaleString()}</div>
                            </div>
                        )
                    })
                )}
            </div>

            <div className="chat-input-container">
                <input
                    type="text"
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    placeholder="Введите сообщение..."
                    onKeyDown={(e) => { if (e.key === "Enter") sendMessage(); }}
                />
                <button onClick={sendMessage}>Отправить</button>
            </div>

            {toast && <div className={`toast ${toast.type}`}>{toast.message}</div>}
        </div>
    );
}
