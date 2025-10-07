import Header from './common/Header/Header';
import Footer from './common/Footer/Footer';
import { jwtDecode } from 'jwt-decode';
import AppRouter from './router';
import {Routes, Route} from 'react-router-dom';
import Login from './components/auth/Login/Login';
import Sidebar from './components/Sidebar/Sidebar';
import ProtectedRoute from './router/ProtectedRoute';
import {useEffect} from "react";
import RegisterViaToken from "./components/auth/RegisterViaToken/RegisterViaToken.jsx";

export default function App() {

    const isTokenExpired = (token) => {
        if (!token) return true;
        try {
            const decodedToken = jwtDecode(token);
            const currentTime = Date.now() / 1000;
            return decodedToken.exp < currentTime;
        } catch (error) {
            console.error('Error decoding token:', error);
            return true;
        }
    };
    useEffect(() => {
        if (isTokenExpired(localStorage.getItem("token"))) {
            localStorage.removeItem("token");
            localStorage.removeItem("name");
        };},[])

    return (
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<RegisterViaToken />} />

                <Route
                    path="/*"
                    element={
                        <ProtectedRoute>
                            <div className="app-wrapper">
                                <Header />
                                <div className="app-body">
                                    <Sidebar />
                                    <main className="app-main">
                                        <AppRouter />
                                    </main>
                                </div>
                                <Footer />
                            </div>
                        </ProtectedRoute>
                    }
                />
            </Routes>
    );
}
