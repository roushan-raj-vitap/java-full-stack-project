export default function Modal({ children, onClose }) {
  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded-lg relative w-full max-w-md">
        <button onClick={onClose} className="absolute top-2 right-2 text-gray-600 hover:text-black">
          ✕
        </button>
        {children}
      </div>
    </div>
  );
}
