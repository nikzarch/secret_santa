import React, { useEffect, useState } from "react";
import "./Groups.css";

export default function Groups() {
    const [groups, setGroups] = useState([]);
    const [newGroupName, setNewGroupName] = useState("");
    const [toast, setToast] = useState(null);

    const [selectedGroup, setSelectedGroup] = useState(null);
    const [modalType, setModalType] = useState(null); // "view", "add", "kick", "transfer"
    const [usernameInput, setUsernameInput] = useState("");

    const token = localStorage.getItem("token");
    const username = localStorage.getItem("name");

    const showToast = (message, type = "error") => {
        setToast({ message, type });
        setTimeout(() => setToast(null), 3000);
    };

    const fetchGroups = async () => {
        try {
            const res = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/groups`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || "Ошибка загрузки групп");
            }
            const data = await res.json();
            console.log(data);
            setGroups(data.content || []);
        } catch (e) {
            showToast(e.message);
        }
    };

    useEffect(() => {
        fetchGroups();
    }, []);

    const handleCreateGroup = async () => {
        if (!newGroupName.trim()) return;
        try {
            const res = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/groups`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify({ name: newGroupName }),
            });
            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || "Ошибка создания группы");
            }
            setNewGroupName("");
            fetchGroups();
            showToast("Группа создана", "success");
        } catch (e) {
            showToast(e.message);
        }
    };

    const handleDeleteGroup = async (name) => {
        if (!window.confirm(`Удалить группу "${name}"?`)) return;
        try {
            const res = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/groups/${name}`, {
                method: "DELETE",
                headers: { Authorization: `Bearer ${token}` },
            });
            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || "Ошибка удаления группы");
            }
            fetchGroups();
            showToast("Группа удалена", "success");
        } catch (e) {
            showToast(e.message);
        }
    };

    const fetchGroupUsers = async (group) => {
        if (!group.id) {
            showToast("Ошибка: группа не имеет id");
            return;
        }
        try {
            const res = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/groups/${group.id}/participants`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || "Ошибка загрузки участников");
            }
            const data = await res.json();
            setSelectedGroup({ ...group, users: data });
            setModalType("view");
        } catch (e) {
            showToast(e.message);
        }
    };


    const handleAddUser = async () => {
        if (!usernameInput.trim()) return;
        try {
            const res = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/groups/invite`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify({ group_id: selectedGroup.id, username: usernameInput }),
            });
            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || "Ошибка приглашения пользователя");
            }
            setUsernameInput("");
            fetchGroupUsers(selectedGroup);
            showToast("Пользователь приглашён", "success");
        } catch (e) {
            showToast(e.message);
        }
    };

    const handleKickUser = async (username) => {
        if (!window.confirm(`Исключить пользователя "${username}"?`)) return;
        try {
            const res = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/groups/kick-user`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify({ group_name: selectedGroup.name, username:  username }),
            });
            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || "Ошибка исключения пользователя");
            }
            fetchGroupUsers(selectedGroup);
            showToast("Пользователь кикнут", "success");
        } catch (e) {
            showToast(e.message);
        }
    };

    const handleTransferOwner = async (username) => {
        try {
            const res = await fetch(`${import.meta.env.VITE_API_URL}/api/v1/groups/transfer-owner`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify({ group_name: selectedGroup.name, username }),
            });
            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || "Ошибка передачи владельца");
            }
            fetchGroupUsers(selectedGroup);
            showToast("Владелец изменён", "success");
            setModalType(null);
        } catch (e) {
            showToast(e.message);
        }
    };

    return (
        <div className="groups-page">
            <h2>Ваши группы</h2>
            <div className="new-group">
                <input
                    type="text"
                    placeholder="Название новой группы"
                    value={newGroupName}
                    onChange={(e) => setNewGroupName(e.target.value)}
                />
                <button onClick={handleCreateGroup}>Создать</button>
            </div>

            <ul className="groups-list">
                {groups.map((group) => (
                    <li key={group.id}>
                        <span>{group.name}</span>
                        <div>
                            {group.owner.name === username && <button onClick={() => handleDeleteGroup(group.name)}>Удалить</button>}
                            <button onClick={() => fetchGroupUsers(group)}>Участники</button>
                        </div>
                    </li>
                ))}
            </ul>


            {modalType === "view" && selectedGroup && (
                <div className="modal">
                    <h3>Участники группы {selectedGroup.name}</h3>
                    <ul>
                        {selectedGroup.users.map((u) => (
                            <li key={u.id}>
                                {u.name}
                                {selectedGroup.owner.name === username && u.name != username && <button onClick={() => handleKickUser(u.name)}>Исключить</button>}
                                {selectedGroup.owner.name === username && u.name != username && <button onClick={() => handleTransferOwner(u.name)}>Сделать владельцем</button>}
                            </li>
                        ))}
                    </ul>
                    {selectedGroup.owner.name === username &&
                    <div className="modal-add-user">
                        <input
                            type="text"
                            placeholder="Имя пользователя"
                            value={usernameInput}
                            onChange={(e) => setUsernameInput(e.target.value)}
                        />
                        <button onClick={handleAddUser}>Добавить</button>
                    </div>
                    }
                    <button className="modal-close" onClick={() => setModalType(null)}>Закрыть</button>
                </div>
            )}

            {toast && <div className={`toast ${toast.type}`}>{toast.message}</div>}
        </div>
    );
}
