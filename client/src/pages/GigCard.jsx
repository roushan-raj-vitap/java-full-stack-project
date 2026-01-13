import { useNavigate } from "react-router-dom";
import { useAuth } from "./context/AuthContext";

const GigCard = ({ gig, handleEdit, handleDelete }) => {
  const { user } = useAuth();
  const isOwner = user?.role === "FREELANCER";
  const navigate = useNavigate();
  function showDetail(id){
    navigate(`/gig/${id}`)
  }
  return (
    <div className="bg-white rounded-2xl shadow hover:shadow-lg transition-shadow duration-300 overflow-hidden">
      <img
        src={gig.imageUrl || "https://via.placeholder.com/400x200"}
        alt={gig.title}
        className="w-full h-48 object-cover"
      />
      <div className="p-4">
        <h3 className="text-lg font-semibold mb-1 truncate">{gig.title}</h3>
        <p className="text-sm text-gray-500 mb-2">{gig.category}</p>
        <p className="text-sm text-gray-700 mb-2 line-clamp-2">{gig.description}</p>

        <div className="mt-4 space-y-1 text-sm text-gray-600">
          <div><span className="font-medium">Price:</span> ₹{gig.price}</div>
          <div><span className="font-medium">Delivery Time:</span> {gig.deliveryTime} days</div>
        </div>

        {isOwner && (
          <div className="mt-4 flex justify-between">
            <button
              className="text-blue-600 hover:underline"
              onClick={() => handleEdit(gig)}
            >
              Edit
            </button>
            <button
              className="text-red-600 hover:underline"
              onClick={() => handleDelete(gig.id)}
            >
              Delete
            </button>
          </div>
        )}
        <button className="text-green-600 hover:underline"
              onClick = {()=>showDetail(gig.id)}>
              Show Details
            </button>
      </div>
    </div>
  );
};

export default GigCard;
