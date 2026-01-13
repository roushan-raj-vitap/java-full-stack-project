// src/pages/GigDetail.jsx
import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAuth } from "./context/AuthContext";

export default function GigDetail() {
    const {user,token} = useAuth();
  const { id } = useParams(); // gig id from URL
  const navigate = useNavigate();
  const [gig, setGig] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchGig() {
      try {
        const res = await axios.get(`http://localhost:8080/api/gigs/${id}`,
            {headers:{Authorization: "Bearer "+token}}
        );
        setGig(res.data);
      } catch (err) {
        console.error("Error fetching gig:", err);
        alert("Could not load gig");
      } finally {
        setLoading(false);
      }
    }

    fetchGig();
  }, [id]);
  const handlePurchase = async () => {
  try {
    const token = localStorage.getItem("token");

    const connectedAccountId = gig.connectedAccountId;
    const platformFeePercent = 10;
    const amountInPaise = gig.price * 100;
    const platformFeeAmount = Math.floor((platformFeePercent / 100) * amountInPaise);

    const res = await axios.post(
      "http://localhost:8080/api/stripe/create-checkout-session",
      {
        name: gig.title,
        amount: amountInPaise,
        quantity: 1,
        currency: "inr",
        connectedAccountId,
        platformFeeAmount,
          buyerId: user.email,
          sellerId: gig.email,
          gigId: gig.id
        
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      }
    );
    const { sessionUrl } = res.data;
    window.location.href = sessionUrl;
  } catch (err) {
    console.error("Stripe checkout failed", err);
    alert("Unable to process payment");
  }
};




  if (loading) return <p className="p-4">Loading...</p>;
  if (!gig) return <p className="p-4">Gig not found</p>;

  return (
    <div className="max-w-3xl mx-auto p-6 bg-white rounded shadow">
      <img src={gig.imageUrl} alt={gig.title} className="w-full h-64 object-cover mb-4 rounded" />
      <h1 className="text-3xl font-bold mb-2">{gig.title}</h1>
      <p className="text-gray-600 mb-4">{gig.description}</p>
      <p className="text-lg font-semibold">Price: ₹{gig.price}</p>
      <p className="text-sm text-gray-500">Delivery in {gig.deliveryTime} days</p>
      <div className="border-t mt-4 pt-4">
        <h3 className="text-xl font-semibold">Freelancer Info</h3>
        <p>Name: {gig.name|| "N/A"} - Email: {gig.email}</p>
      </div>
      <button
        onClick={handlePurchase}
        className="mt-6 bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700"
      >
        Purchase
      </button>
    </div>
  );
}
