'use client';

import { useAuthenticatedFetch } from '@/lib/hooks/useAuthenticatedFetch';
import {useEffect, useState} from "react";
import {Client} from "@stomp/stompjs";
import SockJS from 'sockjs-client';
import { Layout, Typography, List, Spin, Alert, Divider, Card, Space } from 'antd';

const { Content } = Layout;
const { Title, Text, Paragraph } = Typography;

type Petunia = {
    name: string;
    image_url: string;
};

export default function PetuniasPage() {
    const [message, setMessage] = useState("Noch nichts empfangen...");
    const [poster, setPoster] = useState("Hello");

    const { data: petunias, error, loading } =
        useAuthenticatedFetch<Petunia[]>('/api/petunias');

    useEffect(() => {
        const socket = new SockJS(`${process.env.NEXT_PUBLIC_API_BASE_URL}/ws`);
        const client = new Client({
            webSocketFactory: () => socket as WebSocket,
            onConnect: () => {
                client.subscribe("/user/queue/petunias", (msg) => {
                    setMessage(msg.body);
                });
                client.subscribe('/topic/petunias', (msg) => {
                    setPoster(msg.body);
                });
            },
            debug: (str) => console.log(str),
        });

        client.activate();

        return () => {
            client.deactivate();
        };
    }, []);

    if (loading) {
        return <Spin tip="Lade Petunien …" />;
    }

    if (error) {
        return <Alert type="error" message={`Fehler: ${error}`} />;
    }

    if (!petunias || petunias.length === 0) {
        return <Alert type="info" message="Keine Petunien gefunden." />;
    }


    return (
        <Layout style={{ minHeight: '100vh', background: 'var(--background)', padding: '24px' }}>
            <Content>
                <Space direction="vertical" size="large" style={{ width: '100%' }}>
                    {/*Ist das immer gleich? 09333279-0663-4195-8c90-9ea0cb1a33d7*/}
                    <Text><p>Willkommen. Diese Seite ist nur für eingelogte User zu sehen.
                        Außerdem kannst Du hier per Nakadi Nachriten per Websocket an diese Seite senden.</p>

                        <p>Hier sieht man alle drei Möglichkeiten, die mir so einfallen, Daten im Frontend
                        anzuzeigen.</p>
                        <p>1. Normaler REST Webservice call.</p>
                        <p>2. Websocket Nachricht für alle Clients.</p>
                        <p>3. Websocket Nachricht nur an Dich, der User.</p>
                    </Text>

                    <Divider />

                    <div>
                        <Title level={2}>Eine Nachricht an alle:</Title>
                        <Paragraph>{poster}</Paragraph>
                    </div>

                    <Divider />

                    <div>
                        <Title level={2}>Petunien</Title>
                        <List
                            bordered
                            dataSource={petunias}
                            renderItem={(p, i) => (
                                <List.Item key={i}>
                                    {p.name}
                                </List.Item>
                            )}
                        />
                    </div>

                    <Card title="🎯 Live-Nachricht:" style={{ width: '100%' }}>
                        <Paragraph>You can set the next message via sending a petunia.message.user nakadi event:</Paragraph>
                        <Paragraph code>{'{\"name\": \"keycloak-user-id\", \"message\": \"The message!\"}'}</Paragraph>
                        <Paragraph strong>{message}</Paragraph>
                    </Card>
                </Space>
            </Content>
        </Layout>
    );
}
