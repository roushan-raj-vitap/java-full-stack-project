import { useState } from "react";
import { useGig } from "./context/context";
import CreateGig from "./CreateGig";
import GigCard from "./GigCard";
import Modal from "./Model";

const GigList = () => {
  const {
    search,
    setSearch,
    category,
    setCategory,
    filteredGigs,
    onDelete,
    // refreshGigs, // assuming this is exposed
  } = useGig();

  const [showModal, setShowModal] = useState(false);
  const [selectedGig, setSelectedGig] = useState(null);

  const handleEdit = (gig) => {
    setSelectedGig(gig);
    setShowModal(true);
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Available Gigs</h2>

      {/* Search + Category Filter */}
      <div className="flex gap-4 mb-6">
        <input
          type="text"
          placeholder="Search by keyword"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="border px-4 py-2 rounded w-1/2"
        />
        <select
          className="border px-4 py-2 rounded"
          value={category}
          onChange={(e) => setCategory(e.target.value)}
        >
          <option value="">All Categories</option>
          <option value="Graphics">Graphics</option>
          <option value="Web Development">Web Development</option>
          <option value="Marketing">Marketing</option>
        </select>
      </div>

      {/* Gigs List */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredGigs.map((gig) => (
          <GigCard
            key={gig.id}
            gig={gig}
            handleEdit={handleEdit}
            handleDelete={onDelete}
          />
        ))}
      </div>

      {/* Modal for Editing */}
      {showModal && (
        <Modal onClose={() => setShowModal(false)}>
          <CreateGig
            gig={selectedGig}
            onSuccess={() => {
            //   refreshGigs();
              setShowModal(false);
            }}
          />
        </Modal>
      )}
    </div>
  );
};

export default GigList;
