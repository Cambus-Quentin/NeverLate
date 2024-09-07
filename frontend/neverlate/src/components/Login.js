import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../services/AuthService';
import { AuthContext } from '../context/AuthContext';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const { setCurrentUser } = useContext(AuthContext); // Utilisation du contexte pour mettre à jour l'utilisateur
    const navigate = useNavigate();

    const onSubmit = (e) => {
        e.preventDefault();

        AuthService.login(username, password)
            .then((user) => {
                setCurrentUser(user); // Mettre à jour le contexte avec l'utilisateur connecté
                navigate('/home');
            })
            .catch(error => {
                setErrorMessage('Invalid username or password');
            });
    };

    return (
        <div className="login-container">
            <form onSubmit={onSubmit}>
                <div>
                    <label>Username</label>
                    <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} required />
                </div>
                <div>
                    <label>Password</label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                </div>
                <button type="submit">Login</button>
                {errorMessage && <div className="error-message">{errorMessage}</div>}
            </form>
        </div>
    );
};

export default Login;
