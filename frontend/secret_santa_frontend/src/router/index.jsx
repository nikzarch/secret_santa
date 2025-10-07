import React from "react";
import { Routes, Route } from "react-router-dom";
import Login from "../components/auth/Login/Login";
import Main from "../components/home/Main/Main";
import RootRedirect from "./RootRedirect";
import ProtectedRoute from "./ProtectedRoute";

import Me from "../components/home/Me/Me";
import Wishlist from "../components/home/Wishlist/Wishlist.jsx";
import Groups from "../components/home/Groups/Groups.jsx";
import Invitations from "../components/home/Invitations/Invitations.jsx";
import Events from "../components/home/Events/Events.jsx";
export default function AppRouter() {

    return (
        <Routes>
            <Route path="/" element={<RootRedirect />} />

            <Route path="/login" element={<Login />} />

            <Route
                path="/home"
                element={
                    <ProtectedRoute>
                                    <Main />
                    </ProtectedRoute>
                }
            />
            <Route
                path="/me"
                element={
                    <ProtectedRoute>
                                    <Me />
                    </ProtectedRoute>
                }
            />

            <Route
                path="/wishlist"
                element={
                    <ProtectedRoute>
                        <Wishlist />
                    </ProtectedRoute>
                }
            />

            <Route
                path="/groups"
                element={
                    <ProtectedRoute>
                        <Groups />
                    </ProtectedRoute>
                }
            />

            <Route
                path="/groups/invitations"
                element={
                    <ProtectedRoute>
                        <Invitations />
                    </ProtectedRoute>
                }
            />
            <Route
                path="/events"
                element={
                    <ProtectedRoute>
                        <Events />
                    </ProtectedRoute>
                }
            />

        </Routes>
    );
}
