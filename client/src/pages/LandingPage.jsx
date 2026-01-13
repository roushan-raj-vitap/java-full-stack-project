import { useNavigate } from "react-router-dom";

export default function LandingPage() {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-6">
      <div className="bg-white shadow-xl rounded-2xl p-10 max-w-md w-full text-center">
        <h1 className="text-3xl font-bold mb-6">Welcome to FreelanceHub</h1>
        <p className="text-gray-600 mb-8">
          Choose your path to get started.
        </p>

        <div className="space-y-4">
          <button
            onClick={() => navigate("/register/client")}
            className="w-full bg-blue-500 hover:bg-blue-600 text-white py-3 rounded-xl font-semibold transition"
          >
            I want to Hire (Client)
          </button>

          <button
            onClick={() => navigate("/register/freelancer")}
            className="w-full bg-green-500 hover:bg-green-600 text-white py-3 rounded-xl font-semibold transition"
          >
            I want to Work (Freelancer)
          </button>
        </div>

        <p className="mt-6 text-gray-500 text-sm">
          Already have an account?{" "}
          <span
            onClick={() => navigate("/login")}
            className="text-blue-500 hover:underline cursor-pointer"
          >
            Login here
          </span>
        </p>
      </div>
    </div>
  );
}
