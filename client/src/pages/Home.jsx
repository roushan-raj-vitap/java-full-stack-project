import { Link } from "react-router-dom";
import { useAuth } from "./context/AuthContext";
import GigList from "./GigList";

const Home = () => {
  const { user } = useAuth();
  console.log(user);
  if (!user) {
    return <div className="text-center text-xl mt-10">Please login to view your dashboard</div>;
  }

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-4">Welcome, {user.name || user.email}</h1>

      {user.role === "CLIENT" ? (
        <ClientDashboard />
      ) : user.role === "FREELANCER" ? (
        <FreelancerDashboard />
      ) : (
        <div>Unknown Role</div>
      )}
    </div>
  );
};

const ClientDashboard = () => {
  return (
    <div>
      <h2 className="text-2xl font-semibold mb-2">Available Freelancers</h2>
      <GigList />
      <div className="mt-4">
        <Link to="/my-orders" className="text-blue-500 underline">
          View My Orders
        </Link>
      </div>
    </div>
  );
};

const FreelancerDashboard = () => {
  return (
    <>
    <div>
      <h2 className="text-2xl font-semibold mb-2">Your Gigs</h2>
      <GigList /> {/* Can be filtered to only freelancer's gigs */}
      <div className="mt-4">
        <Link to="/freelancer/orders" className="text-blue-500 underline">
          View Your Orders
        </Link>
      </div>
    </div>
      </>
  );
};


export default Home;
