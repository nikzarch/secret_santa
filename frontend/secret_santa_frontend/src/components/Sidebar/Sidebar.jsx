import React from "react";
import { NavLink } from "react-router-dom";
import "./Sidebar.css";

export default function Sidebar() {
    const navItems = [
        { path: "/wishlist", label: "Вишлист" },
        { path: "/events", label: "События" },
        {
            label: "Группы",
            submenu: [
                { path: "/groups", label: "Мои группы" },
                { path: "/groups/invitations", label: "Приглашения" },
            ],
        },
    ];

    return (
        <aside className="sidebar">
            <ul className="sidebar-list">
                {navItems.map((item) => (
                    <li key={item.label}>
                        {item.submenu ? (
                            <>
                                <div className="sidebar-group-header">{item.label}</div>
                                <ul className="sidebar-submenu">
                                    {item.submenu.map((sub) => (
                                        <li key={sub.path}>
                                            <NavLink
                                                to={sub.path}
                                                className={({ isActive }) =>
                                                    isActive ? "sidebar-link active" : "sidebar-link"
                                                }
                                            >
                                                {sub.label}
                                            </NavLink>
                                        </li>
                                    ))}
                                </ul>
                            </>
                        ) : (
                            <NavLink
                                to={item.path}
                                className={({ isActive }) =>
                                    isActive ? "sidebar-link active" : "sidebar-link"
                                }
                            >
                                {item.label}
                            </NavLink>
                        )}
                    </li>
                ))}
            </ul>
        </aside>
    );
}
