import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext'
import Login from './components/Login';
import TimeZoneList from './components/TimeZoneList';
import PrivateRoute from './components/PrivateRoute';
import Header from './components/Header';
import TimeZoneEdit from './components/TimeZoneEdit';
import CreateTimeZone from './components/CreateTimeZone'; // Importer le composant de création

function App() {
  return (
    <AuthProvider>
      <Router>
        <Header />
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route
            path="/home"
            element={
              <PrivateRoute>
                <TimeZoneList />
              </PrivateRoute>
            }
          />
          <Route 
            path="/edit/:id" 
            element={
              <PrivateRoute>
                <TimeZoneEdit />
              </PrivateRoute>} 
          />
          <Route
            path="/create" 
            element={
              <PrivateRoute>
                <CreateTimeZone />
              </PrivateRoute>
            } 
          />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
