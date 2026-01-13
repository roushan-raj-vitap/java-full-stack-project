import axios from "axios";
import { createContext, useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const AuthContext = createContext();
export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const navigate = useNavigate();
  const [token, setToken] = useState(localStorage.getItem("token"));
  const [user, setUser] = useState(() => {
    const storedUser = localStorage.getItem("user");
    return storedUser ? JSON.parse(storedUser) : null;
  });
  const [error, setError] = useState('');
  useEffect(() => {
    if (token) localStorage.setItem("token", token);
    if (user) localStorage.setItem("user", JSON.stringify(user));
  }, [token, user]);

  const login = async (form) => {
    setError('');
    try {
      const res = await axios.post('http://localhost:8080/auth/login', form);
      
      const { jwtToken, email, role} = res.data;
      setToken(jwtToken);
      setUser({ email, role });
      console.log("stripe account id",stripeAccount)
      navigate('/home');
    } catch (err) {
      setError('Invalid credentials');
    }
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    setToken(null);
    setUser(null);
  };

  const isLoggedIn = !!token;

  return (
    <AuthContext.Provider value={{ token, user, login, logout, isLoggedIn, error}}>
      {children}
    </AuthContext.Provider>
  );
};
