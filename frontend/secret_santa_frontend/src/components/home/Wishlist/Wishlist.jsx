import React, { useEffect, useState } from "react";
import "./Wishlist.css";

export default function Wishlist() {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [toast, setToast] = useState(null);

    const [newItem, setNewItem] = useState({ name: "", description: "", link: "", priority: 1 });
    const [editingItem, setEditingItem] = useState(null);

    const token = localStorage.getItem("token");

    const fetchWishlist = async () => {
        setLoading(true);
        try {
            const params = new URLSearchParams({ page: 0, size: 50 });
            const res = await fetch(`http://localhost:8080/api/v1/wishlist?${params.toString()}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            if (!res.ok) throw new Error("Ошибка загрузки вишлиста");
            const data = await res.json();
            setItems(data.content || []);
        } catch (err) {
            showToast(err.message, "error");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchWishlist();
    }, []);

    const showToast = (text, type = "error") => {
        setToast({ text, type });
        setTimeout(() => setToast(null), 3000);
    };

    const handleAdd = async (e) => {
        e.preventDefault();
        try {
            const res = await fetch("http://localhost:8080/api/v1/wishlist", {
                method: "POST",
                headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
                body: JSON.stringify(newItem),
            });
            const data = await res.json();
            if (!res.ok) {
                showToast(data.error || data.message || "Ошибка добавления", "error");
                return;
            }
            showToast("Элемент добавлен", "success");
            setNewItem({ name: "", description: "", link: "", priority: 1 });
            await fetchWishlist();
        } catch (err) {
            showToast(err.message, "error");
        }
    };

    const handleDelete = async (id) => {
        try {
            const res = await fetch(`http://localhost:8080/api/v1/wishlist/${id}`, {
                method: "DELETE",
                headers: { Authorization: `Bearer ${token}` },
            });
            if (!res.ok) throw new Error("Ошибка удаления");
            showToast("Элемент удалён", "success");
            await fetchWishlist();
        } catch (err) {
            showToast(err.message, "error");
        }
    };

    const handleUpdate = async (id, updatedItem) => {
        try {
            const res = await fetch(`http://localhost:8080/api/v1/wishlist/${id}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
                body: JSON.stringify(updatedItem),
            });
            if (!res.ok) throw new Error("Ошибка обновления");
            showToast("Элемент обновлён", "success");
            setEditingItem(null);
            await fetchWishlist();
        } catch (err) {
            showToast(err.message, "error");
        }
    };

    if (loading) return <div>Загрузка...</div>;

    return (
        <div className="wishlist-page">
            <h2>Вишлист</h2>
            <p>пиши сюда, что хочешь получить в подарок, эта инфа будет доступна тому, кто тебе дарит</p>

            <form className="wishlist-form" onSubmit={handleAdd}>
                <div className="wishlist-fields">
                    <input
                        type="text"
                        placeholder="Название"
                        value={newItem.name}
                        onChange={(e) => setNewItem({ ...newItem, name: e.target.value })}
                        required
                    />
                    <input
                        type="text"
                        placeholder="Описание"
                        value={newItem.description}
                        onChange={(e) => setNewItem({ ...newItem, description: e.target.value })}
                    />
                    <input
                        type="text"
                        placeholder="Ссылка"
                        value={newItem.link}
                        onChange={(e) => setNewItem({ ...newItem, link: e.target.value })}
                    />
                    <label>Приоритет:</label>
                    <input
                        type="number"
                        min={1}
                        value={newItem.priority}
                        onChange={(e) => setNewItem({ ...newItem, priority: parseInt(e.target.value) })}
                        required
                    />
                </div>
                <button type="submit">Добавить</button>
            </form>


            <div className="wishlist-list">
                {items.map((item) => (
                    <div key={item.id} className="wishlist-item">
                        {editingItem === item.id ? (
                            <EditForm
                                item={item}
                                onCancel={() => setEditingItem(null)}
                                onSave={(updatedItem) => handleUpdate(item.id, updatedItem)}
                            />
                        ) : (
                            <>
                                <h3>{item.name}</h3>
                                {item.description && <p>{item.description}</p>}
                                {item.link && (
                                    <p>
                                        <a
                                            href={item.link.startsWith("http://") || item.link.startsWith("https://")
                                                ? item.link
                                                : `https://${item.link}`}
                                            target="_blank"
                                            rel="noopener noreferrer"
                                        >
                                            Ссылка ({item.link})
                                        </a>
                                    </p>
                                )}

                                <p>Приоритет: {item.priority}</p>
                                <div className="wishlist-actions">
                                    <button onClick={() => setEditingItem(item.id)}>Редактировать</button>
                                    <button onClick={() => handleDelete(item.id)}>Удалить</button>
                                </div>
                            </>
                        )}
                    </div>
                ))}
            </div>

            {toast && <div className={`toast ${toast.type}`}>{toast.text}</div>}
        </div>
    );
}

function EditForm({ item, onCancel, onSave }) {
    const [formData, setFormData] = useState({
        name: item.name,
        description: item.description,
        link: item.link,
        priority: item.priority,
    });

    return (
        <form
            className="wishlist-edit-form"
            onSubmit={(e) => {
                e.preventDefault();
                onSave(formData);
            }}
        >
            <div className="form-field">
                <label>Название:</label>
                <input
                    type="text"
                    value={formData.name}
                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    required
                />
            </div>
            <div className="form-field">
                <label>Описание:</label>
                <input
                    type="text"
                    value={formData.description}
                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                />
            </div>
            <div className="form-field">
                <label>Ссылка:</label>
                <input
                    type="text"
                    value={formData.link}
                    onChange={(e) => setFormData({ ...formData, link: e.target.value })}
                />
            </div>
            <div className="form-field">
                <label>Приоритет:</label>
                <input
                    type="number"
                    min={1}
                    max={10}
                    value={formData.priority}
                    onChange={(e) => setFormData({ ...formData, priority: parseInt(e.target.value) })}
                    required
                />
            </div>
            <div className="wishlist-actions">
                <button type="submit">Сохранить</button>
                <button type="button" onClick={onCancel}>
                    Отмена
                </button>
            </div>
        </form>
    );
}
