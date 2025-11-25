import { useNavigate } from 'react-router-dom';
import './Header.css';

export default function Header() {
    const navigate = useNavigate();
    const name = localStorage.getItem('name');

    const handleLogout = () => {
        localStorage.removeItem('name');
        localStorage.removeItem('token');
        navigate('/login');
    }

    const openSidebar = () => {
        const evt = new CustomEvent("open-sidebar");
        window.dispatchEvent(evt);
    };

    return (
        <header className="app-header">
            <button className="burger" onClick={openSidebar}>☰</button>
            <div className="name-placeholder" onClick={() => navigate('/home')}>Тайный Санта</div>
            <div
                    className="username-placeholder"
                    onClick={() => navigate('/me')}>
                    {name}
            </div>
            <div className="button-placeholder" >
                <button onClick={handleLogout}>Logout</button>
            </div>
        </header>
    );
}
