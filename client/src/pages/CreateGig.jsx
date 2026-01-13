import axios from "axios";
import { useEffect, useState } from "react";

export default function CreateGig({ gig: editingGig, onSuccess,onGigCreated }) {
  const [gig, setGig] = useState({
    title: "",
    description: "",
    price: "",
    deliveryTime: "",
    category: "",
    imageUrl: "",
    image: null,
  });
  const [updating, setUpdating] = useState(false);

  useEffect(() => {
    if (editingGig) {
      setGig({
        ...editingGig,
        image: null, // keeping it null for now, user can change it
      });
      setUpdating(true);
    }
  }, [editingGig]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setGig((prev) => ({ ...prev, [name]: value }));
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    setGig((prev) => ({ ...prev, image: file }));
  };
  const handleSubmit = async (e) => {
  e.preventDefault();
  const formData = new FormData();

  Object.entries(gig).forEach(([key, value]) => {
    if (value !== null && value !== "") {
      formData.append(key, value);
    }
  });

  const token = localStorage.getItem("token");

  try {
    if (updating) {
      await axios.put(`http://localhost:8080/api/gigs/${editingGig.id}`, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      alert("Gig updated successfully");
      if (onSuccess) onSuccess(); // close modal or refresh list
    } else {
      // ✅ Use response from backend
      const res = await axios.post("http://localhost:8080/api/gigs", formData, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      const createdGig = res.data; // This includes ID, S3 image URL, etc.
      alert("Gig created successfully");
      if (onGigCreated) onGigCreated(createdGig);
      // ✅ Add new gig to state using actual response
      

      if (onSuccess) onSuccess(); // refresh list or close modal
    }
  } catch (err) {
    console.log(err);
    alert("Error in submitting gig");
  }
};


  return (
    <div className="max-w-xl mx-auto p-4">
      <h2 className="text-2xl font-bold mb-4">{updating ? "Edit Gig" : "Create a Gig"}</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          type="text"
          name="title"
          placeholder="Title"
          value={gig.title}
          onChange={handleChange}
          className="w-full p-2 border rounded"
          required
        />
        <textarea
          name="description"
          placeholder="Description"
          value={gig.description}
          onChange={handleChange}
          className="w-full p-2 border rounded"
          required
        />
        <input
          type="number"
          name="price"
          placeholder="Price"
          value={gig.price}
          onChange={handleChange}
          className="w-full p-2 border rounded"
          required
        />
        <input
          type="number"
          name="deliveryTime"
          placeholder="Delivery time (in days)"
          value={gig.deliveryTime}
          onChange={handleChange}
          className="w-full p-2 border rounded"
          required
        />
        <select
          name="category"
          value={gig.category}
          onChange={handleChange}
          className="w-full p-2 border rounded"
          required
        >
          <option value="">Select category</option>
          <option value="web">Web Development</option>
          <option value="design">Design</option>
          <option value="writing">Writing</option>
        </select>
        <input
          type="file"
          accept="image/*"
          onChange={handleImageChange}
          className="w-full"
        />
        <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded">
          {updating ? "Update Gig" : "Create Gig"}
        </button>
      </form>
    </div>
  );
}
