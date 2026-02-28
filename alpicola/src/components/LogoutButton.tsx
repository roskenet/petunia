// components/LogoutButton.tsx
'use client';
import { Button } from 'antd';

export default function LogoutButton() {
  const handleLogout = () => {
    window.location.href = `${process.env.NEXT_PUBLIC_API_BASE_URL}/logout`;
  };

  return (
    <Button onClick={handleLogout} type="primary">
      Logout
    </Button>
  );
}
