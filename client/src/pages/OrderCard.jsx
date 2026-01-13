// src/components/OrderCard.jsx
import { Link } from "react-router-dom";

const OrderCard = ({ order, role }) => {
  return (
    <div className="bg-white rounded shadow p-4 mb-4">
      <div className="flex items-center">
        <img
          src={order.gigImage || "/default-gig.jpg"}
          alt={order.gigTitle}
          className="w-24 h-24 object-cover rounded mr-4"
        />
        <div className="flex-grow">
          <h2 className="text-xl font-semibold">{order.gigTitle}</h2>
          <p className="text-sm text-gray-500">Order ID: {order.id}</p>
          <p className="text-sm">
            {role === "CLIENT"
              ? `Seller: ${order.sellerId}`
              : `Buyer: ${order.buyerId}`}
          </p>
          <p className="text-sm text-gray-600">Status: 
            <span className={`ml-1 font-semibold ${getStatusColor(order.status)}`}>
              {order.status}
            </span>
          </p>
        </div>
        <div>
          <Link
            to={`/gig/${order.gigId}`}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            View Gig
          </Link>
        </div>
      </div>
    </div>
  );
};

// Tailwind color utility for status
function getStatusColor(status) {
  switch (status) {
    case "PENDING":
      return "text-yellow-500";
    case "COMPLETED":
      return "text-green-600";
    case "CANCELLED":
      return "text-red-500";
    default:
      return "text-gray-500";
  }
}

export default OrderCard;
