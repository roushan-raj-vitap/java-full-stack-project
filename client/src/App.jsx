import { useEffect } from "react";
import { Navigate, Route, Routes } from "react-router-dom"; // ✅ Make sure Navigate is imported
import { useAuth } from "./pages/context/AuthContext";

import CreateGig from "./pages/CreateGig";
import GigDetail from "./pages/GigDetail";
import Home from "./pages/Home";
import LandingPage from "./pages/LandingPage";
import Login from "./pages/Login";
import MyOrders from "./pages/MyOrder";
import Navbar from "./pages/Nav";
import RegisterClient from "./pages/RegisterClient";
import RegisterFreelancer from "./pages/RegisterFreelancer";

function App() {
  useEffect(() => {
    document.title = "Freelancer-Application";
  }, []);

  const { isLoggedIn, user } = useAuth();

  // ✅ Wrapper to protect authenticated routes
  const ProtectedRoute = ({ children }) => {
    if (!isLoggedIn) {
      return <Navigate to="/login" replace />;
    }
    return children;
  };

  return (
    <Routes>
      {/* Public Routes */}
      <Route path="/" element={<LandingPage />} />
      <Route path="/login" element={isLoggedIn ? <Navigate to="/home" /> : <Login />} />
      <Route path="/register/client" element={<RegisterClient />} />
      <Route path="/register/freelancer" element={<RegisterFreelancer />} />

      {/* Protected Routes (with Navbar) */}
      <Route element={<ProtectedRoute><Navbar /></ProtectedRoute>}>
        <Route path="/home" element={<Home />} />
        <Route path="/gig/create" element={<CreateGig />} />
        <Route path="/gig/:id" element={<GigDetail/>}></Route>
        <Route path="/gigs/order" element = {<MyOrders/>}/>
      </Route>

      {/* Catch all route */}
      <Route path="*" element={<Navigate to={isLoggedIn ? "/home" : "/"} />} />
    </Routes>
  );
}

export default App;
