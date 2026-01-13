// src/components/Navbar.jsx
import { useState } from "react";
import { Link, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "./context/AuthContext";
import { useGig } from "./context/context";
import CreateGig from "./CreateGig";
import Modal from "./Model";
const Navbar = () => {
    const {logout,user,token} = useAuth();
    const navigate = useNavigate();
    const [showModal,setShowModal] = useState(false);
    const {addGig} = useGig();
    function handleLogout(){
        logout();
        navigate("/login");
    }
    const handleCheckBalance = async () => {
  try {
    const response = await fetch(`http://localhost:8080/api/stripe/login-link`, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`, // ✅ Make sure token is valid
        "Content-Type": "application/json"
      }
    });

    if (!response.ok) {
      throw new Error("Failed to get Stripe login link");
    }

    const loginUrl = await response.text();
    window.location.href = loginUrl;
  } catch (error) {
    console.error("Stripe login link error:", error);
    alert("Unable to redirect to Stripe dashboard.");
  }
};

  return (
    <>
    <nav className="flex justify-between items-center p-4 bg-white shadow">
  <Link to="/" className="text-xl font-bold text-blue-600">
    FreelanceHub
  </Link>
  <div className="flex gap-4 items-center">
    <Link to="/gigs" className="text-gray-700 hover:text-blue-600">Gigs</Link>
    {user?.role === 'FREELANCER' && (
      <button className="text-gray-700 hover:text-blue-600" onClick={()=>setShowModal(true)}>
        Post a Gig
      </button>
    )}
    {user.role=="FREELANCER"&&<button onClick={() => handleCheckBalance()}>
      Check Balance
    </button>}
    <Link to = "/gigs/order">Orders</Link>
    <span className="text-sm text-gray-500">
      {user?.email} ({user?.role})
    </span>
    <button
      onClick={handleLogout}
      className="text-sm bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
    >
      Logout
    </button>
  </div>
</nav>
{showModal && (
  <Modal onClose={() => setShowModal(false)}>
    <CreateGig
      onGigCreated={(newGig) => {
        addGig(newGig);
        setShowModal(false);
      }}
    />
  </Modal>
)}
<Outlet/>
</>

  );
};

export default Navbar;
