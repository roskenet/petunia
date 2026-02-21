'use client';

import { useEffect, useState } from 'react';
import { LogIn } from 'lucide-react';
import LogoutButton from "@/components/LogoutButton";
import { Layout, Typography, Space, Button } from 'antd';

const { Header: AntHeader } = Layout;
const { Title, Text } = Typography;

type UserInfo = {
    name: string;
    preferred_username: string;
};

export default function Header() {
    const [user, setUser] = useState<UserInfo | null>(null);

    useEffect(() => {
        fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/me`, {
            credentials: 'include',
        })
            .then((res) => {
                if (!res.ok) throw new Error('Not authenticated');
                return res.json();
            })
            .then((data) => setUser(data))
            .catch(() => setUser(null));
    }, []);

    return (
        <AntHeader style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '0 16px', background: '#fff', borderBottom: '1px solid #f0f0f0' }}>
            <Button type="link" href="/" style={{padding: 0}}>
                <Title level={4} style={{margin: 0, color: '#eb2f96'}}>Project Petunia</Title>
            </Button>
            <Space size="middle">
                {user ? (
                    <>
                        <Text style={{ color: '#595959' }}>👋 Hallo, {user.name}</Text>
                        <LogoutButton />
                    </>
                ) : (
                    <Button 
                        type="link" 
                        href={`${process.env.NEXT_PUBLIC_API_BASE_URL}/oauth2/authorization/keycloak`}
                        icon={<LogIn size={18} />}
                    >
                        Login
                    </Button>
                )}
            </Space>
        </AntHeader>
    );
}
