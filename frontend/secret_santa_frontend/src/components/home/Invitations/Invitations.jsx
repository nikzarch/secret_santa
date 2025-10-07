import React, { useEffect, useState } from "react";

import "./Invitations.css";

export default function Invitations() {
    const [invites, setInvites] = useState([]);
    const [toast, setToast] = useState(null);

    const token = localStorage.getItem("token");

    const showToast = (text, type="error") => {
        setToast({ text, type });
        setTimeout(() => setToast(null), 3000);
    };

    const fetchInvites = async () => {
        try {
            const res = await fetch("http://localhost:8080/api/v1/groups/invites", {
                headers: { Authorization: `Bearer ${token}` }
            });
            if (!res.ok) throw new Error("Ошибка загрузки приглашений");
            const data = await res.json();
            console.log(data);
            setInvites(data);
        } catch (e) {
            showToast(e.message);
        }
    };

    useEffect(() => { fetchInvites(); }, []);

    const accept = async (id) => {
        try {
            const res = await fetch(`http://localhost:8080/api/v1/groups/invites/${id}/accept`, {
                method: "POST",
                headers: { Authorization: `Bearer ${token}` }
            });
            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || 'Ошибка');
            }
            showToast("Приглашение принято", "success");
            fetchInvites();
        } catch (e) { showToast(e.message); }
    };

    const decline = async (id) => {
        try {
            const res = await fetch(`http://localhost:8080/api/v1/groups/invites/${id}/decline`, {
                method: "POST",
                headers: { Authorization: `Bearer ${token}` }
            });
            if (!res.ok) throw new Error("Ошибка");
            showToast("Приглашение отклонено", "success");
            fetchInvites();
        } catch (e) { showToast(e.message); }
    };

    return (
        <div className="invites-page">
            <h2>Приглашения</h2>
            {invites.length === 0 && <p>У вас нет приглашений.</p>}
            <ul>
                {invites.map(inv => (
                    <li key={inv.id}>
                        Приглашение в группу <strong>{inv.group_name}</strong> пользователем {inv.inviter_name}
                        <div>
                            <button onClick={() => accept(inv.id)}>Принять</button>
                            <button onClick={() => decline(inv.id)}>Отклонить</button>
                        </div>
                    </li>
                ))}
            </ul>

            {toast && <div className={`toast ${toast.type}`}>{toast.text}</div>}
        </div>
    );
}
