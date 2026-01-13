import axios from 'axios';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const RegisterFreelancer = () => {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    username: '',
    email: '',
    password: '',
    role: 'FREELANCER', // or 'FREELANCER' based on UI
  });

  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
  e.preventDefault();
  setError('');
  setSuccess('');

  try {
    // 1. Register the user on your backend
    const res = await axios.post('http://localhost:8080/user/register', form);

    // 2. Create a Stripe connected account
    const accountRes = await axios.post('http://localhost:8080/api/stripe/create-account', null, {
      params: { email: form.email },
    });
    const accountId = accountRes.data;

    // 3. Generate the onboarding link
    const linkRes = await axios.get('http://localhost:8080/api/stripe/account-link', {
      params: { accountId },
    });

    const onboardingUrl = linkRes.data;

    // 4. Redirect the user to Stripe onboarding
    window.location.href = onboardingUrl;

  } catch (err) {
    setError(err.response?.data || 'Something went wrong');
  }
};



  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100 px-4">
      <div className="max-w-md w-full bg-white p-8 rounded-lg shadow-lg">
        <h2 className="text-2xl font-bold mb-6 text-center">Register</h2>

        {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
        {success && <p className="text-green-600 text-sm mb-4">{success}</p>}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block mb-1 font-medium">Name</label>
            <input
              type="text"
              name="username"
              value={form.username}
              onChange={handleChange}
              required
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div>
            <label className="block mb-1 font-medium">Email</label>
            <input
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              required
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div>
            <label className="block mb-1 font-medium">Password</label>
            <input
              type="password"
              name="password"
              value={form.password}
              onChange={handleChange}
              required
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div>
            <label className="block mb-1 font-medium">Role: Freelancer</label>
          </div>

          <button
            type="submit"
            className="w-full bg-green-600 text-white py-2 rounded-md hover:bg-green-700 transition duration-200"
          >
            Register
          </button>
        </form>

        <p className="mt-4 text-center text-sm">
          Already have an account?{' '}
          <span
            onClick={() => navigate('/login')}
            className="text-blue-600 hover:underline cursor-pointer"
          >
            Login
          </span>
        </p>
      </div>
    </div>
  );
};

export default RegisterFreelancer;
