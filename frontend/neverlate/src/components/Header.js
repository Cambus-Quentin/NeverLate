import React, { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import { Link, useNavigate } from 'react-router-dom';

function Header() {
  const { currentUser, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();  // Appel de la fonction de déconnexion
    navigate('/login');  // Redirection vers la page de connexion
  };

  return (
    <header>
      <nav>
        {currentUser ? (
          <>
            <span>Bienvenue, {currentUser.sub}</span> {/* 'sub' correspond souvent au username dans un JWT */}
            <button onClick={handleLogout}>Déconnexion</button>
          </>
        ) : (
          <Link to="/login">Connexion</Link>
        )}
      </nav>
    </header>
  );
}

export default Header;
