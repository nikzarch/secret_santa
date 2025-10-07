import React from "react";
import { Navigate } from "react-router-dom";

export default function RootRedirect() {
    const token = localStorage.getItem("token");

    return token ? <Navigate to="/home" replace /> : <Navigate to="/login" replace />;
}
