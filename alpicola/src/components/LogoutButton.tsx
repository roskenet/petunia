// components/LogoutButton.tsx
'use client';

export default function LogoutButton() {
  const handleLogout = () => {
    window.location.href = '/logout';
  };

  return (
    <button onClick={handleLogout}>
      Logout
    </button>
  );
}