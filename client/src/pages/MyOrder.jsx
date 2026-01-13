// src/pages/MyOrders.jsx
import axios from "axios";
import { useEffect, useState } from "react";
// import { useAuth } from "../context/AuthContext";
import OrderCard from "./OrderCard.jsx";
import { useAuth } from "./context/AuthContext.jsx";
const MyOrders = () => {
  const { user, token } = useAuth();
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    async function fetchOrders() {
      try {
        const res = await axios.get("http://localhost:8080/api/orders", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setOrders(res.data);
      } catch (err) {
        console.error("Failed to load orders", err);
      }
    }

    fetchOrders();
  }, []);

  return (
    <div className="max-w-4xl mx-auto p-4">
      <h1 className="text-2xl font-bold mb-6">My Orders</h1>
      {orders.length === 0 ? (
        <p>No orders found.</p>
      ) : (
        orders.map((order) => (
          <OrderCard key={order.id} order={order} role={user.role} />
        ))
      )}
    </div>
  );
};

export default MyOrders;
