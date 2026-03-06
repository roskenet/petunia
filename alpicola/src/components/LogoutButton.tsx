// components/LogoutButton.tsx
'use client';
import { Button } from 'antd';
import { logout } from '@/lib/api';

export default function LogoutButton() {
  const handleLogout = () => {
    void logout();
  };

  return (
    <Button onClick={handleLogout} type="primary">
      Logout
    </Button>
  );
}
