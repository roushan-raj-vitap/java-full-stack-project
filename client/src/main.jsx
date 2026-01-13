import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import App from './App.jsx';
import './index.css';
import { AuthProvider } from './pages/context/AuthContext.jsx';
import CustomGigContext from './pages/context/context.jsx';
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
    <AuthProvider>
    <CustomGigContext>
    <App />
    </CustomGigContext>
    </AuthProvider>
    </BrowserRouter>
    
  </StrictMode>,
)
